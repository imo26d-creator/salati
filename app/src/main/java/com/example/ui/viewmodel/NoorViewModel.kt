package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.calculator.HijriCalendarHelper
import com.example.data.calculator.PrayerTimeCalculator
import com.example.data.calculator.QiblaCalculator
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.DailyCompanionRepository
import com.example.data.repository.NoorRepository
import com.example.data.repository.PresetDhikr
import com.example.data.repository.QuranDataRepository
import com.example.data.repository.TasbihCategory
import com.example.data.repository.TasbihDataRepository
import com.example.util.AzanSoundPlayer
import com.example.util.AzkarNotificationScheduler
import com.example.util.CompassManager
import com.example.util.PrayerNotificationScheduler
import com.example.util.QuranAudioPlayer
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
    val selectedQuranReciter: QuranReciter = QuranDataRepository.recitersList[0],
    val quranReaderFontSize: Int = 22,
    val selectedPresetDhikr: PresetDhikr = TasbihDataRepository.presetDhikrList[0],
    val tasbihCount: Int = 0,
    val tasbihTarget: Int = 33,
    val tasbihLaps: Int = 0,
    val todayTotalTasbihCount: Int = 33,
    val selectedDhikrName: String = "سُبْحَانَ اللَّهِ",
    val isTasbihHapticEnabled: Boolean = true,
    val isTasbihSoundEnabled: Boolean = true,
    val isTasbihFullScreenTapEnabled: Boolean = false,
    val showTasbihGoalCelebration: Boolean = false,
    val customDhikrList: List<PresetDhikr> = emptyList(),
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
    val playingMuezzin: MuezzinVoice? = null,
    val playingPrayer: PrayerType? = null,
    val prayerAzanConfigs: Map<PrayerType, PrayerAzanConfig> = mapOf(
        PrayerType.FAJR to PrayerAzanConfig(
            prayerType = PrayerType.FAJR,
            isEnabled = true,
            muezzin = MuezzinVoice.MADINAH,
            volume = 0.90f,
            alertType = AzanAlertType.FULL_AZAN
        ),
        PrayerType.SUNRISE to PrayerAzanConfig(
            prayerType = PrayerType.SUNRISE,
            isEnabled = true,
            muezzin = MuezzinVoice.TAKBEERAT,
            volume = 0.65f,
            alertType = AzanAlertType.TAKBEER_ONLY
        ),
        PrayerType.DHUHR to PrayerAzanConfig(
            prayerType = PrayerType.DHUHR,
            isEnabled = true,
            muezzin = MuezzinVoice.MAKKAH,
            volume = 0.80f,
            alertType = AzanAlertType.FULL_AZAN
        ),
        PrayerType.ASR to PrayerAzanConfig(
            prayerType = PrayerType.ASR,
            isEnabled = true,
            muezzin = MuezzinVoice.MISHARY,
            volume = 0.80f,
            alertType = AzanAlertType.FULL_AZAN
        ),
        PrayerType.MAGHRIB to PrayerAzanConfig(
            prayerType = PrayerType.MAGHRIB,
            isEnabled = true,
            muezzin = MuezzinVoice.AL_AQSA,
            volume = 0.85f,
            alertType = AzanAlertType.FULL_AZAN
        ),
        PrayerType.ISHA to PrayerAzanConfig(
            prayerType = PrayerType.ISHA,
            isEnabled = true,
            muezzin = MuezzinVoice.ABDULBASIT,
            volume = 0.85f,
            alertType = AzanAlertType.FULL_AZAN
        )
    ),
    val isAutoPhoneTime: Boolean = true,
    val is24HourFormat: Boolean = false,
    val deviceCurrentTimeFormatted: String = "",
    val deviceTimeZoneName: String = "",
    val prayerManualOffsets: Map<PrayerType, Int> = emptyMap(),
    val timeSyncSuccessMessage: String? = null,
    val morningAzkarEnabled: Boolean = true,
    val morningAzkarHour: Int = 6,
    val morningAzkarMinute: Int = 30,
    val eveningAzkarEnabled: Boolean = true,
    val eveningAzkarHour: Int = 17,
    val eveningAzkarMinute: Int = 30,
    val azkarStreakDays: Int = 3,
    val targetOpenAzkarCategory: DhikrCategory? = null,
    val azkarFeedbackMessage: String? = null,
    val hijriDateAdjustmentDays: Int = 0,
    val manualTimeOffsetMinutes: Int = 0,
    val isCustomDateSelected: Boolean = false,
    val dateSyncSuccessMessage: String? = null
)

class NoorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoorRepository
    val compassManager = CompassManager(application.applicationContext)

    private val _uiState = MutableStateFlow(NoorUiState())
    val uiState: StateFlow<NoorUiState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayDateFormat = SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar"))

    init {
        AzanSoundPlayer.init(application.applicationContext)
        val db = NoorDatabase.getInstance(application)
        repository = NoorRepository(db.noorDao())

        // Initial setup
        val initialCal = Calendar.getInstance()
        val todayStr = dateFormat.format(initialCal.time)

        // Time and Date settings persistence
        val timePrefs = application.applicationContext.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
        val savedHijriAdj = timePrefs.getInt("pref_hijri_adjustment_days", 0)
        val savedTimeOffset = timePrefs.getInt("pref_manual_time_offset_minutes", 0)
        val savedAutoTime = timePrefs.getBoolean("pref_is_auto_phone_time", true)
        val saved24Hour = timePrefs.getBoolean("pref_is_24_hour_format", false)

        val effectiveCal = (initialCal.clone() as Calendar).apply {
            if (savedTimeOffset != 0) {
                add(Calendar.MINUTE, savedTimeOffset)
            }
        }
        val initialHijri = HijriCalendarHelper.getHijriDate(effectiveCal, savedHijriAdj)

        val qibla = QiblaCalculator.calculateQiblaBearing(_uiState.value.latitude, _uiState.value.longitude)
        val dist = QiblaCalculator.calculateDistanceToKaabaKm(_uiState.value.latitude, _uiState.value.longitude)
        compassManager.targetQiblaAngle = qibla

        val randomDhikr = DailyCompanionRepository.randomOpeningAzkar.random()

        // Azkar Notifications initial setup
        val azkarPrefs = AzkarNotificationScheduler.getPrefs(application.applicationContext)
        val mEnabled = azkarPrefs.getBoolean(AzkarNotificationScheduler.KEY_MORNING_ENABLED, true)
        val mHour = azkarPrefs.getInt(AzkarNotificationScheduler.KEY_MORNING_HOUR, 6)
        val mMin = azkarPrefs.getInt(AzkarNotificationScheduler.KEY_MORNING_MINUTE, 30)
        val eEnabled = azkarPrefs.getBoolean(AzkarNotificationScheduler.KEY_EVENING_ENABLED, true)
        val eHour = azkarPrefs.getInt(AzkarNotificationScheduler.KEY_EVENING_HOUR, 17)
        val eMin = azkarPrefs.getInt(AzkarNotificationScheduler.KEY_EVENING_MINUTE, 30)
        val streak = azkarPrefs.getInt(AzkarNotificationScheduler.KEY_STREAK_COUNT, 3)

        AzkarNotificationScheduler.scheduleAllConfigured(application.applicationContext)

        _uiState.update {
            it.copy(
                gregorianDateFormatted = displayDateFormat.format(initialCal.time),
                hijriDate = initialHijri,
                qiblaBearing = qibla,
                distanceToMakkahKm = dist,
                randomOpeningDhikr = randomDhikr,
                morningAzkarEnabled = mEnabled,
                morningAzkarHour = mHour,
                morningAzkarMinute = mMin,
                eveningAzkarEnabled = eEnabled,
                eveningAzkarHour = eHour,
                eveningAzkarMinute = eMin,
                azkarStreakDays = streak,
                hijriDateAdjustmentDays = savedHijriAdj,
                manualTimeOffsetMinutes = savedTimeOffset,
                isAutoPhoneTime = savedAutoTime,
                is24HourFormat = saved24Hour
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
        viewModelScope.launch {
            AzanSoundPlayer.playingPrayer.collect { prayer ->
                _uiState.update { it.copy(playingPrayer = prayer) }
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

    private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
               c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    private fun startLiveTimer() {
        viewModelScope.launch {
            val timeFormat12 = SimpleDateFormat("hh:mm:ss a", Locale("ar"))
            val timeFormat24 = SimpleDateFormat("HH:mm:ss", Locale.US)
            while (isActive) {
                val realNow = Calendar.getInstance()
                val tz = TimeZone.getDefault()
                val offsetHours = tz.getOffset(realNow.timeInMillis) / 3600000
                val sign = if (offsetHours >= 0) "+" else ""
                val tzFormatted = "GMT$sign$offsetHours (${tz.displayName})"

                val offsetMin = _uiState.value.manualTimeOffsetMinutes
                val effectiveNow = (realNow.clone() as Calendar).apply {
                    if (offsetMin != 0) {
                        add(Calendar.MINUTE, offsetMin)
                    }
                }

                val formattedTime = if (_uiState.value.is24HourFormat) {
                    timeFormat24.format(effectiveNow.time)
                } else {
                    timeFormat12.format(effectiveNow.time)
                }

                _uiState.update {
                    it.copy(
                        deviceCurrentTimeFormatted = formattedTime,
                        deviceTimeZoneName = tzFormatted
                    )
                }

                updateAtmosphereAndGreeting(effectiveNow)
                updateCountdown(effectiveNow)
                delay(1000)
            }
        }
    }

    fun recalculatePrayerTimes() {
        val offsetMin = _uiState.value.manualTimeOffsetMinutes
        val nowCal = (Calendar.getInstance().clone() as Calendar).apply {
            if (offsetMin != 0) {
                add(Calendar.MINUTE, offsetMin)
            }
        }
        val todayTimes = PrayerTimeCalculator.calculatePrayerTimes(
            calendar = nowCal,
            latitude = _uiState.value.latitude,
            longitude = _uiState.value.longitude,
            method = _uiState.value.calculationMethod,
            juristicMethod = _uiState.value.juristicMethod,
            manualOffsetMinutes = _uiState.value.prayerManualOffsets,
            is24HourFormat = _uiState.value.is24HourFormat
        )

        val nextP = todayTimes.find { it.isNext } ?: todayTimes.firstOrNull { it.type == PrayerType.FAJR }

        val selectedCal = _uiState.value.selectedDate
        val selectedTimes = if (isSameDay(selectedCal, Calendar.getInstance())) {
            todayTimes
        } else {
            PrayerTimeCalculator.calculatePrayerTimes(
                calendar = selectedCal,
                latitude = _uiState.value.latitude,
                longitude = _uiState.value.longitude,
                method = _uiState.value.calculationMethod,
                juristicMethod = _uiState.value.juristicMethod,
                manualOffsetMinutes = _uiState.value.prayerManualOffsets,
                is24HourFormat = _uiState.value.is24HourFormat
            )
        }

        val currentHijri = HijriCalendarHelper.getHijriDate(selectedCal, _uiState.value.hijriDateAdjustmentDays)

        _uiState.update {
            it.copy(
                todayPrayerTimes = todayTimes,
                nextPrayer = nextP,
                selectedDatePrayerTimes = selectedTimes,
                hijriDate = currentHijri,
                gregorianDateFormatted = displayDateFormat.format(selectedCal.time)
            )
        }
        schedulePrayerNotifications()
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
        val isToday = isSameDay(calendar, Calendar.getInstance())
        val selectedTimes = PrayerTimeCalculator.calculatePrayerTimes(
            calendar = calendar,
            latitude = _uiState.value.latitude,
            longitude = _uiState.value.longitude,
            method = _uiState.value.calculationMethod,
            juristicMethod = _uiState.value.juristicMethod,
            manualOffsetMinutes = _uiState.value.prayerManualOffsets,
            is24HourFormat = _uiState.value.is24HourFormat
        )
        val currentHijri = HijriCalendarHelper.getHijriDate(calendar, _uiState.value.hijriDateAdjustmentDays)
        _uiState.update {
            it.copy(
                selectedDate = calendar,
                selectedDatePrayerTimes = selectedTimes,
                hijriDate = currentHijri,
                gregorianDateFormatted = displayDateFormat.format(calendar.time),
                isCustomDateSelected = !isToday,
                dateSyncSuccessMessage = if (!isToday) "تم تغيير التاريخ المعتمد إلى: ${displayDateFormat.format(calendar.time)} 📅" else null
            )
        }
        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(dateSyncSuccessMessage = null) }
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
        val state = _uiState.value
        if (state.isTasbihHapticEnabled) {
            compassManager.triggerHapticFeedback()
        }
        if (state.isTasbihSoundEnabled) {
            AzanSoundPlayer.playTasbihClick()
        }

        val target = state.tasbihTarget
        val currentCount = state.tasbihCount
        val nextCount = currentCount + 1
        val newDailyTotal = state.todayTotalTasbihCount + 1

        val hasReachedTarget = target > 0 && nextCount >= target
        val newCount = if (hasReachedTarget) 0 else nextCount
        val newLaps = if (hasReachedTarget) state.tasbihLaps + 1 else state.tasbihLaps
        val showCelebration = hasReachedTarget

        if (hasReachedTarget) {
            if (state.isTasbihSoundEnabled) {
                AzanSoundPlayer.playGoalCompletionChime()
            }
            if (state.isTasbihHapticEnabled) {
                // Double vibration pulse for milestone
                compassManager.triggerHapticFeedback()
            }
        }

        _uiState.update {
            it.copy(
                tasbihCount = newCount,
                tasbihLaps = newLaps,
                todayTotalTasbihCount = newDailyTotal,
                showTasbihGoalCelebration = showCelebration
            )
        }

        viewModelScope.launch {
            repository.saveTasbihRecord(
                dhikrName = state.selectedDhikrName,
                count = if (hasReachedTarget) target else newCount,
                target = target
            )
        }
    }

    fun decrementTasbih() {
        val state = _uiState.value
        if (state.tasbihCount > 0) {
            val newCount = state.tasbihCount - 1
            val newDailyTotal = (state.todayTotalTasbihCount - 1).coerceAtLeast(0)
            if (state.isTasbihHapticEnabled) {
                compassManager.triggerHapticFeedback()
            }
            _uiState.update {
                it.copy(
                    tasbihCount = newCount,
                    todayTotalTasbihCount = newDailyTotal
                )
            }
            viewModelScope.launch {
                repository.saveTasbihRecord(
                    dhikrName = state.selectedDhikrName,
                    count = newCount,
                    target = state.tasbihTarget
                )
            }
        }
    }

    fun resetTasbih() {
        val state = _uiState.value
        if (state.isTasbihHapticEnabled) {
            compassManager.triggerHapticFeedback()
        }
        _uiState.update {
            it.copy(
                tasbihCount = 0,
                showTasbihGoalCelebration = false
            )
        }
        viewModelScope.launch {
            repository.saveTasbihRecord(
                dhikrName = state.selectedDhikrName,
                count = 0,
                target = state.tasbihTarget
            )
        }
    }

    fun resetTasbihLaps() {
        _uiState.update { it.copy(tasbihLaps = 0) }
    }

    fun dismissTasbihCelebration() {
        _uiState.update { it.copy(showTasbihGoalCelebration = false) }
    }

    fun selectPresetDhikr(dhikr: PresetDhikr, target: Int? = null) {
        val chosenTarget = target ?: dhikr.defaultTarget
        _uiState.update {
            it.copy(
                selectedPresetDhikr = dhikr,
                selectedDhikrName = dhikr.arabicText,
                tasbihTarget = chosenTarget,
                tasbihCount = 0,
                tasbihLaps = 0,
                showTasbihGoalCelebration = false
            )
        }
    }

    fun setTasbihTarget(target: Int) {
        _uiState.update {
            it.copy(
                tasbihTarget = target,
                tasbihCount = 0
            )
        }
    }

    fun toggleTasbihHaptic() {
        _uiState.update { it.copy(isTasbihHapticEnabled = !it.isTasbihHapticEnabled) }
    }

    fun toggleTasbihSound() {
        _uiState.update { it.copy(isTasbihSoundEnabled = !it.isTasbihSoundEnabled) }
    }

    fun toggleTasbihFullScreenTap() {
        _uiState.update { it.copy(isTasbihFullScreenTapEnabled = !it.isTasbihFullScreenTapEnabled) }
    }

    fun addCustomDhikr(arabicText: String, target: Int = 33, virtue: String = "") {
        val customDhikr = PresetDhikr(
            id = "custom_${System.currentTimeMillis()}",
            arabicText = arabicText.trim(),
            transliteration = "",
            translation = "",
            defaultTarget = target,
            category = TasbihCategory.CUSTOM,
            virtueArabic = if (virtue.isBlank()) "ذكر مخصص من اختيارك وتقربك إلى الله." else virtue.trim(),
            hadithReference = "ذكر شخصي",
            isCustom = true
        )
        _uiState.update {
            val updatedList = it.customDhikrList + customDhikr
            it.copy(
                customDhikrList = updatedList,
                selectedPresetDhikr = customDhikr,
                selectedDhikrName = customDhikr.arabicText,
                tasbihTarget = target,
                tasbihCount = 0,
                tasbihLaps = 0
            )
        }
    }

    fun deleteCustomDhikr(id: String) {
        _uiState.update {
            val updatedList = it.customDhikrList.filterNot { item -> item.id == id }
            val fallback = if (it.selectedPresetDhikr.id == id) TasbihDataRepository.presetDhikrList[0] else it.selectedPresetDhikr
            it.copy(
                customDhikrList = updatedList,
                selectedPresetDhikr = fallback,
                selectedDhikrName = fallback.arabicText
            )
        }
    }

    fun selectDhikr(name: String, target: Int = 100) {
        val matched = TasbihDataRepository.presetDhikrList.find { it.arabicText == name }
            ?: PresetDhikr(
                id = "custom_${System.currentTimeMillis()}",
                arabicText = name,
                defaultTarget = target,
                category = TasbihCategory.TAHMID_TASBIH
            )
        selectPresetDhikr(matched, target)
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

    fun selectQuranReciter(reciter: QuranReciter) {
        _uiState.update { it.copy(selectedQuranReciter = reciter) }
        QuranAudioPlayer.setReciter(reciter)
    }

    fun setQuranReaderFontSize(fontSize: Int) {
        _uiState.update { it.copy(quranReaderFontSize = fontSize.coerceIn(16, 38)) }
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
        schedulePrayerNotifications()
    }

    fun togglePerPrayerAzan(prayerType: PrayerType) {
        val map = _uiState.value.prayerAzanEnabled.toMutableMap()
        map[prayerType] = !(map[prayerType] ?: true)
        _uiState.update { it.copy(prayerAzanEnabled = map) }
        schedulePrayerNotifications()
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

    fun playPreviewTakbeerAlert() {
        AzanSoundPlayer.playTakbeerAlert(_uiState.value.azanVolume)
    }

    fun playPreviewReciterAlert() {
        AzanSoundPlayer.playReciterAyahAlert(_uiState.value.azanVolume)
    }

    fun playPreviewChime(isFull: Boolean = false) {
        playPreviewTakbeerAlert()
    }

    fun toggleAutoPhoneTime(enabled: Boolean) {
        val context = getApplication<Application>().applicationContext
        context.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("pref_is_auto_phone_time", enabled)
            .apply()

        _uiState.update { it.copy(isAutoPhoneTime = enabled) }
        if (enabled) {
            setManualTimeOffsetMinutes(0)
            syncWithDeviceTimeNow()
        }
    }

    fun syncWithDeviceTimeNow() {
        val nowCal = Calendar.getInstance()
        val tz = TimeZone.getDefault()
        val offsetHours = tz.getOffset(nowCal.timeInMillis) / 3600000
        val sign = if (offsetHours >= 0) "+" else ""
        val tzFormatted = "GMT$sign$offsetHours (${tz.displayName})"
        val hijri = HijriCalendarHelper.getHijriDate(nowCal, _uiState.value.hijriDateAdjustmentDays)

        val context = getApplication<Application>().applicationContext
        context.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
            .edit()
            .putInt("pref_manual_time_offset_minutes", 0)
            .putBoolean("pref_is_auto_phone_time", true)
            .apply()

        _uiState.update {
            it.copy(
                selectedDate = nowCal,
                isCustomDateSelected = false,
                manualTimeOffsetMinutes = 0,
                isAutoPhoneTime = true,
                gregorianDateFormatted = displayDateFormat.format(nowCal.time),
                hijriDate = hijri,
                deviceTimeZoneName = tzFormatted,
                timeSyncSuccessMessage = "تمت مزامنة الوقت والتاريخ مع ساعة هاتفك بنجاح ⏱️"
            )
        }
        recalculatePrayerTimes()
        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(timeSyncSuccessMessage = null) }
        }
    }

    fun toggle24HourFormat(is24Hour: Boolean) {
        val context = getApplication<Application>().applicationContext
        context.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("pref_is_24_hour_format", is24Hour)
            .apply()

        _uiState.update { it.copy(is24HourFormat = is24Hour) }
        recalculatePrayerTimes()
    }

    fun setPrayerManualOffset(prayerType: PrayerType, offsetMinutes: Int) {
        val current = _uiState.value.prayerManualOffsets.toMutableMap()
        current[prayerType] = offsetMinutes.coerceIn(-30, 30)
        _uiState.update { it.copy(prayerManualOffsets = current) }
        recalculatePrayerTimes()
    }

    fun resetPrayerManualOffsets() {
        _uiState.update { it.copy(prayerManualOffsets = emptyMap()) }
        recalculatePrayerTimes()
    }

    fun clearTimeSyncMessage() {
        _uiState.update { it.copy(timeSyncSuccessMessage = null) }
    }

    fun setHijriDateAdjustment(days: Int) {
        val clamped = days.coerceIn(-3, 3)
        val context = getApplication<Application>().applicationContext
        context.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
            .edit()
            .putInt("pref_hijri_adjustment_days", clamped)
            .apply()

        val newHijri = HijriCalendarHelper.getHijriDate(_uiState.value.selectedDate, clamped)
        _uiState.update {
            it.copy(
                hijriDateAdjustmentDays = clamped,
                hijriDate = newHijri,
                dateSyncSuccessMessage = if (clamped == 0) "تم ضبط التاريخ الهجري على الحساب الفلكي التلقائي 🌙"
                else "تم تعديل التاريخ الهجري بمقدار ${if (clamped > 0) "+$clamped" else "$clamped"} يوم 🌙"
            )
        }
        recalculatePrayerTimes()
        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(dateSyncSuccessMessage = null) }
        }
    }

    fun setManualTimeOffsetMinutes(minutes: Int) {
        val clamped = minutes.coerceIn(-720, 720)
        val context = getApplication<Application>().applicationContext
        context.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
            .edit()
            .putInt("pref_manual_time_offset_minutes", clamped)
            .putBoolean("pref_is_auto_phone_time", clamped == 0)
            .apply()

        _uiState.update {
            it.copy(
                manualTimeOffsetMinutes = clamped,
                isAutoPhoneTime = (clamped == 0),
                timeSyncSuccessMessage = if (clamped == 0) "تمت إعادة ضبط الوقت ومزامنة ساعة الهاتف ⏱️"
                else "تم تعديل الوقت بمقدار ${if (clamped > 0) "+$clamped" else "$clamped"} دقيقة ⏱️"
            )
        }
        recalculatePrayerTimes()
        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(timeSyncSuccessMessage = null) }
        }
    }

    fun adjustManualTimeBy(deltaMinutes: Int) {
        val newOffset = _uiState.value.manualTimeOffsetMinutes + deltaMinutes
        setManualTimeOffsetMinutes(newOffset)
    }

    fun setSpecificCustomTime(hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMin = now.get(Calendar.MINUTE)
        val currentTotalMin = currentHour * 60 + currentMin
        val targetTotalMin = hourOfDay * 60 + minute
        var diff = targetTotalMin - currentTotalMin
        if (diff > 720) diff -= 1440
        if (diff < -720) diff += 1440
        setManualTimeOffsetMinutes(diff)
    }

    fun setSpecificCustomDate(year: Int, monthIndex: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        setSelectedDate(cal)
    }

    fun resetDateToToday() {
        val nowCal = Calendar.getInstance()
        val effectiveCal = (nowCal.clone() as Calendar).apply {
            if (_uiState.value.manualTimeOffsetMinutes != 0) {
                add(Calendar.MINUTE, _uiState.value.manualTimeOffsetMinutes)
            }
        }
        setSelectedDate(effectiveCal)
        _uiState.update {
            it.copy(
                isCustomDateSelected = false,
                dateSyncSuccessMessage = "تمت العودة إلى تاريخ اليوم 🔄"
            )
        }
        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(dateSyncSuccessMessage = null) }
        }
    }

    fun resetAllTimeAndDateSettings() {
        val context = getApplication<Application>().applicationContext
        context.getSharedPreferences("noor_time_date_prefs", Context.MODE_PRIVATE)
            .edit()
            .putInt("pref_hijri_adjustment_days", 0)
            .putInt("pref_manual_time_offset_minutes", 0)
            .putBoolean("pref_is_auto_phone_time", true)
            .apply()

        val nowCal = Calendar.getInstance()
        _uiState.update {
            it.copy(
                hijriDateAdjustmentDays = 0,
                manualTimeOffsetMinutes = 0,
                isAutoPhoneTime = true,
                selectedDate = nowCal,
                isCustomDateSelected = false,
                timeSyncSuccessMessage = "تمت استعادة إعدادات الوقت والتاريخ الافتراضية بنجاح ⏱️"
            )
        }
        syncWithDeviceTimeNow()
    }

    fun clearDateFeedback() {
        _uiState.update { it.copy(dateSyncSuccessMessage = null) }
    }

    fun setSelectedMuezzin(muezzin: MuezzinVoice) {
        _uiState.update { it.copy(selectedMuezzin = muezzin) }
        if (_uiState.value.isAzanAudioPlaying) {
            playMuezzinPreview(muezzin)
        }
        schedulePrayerNotifications()
    }

    fun setAzanVolume(volume: Float) {
        val clamped = volume.coerceIn(0.0f, 1.0f)
        _uiState.update { it.copy(azanVolume = clamped) }
        schedulePrayerNotifications()
    }

    fun setPrayerMuezzin(prayerType: PrayerType, muezzin: MuezzinVoice) {
        _uiState.update { state ->
            val currentMap = state.prayerAzanConfigs.toMutableMap()
            val existing = currentMap[prayerType] ?: PrayerAzanConfig(prayerType = prayerType)
            currentMap[prayerType] = existing.copy(muezzin = muezzin)
            state.copy(prayerAzanConfigs = currentMap)
        }
        if (_uiState.value.isAzanAudioPlaying && _uiState.value.playingPrayer == prayerType) {
            playPrayerAzanPreview(prayerType)
        }
        schedulePrayerNotifications()
    }

    fun setPrayerVolume(prayerType: PrayerType, volume: Float) {
        val clamped = volume.coerceIn(0.0f, 1.0f)
        _uiState.update { state ->
            val currentMap = state.prayerAzanConfigs.toMutableMap()
            val existing = currentMap[prayerType] ?: PrayerAzanConfig(prayerType = prayerType)
            currentMap[prayerType] = existing.copy(volume = clamped)
            state.copy(prayerAzanConfigs = currentMap)
        }
        schedulePrayerNotifications()
    }

    fun setPrayerAlertType(prayerType: PrayerType, alertType: AzanAlertType) {
        _uiState.update { state ->
            val currentMap = state.prayerAzanConfigs.toMutableMap()
            val existing = currentMap[prayerType] ?: PrayerAzanConfig(prayerType = prayerType)
            currentMap[prayerType] = existing.copy(
                alertType = alertType,
                isEnabled = alertType != AzanAlertType.SILENT
            )
            state.copy(prayerAzanConfigs = currentMap)
        }
        schedulePrayerNotifications()
    }

    fun setPrayerAzanEnabled(prayerType: PrayerType, enabled: Boolean) {
        _uiState.update { state ->
            val currentMap = state.prayerAzanConfigs.toMutableMap()
            val existing = currentMap[prayerType] ?: PrayerAzanConfig(prayerType = prayerType)
            currentMap[prayerType] = existing.copy(
                isEnabled = enabled,
                alertType = if (enabled) AzanAlertType.FULL_AZAN else AzanAlertType.SILENT
            )
            val prayerAzanMap = state.prayerAzanEnabled.toMutableMap()
            prayerAzanMap[prayerType] = enabled
            state.copy(
                prayerAzanConfigs = currentMap,
                prayerAzanEnabled = prayerAzanMap
            )
        }
        schedulePrayerNotifications()
    }

    fun togglePrayerAzanEnabled(prayerType: PrayerType) {
        val currentEnabled = _uiState.value.prayerAzanConfigs[prayerType]?.isEnabled
            ?: (_uiState.value.prayerAzanEnabled[prayerType] ?: true)
        setPrayerAzanEnabled(prayerType, !currentEnabled)
    }

    fun applyMuezzinToAllPrayers(muezzin: MuezzinVoice) {
        _uiState.update { state ->
            val newMap = state.prayerAzanConfigs.mapValues { (_, config) ->
                config.copy(muezzin = muezzin)
            }
            state.copy(
                selectedMuezzin = muezzin,
                prayerAzanConfigs = newMap
            )
        }
        schedulePrayerNotifications()
    }

    fun applyVolumeToAllPrayers(volume: Float) {
        val clamped = volume.coerceIn(0.0f, 1.0f)
        _uiState.update { state ->
            val newMap = state.prayerAzanConfigs.mapValues { (_, config) ->
                config.copy(volume = clamped)
            }
            state.copy(
                azanVolume = clamped,
                prayerAzanConfigs = newMap
            )
        }
        schedulePrayerNotifications()
    }

    fun playPrayerAzanPreview(prayerType: PrayerType) {
        val config = _uiState.value.prayerAzanConfigs[prayerType] ?: PrayerAzanConfig(prayerType = prayerType)
        val muezzinToPlay = when (config.alertType) {
            AzanAlertType.FULL_AZAN -> config.muezzin
            AzanAlertType.TAKBEER_ONLY -> MuezzinVoice.TAKBEERAT
            AzanAlertType.RECITER_VOICE -> MuezzinVoice.RECITER_AYAH
            AzanAlertType.VIBRATE_ONLY, AzanAlertType.SILENT -> config.muezzin
        }
        AzanSoundPlayer.playMuezzinPreview(
            muezzin = muezzinToPlay,
            volume = config.volume,
            prayerType = prayerType
        )
    }

    fun togglePrayerAzanPreview(prayerType: PrayerType) {
        if (_uiState.value.isAzanAudioPlaying && _uiState.value.playingPrayer == prayerType) {
            stopMuezzinPreview()
        } else {
            playPrayerAzanPreview(prayerType)
        }
    }

    /**
     * Toggles playing the Adhan early (before the prayer time).
     * If playing, it stops immediately; otherwise plays the configured Muezzin for that prayer.
     */
    fun toggleEarlyAdhanListening(prayerType: PrayerType? = null) {
        val targetPrayer = prayerType ?: _uiState.value.nextPrayer?.type ?: PrayerType.DHUHR
        if (_uiState.value.isAzanAudioPlaying && (_uiState.value.playingPrayer == targetPrayer || prayerType == null)) {
            stopMuezzinPreview()
        } else {
            val config = _uiState.value.prayerAzanConfigs[targetPrayer] ?: PrayerAzanConfig(prayerType = targetPrayer)
            val volume = if (config.volume > 0.1f) config.volume else _uiState.value.azanVolume.coerceAtLeast(0.75f)
            AzanSoundPlayer.playMuezzinPreview(
                muezzin = config.muezzin,
                volume = volume,
                prayerType = targetPrayer
            )
        }
    }

    fun playMuezzinPreview(muezzin: MuezzinVoice) {
        AzanSoundPlayer.playMuezzinPreview(muezzin, _uiState.value.azanVolume, prayerType = null)
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

    fun schedulePrayerNotifications() {
        try {
            val times = _uiState.value.todayPrayerTimes
            val configs = _uiState.value.prayerAzanConfigs
            val preMinutes = _uiState.value.prePrayerAlertMinutes
            PrayerNotificationScheduler.scheduleAllPrayerAlarms(
                context = getApplication(),
                prayerTimes = times,
                configs = configs,
                preAlertMinutes = preMinutes
            )
        } catch (_: Exception) {}
    }

    fun sendTestPrayerNotification(prayerType: PrayerType = PrayerType.DHUHR) {
        try {
            val config = _uiState.value.prayerAzanConfigs[prayerType] ?: PrayerAzanConfig(prayerType = prayerType)
            PrayerNotificationScheduler.sendInstantTestNotification(
                context = getApplication(),
                prayerType = prayerType,
                muezzin = config.muezzin,
                alertType = config.alertType,
                volume = config.volume
            )
        } catch (_: Exception) {}
    }

    fun setMorningAzkarNotification(enabled: Boolean, hour: Int, minute: Int) {
        val context = getApplication<Application>().applicationContext
        if (enabled) {
            AzkarNotificationScheduler.scheduleReminder(context, isMorning = true, hour, minute)
        } else {
            AzkarNotificationScheduler.cancelReminder(context, isMorning = true)
        }
        val amPm = if (hour < 12) "ص" else "م"
        val displayHour = if (hour % 12 == 0) 12 else hour % 12
        val formatted = String.format(Locale.US, "%02d:%02d %s", displayHour, minute, amPm)
        val msg = if (enabled) {
            "تم تفعيل تنبيه أذكار الصباح بنجاح عند الساعة $formatted ☀️"
        } else {
            "تم إيقاف تنبيه أذكار الصباح"
        }
        _uiState.update {
            it.copy(
                morningAzkarEnabled = enabled,
                morningAzkarHour = hour,
                morningAzkarMinute = minute,
                azkarFeedbackMessage = msg
            )
        }
    }

    fun setEveningAzkarNotification(enabled: Boolean, hour: Int, minute: Int) {
        val context = getApplication<Application>().applicationContext
        if (enabled) {
            AzkarNotificationScheduler.scheduleReminder(context, isMorning = false, hour, minute)
        } else {
            AzkarNotificationScheduler.cancelReminder(context, isMorning = false)
        }
        val amPm = if (hour < 12) "ص" else "م"
        val displayHour = if (hour % 12 == 0) 12 else hour % 12
        val formatted = String.format(Locale.US, "%02d:%02d %s", displayHour, minute, amPm)
        val msg = if (enabled) {
            "تم تفعيل تنبيه أذكار المساء بنجاح عند الساعة $formatted 🌙"
        } else {
            "تم إيقاف تنبيه أذكار المساء"
        }
        _uiState.update {
            it.copy(
                eveningAzkarEnabled = enabled,
                eveningAzkarHour = hour,
                eveningAzkarMinute = minute,
                azkarFeedbackMessage = msg
            )
        }
    }

    fun sendTestAzkarNotification(isMorning: Boolean) {
        val context = getApplication<Application>().applicationContext
        AzkarNotificationScheduler.sendInstantTestNotification(context, isMorning)
        val msg = if (isMorning) "تم إرسال إشعار أذكار الصباح التجريبي ☀️" else "تم إرسال إشعار أذكار المساء التجريبي 🌙"
        _uiState.update { it.copy(azkarFeedbackMessage = msg) }
    }

    fun recordAzkarCompleted(category: DhikrCategory) {
        val context = getApplication<Application>().applicationContext
        val newStreak = AzkarNotificationScheduler.recordAzkarSessionCompleted(context)
        _uiState.update {
            it.copy(
                azkarStreakDays = newStreak,
                azkarFeedbackMessage = "تقبل الله طاعتكم! سلسلة الاستمرارية الحالية: $newStreak أيام متتالية 🌟"
            )
        }
        viewModelScope.launch {
            if (category == DhikrCategory.MORNING) {
                repository.saveDailyChecklist(
                    _uiState.value.dailyChecklist.copy(morningAzkarDone = true)
                )
            } else if (category == DhikrCategory.EVENING) {
                repository.saveDailyChecklist(
                    _uiState.value.dailyChecklist.copy(eveningAzkarDone = true)
                )
            }
        }
    }

    fun clearAzkarFeedback() {
        _uiState.update { it.copy(azkarFeedbackMessage = null) }
    }

    fun setTargetOpenAzkarCategory(category: DhikrCategory?) {
        _uiState.update { it.copy(targetOpenAzkarCategory = category) }
    }
}
