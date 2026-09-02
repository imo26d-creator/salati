package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranReciter
import com.example.data.model.Surah
import com.example.data.repository.QuranDataRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState
import com.example.util.QuranAudioPlayer

@Composable
fun QuranScreen(
    uiState: NoorUiState,
    onAddPage: () -> Unit,
    onBookmarkAyah: (Int, Int, String, String) -> Unit,
    onSelectReciter: (QuranReciter) -> Unit,
    onFontSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayMode by remember { mutableStateOf(QuranDisplayMode.READ) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("الكل") }
    var selectedSurahForReading by remember { mutableStateOf<Surah?>(null) }
    var showBookmarksDialog by remember { mutableStateOf(false) }
    var showRecitersDialog by remember { mutableStateOf(false) }
    var showFullAudioPlayer by remember { mutableStateOf(false) }

    // Audio player reactive states
    val isPlaying by QuranAudioPlayer.isPlaying.collectAsState()
    val isBuffering by QuranAudioPlayer.isBuffering.collectAsState()
    val currentPlayingSurah by QuranAudioPlayer.currentSurah.collectAsState()
    val currentReciter by QuranAudioPlayer.currentReciter.collectAsState()
    val currentPositionMs by QuranAudioPlayer.currentPositionMs.collectAsState()
    val durationMs by QuranAudioPlayer.durationMs.collectAsState()
    val repeatMode by QuranAudioPlayer.repeatMode.collectAsState()
    val playbackSpeed by QuranAudioPlayer.playbackSpeed.collectAsState()
    val sleepTimerMinutes by QuranAudioPlayer.sleepTimerMinutes.collectAsState()
    val sleepRemainingSeconds by QuranAudioPlayer.sleepTimerRemainingSeconds.collectAsState()

    val filteredSurahs = QuranDataRepository.surahsList.filter { surah ->
        val matchesSearch = searchQuery.isEmpty() ||
                surah.arabicName.contains(searchQuery) ||
                surah.englishName.contains(searchQuery, ignoreCase = true) ||
                "${surah.number}".contains(searchQuery) ||
                "جزء ${surah.juzNumber}".contains(searchQuery)

        val matchesFilter = when (selectedFilter) {
            "مكية" -> surah.revelationType == "مكية"
            "مدنية" -> surah.revelationType == "مدنية"
            "جزء عم (30)" -> surah.juzNumber == 30
            "جزء تبارك (29)" -> surah.juzNumber == 29
            "جزء قد سمع (28)" -> surah.juzNumber == 28
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("quran_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = if (currentPlayingSurah != null) 100.dp else 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "القرآن الكريم كاملاً 📖",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = IvoryWhite
                            )
                            Text(
                                text = "تلاوة وتدبر لجميع سور المصحف الشريف (114 سورة)",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextMuted
                            )
                        }

                        GlassIconButton(
                            icon = Icons.Default.Bookmark,
                            contentDescription = "العلامات المرجعية",
                            onClick = { showBookmarksDialog = true },
                            tint = SoftGold
                        )
                    }
                }

                // Mode Switcher (قراءة المصحف / الاستماع للتلاوة)
                item {
                    QuranModeSwitcher(
                        selectedMode = displayMode,
                        onModeSelected = { displayMode = it }
                    )
                }

                // Content for READ Mode
                if (displayMode == QuranDisplayMode.READ) {
                    // Daily Quran Goal Card
                    item {
                        DailyQuranGoalCard(
                            pagesRead = uiState.quranPagesRead,
                            targetPages = uiState.quranTargetPages,
                            lastSurahName = uiState.lastReadSurahName,
                            onAddPage = onAddPage,
                            onOpenLastRead = {
                                val s = QuranDataRepository.surahsList.find { it.arabicName == uiState.lastReadSurahName }
                                    ?: QuranDataRepository.surahsList.first()
                                selectedSurahForReading = s
                            }
                        )
                    }
                } else {
                    // Content for LISTEN Mode
                    // Reciter Selection Header Card
                    item {
                        CurrentReciterHeaderCard(
                            currentReciter = currentReciter,
                            onChangeReciter = { showRecitersDialog = true }
                        )
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = if (displayMode == QuranDisplayMode.READ) "ابحث عن سورة بالاسم، الرقم، أو الجزء..." else "ابحث عن تلاوة سورة...",
                                color = TextMuted
                            )
                        },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SoftGold) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftGold,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = IvoryWhite,
                            unfocusedTextColor = IvoryWhite,
                            focusedContainerColor = MidnightNavySurface,
                            unfocusedContainerColor = MidnightNavySurface
                        )
                    )
                }

                // Filters Row
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("الكل", "مكية", "مدنية", "جزء عم (30)", "جزء تبارك (29)", "جزء قد سمع (28)")
                        items(filters) { filter ->
                            val isSel = selectedFilter == filter
                            StatusChip(
                                label = filter,
                                isSelected = isSel,
                                selectedColor = if (isSel) EmeraldPrimary else TextMuted,
                                onClick = { selectedFilter = filter }
                            )
                        }
                    }
                }

                // Surahs List: depending on mode
                if (displayMode == QuranDisplayMode.READ) {
                    items(filteredSurahs) { surah ->
                        SurahItemCard(
                            surah = surah,
                            isLastRead = surah.arabicName == uiState.lastReadSurahName,
                            onClick = { selectedSurahForReading = surah },
                            onListenClick = {
                                QuranAudioPlayer.playSurah(surah, currentReciter)
                            }
                        )
                    }
                } else {
                    items(filteredSurahs) { surah ->
                        val isCurrent = currentPlayingSurah?.number == surah.number
                        SurahAudioListItemCard(
                            surah = surah,
                            isPlaying = isPlaying && isCurrent,
                            isCurrentSurah = isCurrent,
                            isBuffering = isBuffering && isCurrent,
                            onPlayClick = {
                                if (isCurrent) {
                                    QuranAudioPlayer.togglePlayPause()
                                } else {
                                    QuranAudioPlayer.playSurah(surah, currentReciter)
                                }
                            },
                            onReadClick = {
                                selectedSurahForReading = surah
                            }
                        )
                    }
                }
            }

            // Persistent Floating Mini-Player at bottom if active
            AnimatedVisibility(
                visible = currentPlayingSurah != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                currentPlayingSurah?.let { surah ->
                    QuranMiniPlayerBar(
                        surah = surah,
                        reciter = currentReciter,
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                        currentPosMs = currentPositionMs,
                        durationMs = durationMs,
                        onTogglePlay = { QuranAudioPlayer.togglePlayPause() },
                        onNext = { QuranAudioPlayer.playNextSurah() },
                        onPrev = { QuranAudioPlayer.playPreviousSurah() },
                        onClick = { showFullAudioPlayer = true },
                        onClose = { QuranAudioPlayer.stop() }
                    )
                }
            }
        }
    }

    // Surah Reader Dialog (Mushaf text & Tafsir)
    if (selectedSurahForReading != null) {
        val s = selectedSurahForReading!!
        SurahReaderDialog(
            surah = s,
            fontSize = uiState.quranReaderFontSize,
            onFontSizeChange = onFontSizeChange,
            onDismiss = { selectedSurahForReading = null },
            onBookmarkAyah = { ayahNum, ayahText ->
                onBookmarkAyah(s.number, ayahNum, s.arabicName, ayahText)
            },
            onListenToSurah = {
                QuranAudioPlayer.playSurah(s, currentReciter)
            }
        )
    }

    // Full Screen Audio Player Dialog
    if (showFullAudioPlayer && currentPlayingSurah != null) {
        FullAudioPlayerDialog(
            surah = currentPlayingSurah!!,
            reciter = currentReciter,
            isPlaying = isPlaying,
            isBuffering = isBuffering,
            currentPosMs = currentPositionMs,
            durationMs = durationMs,
            repeatMode = repeatMode,
            playbackSpeed = playbackSpeed,
            sleepTimerMins = sleepTimerMinutes,
            sleepRemainingSecs = sleepRemainingSeconds,
            onTogglePlay = { QuranAudioPlayer.togglePlayPause() },
            onNext = { QuranAudioPlayer.playNextSurah() },
            onPrev = { QuranAudioPlayer.playPreviousSurah() },
            onSeek = { QuranAudioPlayer.seekTo(it) },
            onToggleRepeat = { QuranAudioPlayer.toggleRepeatMode() },
            onSpeedChange = { QuranAudioPlayer.setPlaybackSpeed(it) },
            onSetSleepTimer = { QuranAudioPlayer.setSleepTimer(it) },
            onSelectReciter = { showRecitersDialog = true },
            onDismiss = { showFullAudioPlayer = false }
        )
    }

    // Reciters Selection Bottom Sheet / Dialog
    if (showRecitersDialog) {
        RecitersSelectionDialog(
            reciters = QuranDataRepository.recitersList,
            selectedReciter = currentReciter,
            onSelectReciter = {
                onSelectReciter(it)
                QuranAudioPlayer.setReciter(it)
            },
            onDismiss = { showRecitersDialog = false }
        )
    }

    // Bookmarks list dialog
    if (showBookmarksDialog) {
        AlertDialog(
            onDismissRequest = { showBookmarksDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(
                    text = "العلامات المرجعية المحفوظة 🔖",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
            },
            text = {
                if (uiState.bookmarks.isEmpty()) {
                    Text(
                        text = "لا توجد علامات مرجعية محفوظة بعد. يمكنك حفظ أي آية أثناء القراءة للعودة إليها سريعًا.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.bookmarks) { b ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MidnightNavySurface)
                                    .clickable {
                                        val surah = QuranDataRepository.getSurahByNumber(b.surahNumber)
                                        if (surah != null) {
                                            selectedSurahForReading = surah
                                            showBookmarksDialog = false
                                        }
                                    }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "سورة ${b.surahName} • آية ${b.ayahNumber}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = SoftGold
                                    )
                                    Text(
                                        text = b.ayahText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = IvoryWhite,
                                        maxLines = 1
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "فتح السورة",
                                    tint = EmeraldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBookmarksDialog = false }) {
                    Text(text = "إغلاق", color = SoftGold)
                }
            }
        )
    }
}
