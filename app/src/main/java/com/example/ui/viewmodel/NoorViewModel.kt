package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.calculator.HijriCalendarHelper
import com.example.data.calculator.PrayerTimeCalculator
import com.example.data.calculator.QiblaCalculator
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.DailyCompanionRepository
import com.example.data.repository.NoorRepository
import com.example.util.AzanSoundPlayer
import com.example.util.CompassManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AtmosphereTime {
    FAJR,
    MORNING,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA
}

data class NoorUiState(
    val cityName: String = "الرياض، المملكة العربية السعودية",
    val latitude: Double = 24.7136,
    val longitude: Double = 46.6753,
    val hijriDate: HijriCalendarHelper.HijriDate = HijriCalendarHelper.getHijriDate(Calendar.getInstance()),
    val gregorianDateFormatted: String = "",
    val timeOfDayGreeting: String = "السلام عليكم ورحمة الله",
    val atmosphere: AtmosphereTime = AtmosphereTime.ISHA,
    val todayPrayerTimes: List<PrayerTimeInfo> = emptyList(),
    val nextPrayer: PrayerTimeInfo? = null,
    val countdownText: String = "00:00:00",
    val countdownProgress: Float = 0.5f,
    val selectedDate: Calendar = Calendar.getInstance(),
    val selectedDatePrayerTimes: List<PrayerTimeInfo> = emptyList(),
    val todayPrayerLogs: Map<PrayerType, PrayerStatus> = emptyMap(),
    val weeklyPrayerLogs: List<PrayerLogEntity> = emptyList(),
    val quranPagesRead: Int = 3,
    val quranTargetPages: Int = 5,
    val lastReadSurahName: String = "الفاتحة",
    val lastReadAyahNumber: Int = 1,
    val tasbihCount: Int = 33,
    val tasbihTarget: Int = 100,
    val selectedDhikrName: String = "سبحان الله وبحمده",
    val dailyChecklist: DailyChecklistEntity = DailyChecklistEntity(""),
    val calculationMethod: CalculationMethod = CalculationMethod.UMM_AL_QURA,
    val juristicMethod: JuristicMethod = JuristicMethod.STANDARD,
    val prePrayerAlertMinutes: Int = 3,
    val isFullAzanSound: Boolean = true,
    val prayerAzanEnabled: Map<PrayerType, Boolean> = PrayerType.values().associateWith { true },
    val qiblaBearing: Float = 0f,
    val distanceToMakkahKm: Int = 0,
    val randomOpeningDhikr: String = "",
    val dailyDeed: com.example.data.repository.DailyDeed = DailyCompanionRepository.dailyDeeds[0],
    val dailyHadith: com.example.data.repository.DailyHadith = DailyCompanionRepository.dailyHadiths[0],
    val dailyAyah: com.example.data.repository.DailyAyah = DailyCompanionRepository.dailyAyahs[0],
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val isMosqueMode: Boolean = false,
    val isTravelMode: Boolean = false,
    val isARModeActive: Boolean = false,
    val selectedMuezzin: MuezzinVoice = MuezzinVoice.MAKKAH,
    val azanVolume: Float = 0.85f,
    val isAzanAudioPlaying: Boolean = false,
    val playingMuezzin: MuezzinVoice? = null
)

class NoorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoorRepository
    val compassManager = CompassManager(application.applicationContext)

    private val _uiState = MutableStateFlow(NoorUiState())
    val uiState: StateFlow<NoorUiState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayDateFormat = SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar"))

    init {
        val db = NoorDatabase.getInstance(application)
        repository = NoorRepository(db.noorDao())

        // Initial setup
        val initialCal = Calendar.getInstance()
        val todayStr = dateFormat.format(initialCal.time)
        val initialHijri = HijriCalendarHelper.getHijriDate(initialCal)

        val qibla = QiblaCalculator.calculateQiblaBearing(_uiState.value.latitude, _uiState.value.longitude)
        val dist = QiblaCalculator.calculateDistanceToKaabaKm(_uiState.value.latitude, _uiState.value.longitude)
        compassManager.targetQiblaAngle = qibla

        val randomDhikr = DailyCompanionRepository.randomOpeningAzkar.random()

        _uiState.update {
            it.copy(
                gregorianDateFormatted = displayDateFormat.format(initialCal.time),
                hijriDate = initialHijri,
                qiblaBearing = qibla,
                distanceToMakkahKm = dist,
                randomOpeningDhikr = randomDhikr
            )
        }

        recalculatePrayerTimes()
        startLiveTimer()
        observeDatabase(todayStr)
        observeSoundPlayback()
    }

    private fun observeSoundPlayback() {
        viewModelScope.launch {
            AzanSoundPlayer.isPlaying.collect { playing ->
                _uiState.update { it.copy(isAzanAudioPlaying = playing) }
            }
        }
        viewModelScope.launch {
            AzanSoundPlayer.playingMuezzin.collect { muezzin ->
                _uiState.update { it.copy(playingMuezzin = muezzin) }
            }
        }
    }

    private fun observeDatabase(todayStr: String) {
        viewModelScope.launch {
            repository.getPrayerLogsForDate(todayStr).collect { logs ->
                val map = mutableMapOf<PrayerType, PrayerStatus>()
                for (type in PrayerType.values()) {
                    val log = logs.find { it.prayerName == type.name }
                    map[type] = when (log?.status) {
                        "ON_TIME" -> PrayerStatus.ON_TIME
                        "LATE" -> PrayerStatus.LATE
                        "MISSED" -> PrayerStatus.MISSED
                        else -> PrayerStatus.NOT_RECORDED
                    }
                }
                _uiState.update { it.copy(todayPrayerLogs = map) }
            }
        }

        viewModelScope.launch {
            repository.getRecentPrayerLogs().collect { logs ->
                _uiState.update { it.copy(weeklyPrayerLogs = logs) }
            }
        }

        viewModelScope.launch {
            repository.getQuranProgress(todayStr).collect { progress ->
                if (progress != null) {
                    _uiState.update {
                        it.copy(
                            quranPagesRead = progress.pagesRead,
                            quranTargetPages = progress.targetPages,
                            lastReadSurahName = progress.lastSurahName,
                            lastReadAyahNumber = progress.lastAyahNumber
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            repository.getAllBookmarks().collect { list ->
                _uiState.update { it.copy(bookmarks = list) }
            }
        }

        viewModelScope.launch {
            repository.getDailyChecklist(todayStr).collect { checklist ->
                if (checklist != null) {
                    _uiState.update { it.copy(dailyChecklist = checklist) }
                }
            }
        }
    }

    private fun startLiveTimer() {
        viewModelScope.launch {
            while (isActive) {
                val now = Calendar.getInstance()
                updateAtmosphereAndGreeting(now)
                updateCountdown(now)
                delay(1000)
            }
        }
    }

    fun recalculatePrayerTimes() {
        val nowCal = Calendar.getInstance()
        val todayTimes = PrayerTimeCalculator.calculatePrayerTimes(
            calendar = nowCal,
            latitude = _uiState.value.latitude,
            longitude = _uiState.value.longitude,
            method = _uiState.value.calculationMethod,
            juristicMethod = _uiState.value.juristicMethod
        )

        val nextP = todayTimes.find { it.isNext } ?: todayTimes.firstOrNull { it.type == PrayerType.FAJR }

        val selectedTimes = PrayerTimeCalculator.calculatePrayerTimes(
            calendar = _uiState.value.selectedDate,
            latitude = _uiState.value.latitude,
            longitude = _uiState.value.longitude,
            method = _uiState.value.calculationMethod,
            juristicMethod = _uiState.value.juristicMethod
        )

        _uiState.update {
            it.copy(
                todayPrayerTimes = todayTimes,
                nextPrayer = nextP,
                selectedDatePrayerTimes = selectedTimes
            )
        }
    }

    private fun updateAtmosphereAndGreeting(now: Calendar) {
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val atmosphere = when {
            hour in 4..5 -> AtmosphereTime.FAJR
            hour in 6..11 -> AtmosphereTime.MORNING
            hour in 12..14 -> AtmosphereTime.DHUHR
            hour in 15..17 -> AtmosphereTime.ASR
            hour in 18..19 -> AtmosphereTime.MAGHRIB
            else -> AtmosphereTime.ISHA
        }

        val greeting = when (atmosphere) {
            AtmosphereTime.FAJR -> "السلام عليكم، فجر مبارك 🌙"
            AtmosphereTime.MORNING -> "السلام عليكم، صباح الخير والبركة ☀️"
            AtmosphereTime.DHUHR -> "السلام عليكم، يومك عامر بذكر الله 🤍"
            AtmosphereTime.ASR -> "السلام عليكم، مساء الخير والسكينة ✨"
            AtmosphereTime.MAGHRIB -> "السلام عليكم، تقبل الله طاعاتكم 🌅"
            AtmosphereTime.ISHA -> "السلام عليكم، ليلة هانئة مباركة 🌙"
        }

        _uiState.update {
            it.copy(
                atmosphere = atmosphere,
                timeOfDayGreeting = greeting
            )
        }
    }

    private fun updateCountdown(now: Calendar) {
        val next = _uiState.value.nextPrayer ?: return
        val diffMillis = next.timestampMillis - now.timeInMillis
        val remaining = if (diffMillis > 0) diffMillis else 0L

        val hours = (remaining / (1000 * 60 * 60)) % 24
        val minutes = (remaining / (1000 * 60)) % 60
        val seconds = (remaining / 1000) % 60

        val formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        val totalWindow = 4 * 3600 * 1000f // 4 hours window
        val progress = (1f - (remaining / totalWindow)).coerceIn(0.05f, 1f)

        _uiState.update {
            it.copy(
                countdownText = formatted,
                countdownProgress = progress
            )
        }
    }

    fun setSelectedDate(calendar: Calendar) {
        val selectedTimes = PrayerTimeCalculator.calculatePrayerTimes(
            calendar = calendar,
            latitude = _uiState.value.latitude,
            longitude = _uiState.value.longitude,
            method = _uiState.value.calculationMethod,
            juristicMethod = _uiState.value.juristicMethod
        )
        _uiState.update {
            it.copy(
                selectedDate = calendar,
                selectedDatePrayerTimes = selectedTimes
            )
        }
    }

    fun logPrayerStatus(prayerType: PrayerType, status: PrayerStatus) {
        val todayStr = dateFormat.format(Calendar.getInstance().time)
        viewModelScope.launch {
            val statusStr = when (status) {
                PrayerStatus.ON_TIME -> "ON_TIME"
                PrayerStatus.LATE -> "LATE"
                PrayerStatus.MISSED -> "MISSED"
                PrayerStatus.NOT_RECORDED -> "NOT_RECORDED"
            }
            repository.savePrayerLog(todayStr, prayerType.name, statusStr)
        }
    }

    fun incrementTasbih() {
        compassManager.triggerHapticFeedback()
        val newCount = _uiState.value.tasbihCount + 1
        _uiState.update { it.copy(tasbihCount = newCount) }
        viewModelScope.launch {
            repository.saveTasbihRecord(
                dhikrName = _uiState.value.selectedDhikrName,
                count = newCount,
                target = _uiState.value.tasbihTarget
            )
        }
    }

    fun resetTasbih() {
        _uiState.update { it.copy(tasbihCount = 0) }
        viewModelScope.launch {
            repository.saveTasbihRecord(
                dhikrName = _uiState.value.selectedDhikrName,
                count = 0,
                target = _uiState.value.tasbihTarget
            )
        }
    }

    fun selectDhikr(name: String, target: Int = 100) {
        _uiState.update {
            it.copy(
                selectedDhikrName = name,
                tasbihTarget = target,
                tasbihCount = 0
            )
        }
    }

    fun updateQuranProgress(pagesRead: Int, surahName: String = "", ayahNum: Int = 1) {
        val todayStr = dateFormat.format(Calendar.getInstance().time)
        val sName = if (surahName.isNotEmpty()) surahName else _uiState.value.lastReadSurahName
        _uiState.update {
            it.copy(
                quranPagesRead = pagesRead,
                lastReadSurahName = sName,
                lastReadAyahNumber = ayahNum
            )
        }
        viewModelScope.launch {
            repository.saveQuranProgress(
                QuranProgressEntity(
                    dateString = todayStr,
                    pagesRead = pagesRead,
                    targetPages = _uiState.value.quranTargetPages,
                    lastSurahName = sName,
                    lastAyahNumber = ayahNum
                )
            )
        }
    }

    fun toggleChecklist(type: String) {
        val todayStr = dateFormat.format(Calendar.getInstance().time)
        val current = _uiState.value.dailyChecklist
        val updated = when (type) {
            "morning" -> current.copy(dateString = todayStr, morningAzkarDone = !current.morningAzkarDone)
            "evening" -> current.copy(dateString = todayStr, eveningAzkarDone = !current.eveningAzkarDone)
            "sleep" -> current.copy(dateString = todayStr, sleepAzkarDone = !current.sleepAzkarDone)
            "duha" -> current.copy(dateString = todayStr, duhaPrayerDone = !current.duhaPrayerDone)
            "witr" -> current.copy(dateString = todayStr, witrPrayerDone = !current.witrPrayerDone)
            "charity" -> current.copy(dateString = todayStr, charityDone = !current.charityDone)
            else -> current
        }
        _uiState.update { it.copy(dailyChecklist = updated) }
        viewModelScope.launch {
            repository.saveDailyChecklist(updated)
        }
    }

    fun addBookmark(surahNumber: Int, ayahNumber: Int, surahName: String, ayahText: String) {
        viewModelScope.launch {
            repository.addBookmark(
                BookmarkEntity(
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    surahName = surahName,
                    ayahText = ayahText
                )
            )
        }
    }

    fun removeBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.removeBookmark(bookmark)
        }
    }

    fun setCalculationMethod(method: CalculationMethod) {
        _uiState.update { it.copy(calculationMethod = method) }
        recalculatePrayerTimes()
    }

    fun setJuristicMethod(method: JuristicMethod) {
        _uiState.update { it.copy(juristicMethod = method) }
        recalculatePrayerTimes()
    }

    fun setPrePrayerAlertMinutes(minutes: Int) {
        _uiState.update { it.copy(prePrayerAlertMinutes = minutes) }
    }

    fun togglePerPrayerAzan(prayerType: PrayerType) {
        val map = _uiState.value.prayerAzanEnabled.toMutableMap()
        map[prayerType] = !(map[prayerType] ?: true)
        _uiState.update { it.copy(prayerAzanEnabled = map) }
    }

    fun toggleMosqueMode() {
        _uiState.update { it.copy(isMosqueMode = !it.isMosqueMode) }
    }

    fun toggleTravelMode() {
        _uiState.update { it.copy(isTravelMode = !it.isTravelMode) }
    }

    fun toggleARMode() {
        _uiState.update { it.copy(isARModeActive = !it.isARModeActive) }
    }

    fun playPreviewChime(isFull: Boolean = false) {
        AzanSoundPlayer.playAlertChime(isFull, _uiState.value.azanVolume)
    }

    fun setSelectedMuezzin(muezzin: MuezzinVoice) {
        _uiState.update { it.copy(selectedMuezzin = muezzin) }
        if (_uiState.value.isAzanAudioPlaying) {
            playMuezzinPreview(muezzin)
        }
    }

    fun setAzanVolume(volume: Float) {
        val clamped = volume.coerceIn(0.0f, 1.0f)
        _uiState.update { it.copy(azanVolume = clamped) }
    }

    fun playMuezzinPreview(muezzin: MuezzinVoice) {
        AzanSoundPlayer.playMuezzinPreview(muezzin, _uiState.value.azanVolume)
    }

    fun stopMuezzinPreview() {
        AzanSoundPlayer.stop()
    }

    fun toggleMuezzinPreview(muezzin: MuezzinVoice) {
        if (_uiState.value.isAzanAudioPlaying && _uiState.value.playingMuezzin == muezzin) {
            stopMuezzinPreview()
        } else {
            playMuezzinPreview(muezzin)
        }
    }

    fun setCustomLocation(cityName: String, lat: Double, lng: Double) {
        val qibla = QiblaCalculator.calculateQiblaBearing(lat, lng)
        val dist = QiblaCalculator.calculateDistanceToKaabaKm(lat, lng)
        compassManager.targetQiblaAngle = qibla

        _uiState.update {
            it.copy(
                cityName = cityName,
                latitude = lat,
                longitude = lng,
                qiblaBearing = qibla,
                distanceToMakkahKm = dist
            )
        }
        recalculatePrayerTimes()
    }
}
