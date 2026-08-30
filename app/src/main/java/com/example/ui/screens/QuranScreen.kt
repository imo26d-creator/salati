package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
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
import com.example.data.model.Surah
import com.example.data.repository.QuranDataRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState

@Composable
fun QuranScreen(
    uiState: NoorUiState,
    onAddPage: () -> Unit,
    onBookmarkAyah: (Int, Int, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("الكل") }
    var selectedSurahForReading by remember { mutableStateOf<Surah?>(null) }
    var showBookmarksDialog by remember { mutableStateOf(false) }

    val filteredSurahs = QuranDataRepository.surahsList.filter { surah ->
        val matchesSearch = searchQuery.isEmpty() ||
                surah.arabicName.contains(searchQuery) ||
                surah.englishName.contains(searchQuery, ignoreCase = true) ||
                "${surah.number}".contains(searchQuery)

        val matchesFilter = when (selectedFilter) {
            "مكية" -> surah.revelationType == "مكية"
            "مدنية" -> surah.revelationType == "مدنية"
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
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
                            text = "القرآن الكريم 📖",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        Text(
                            text = "تلاوة وتدبر مع التفسير الصوتي والمقروء",
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

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("ابحث عن سورة بالاسم أو الرقم...", color = TextMuted) },
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

            // Filters (الكل، مكية، مدنية)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("الكل", "مكية", "مدنية").forEach { filter ->
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

            // Surahs List
            items(filteredSurahs) { surah ->
                SurahItemCard(
                    surah = surah,
                    isLastRead = surah.arabicName == uiState.lastReadSurahName,
                    onClick = { selectedSurahForReading = surah }
                )
            }
        }
    }

    // Surah Reader Dialog
    if (selectedSurahForReading != null) {
        val s = selectedSurahForReading!!
        SurahReaderDialog(
            surah = s,
            onDismiss = { selectedSurahForReading = null },
            onBookmarkAyah = { ayahNum, ayahText ->
                onBookmarkAyah(s.number, ayahNum, s.arabicName, ayahText)
            }
        )
    }

    // Bookmarks list dialog
    if (showBookmarksDialog) {
        AlertDialog(
            onDismissRequest = { showBookmarksDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(text = "العلامات المرجعية المحفوظة 🔖", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
            },
            text = {
                if (uiState.bookmarks.isEmpty()) {
                    Text(text = "لا توجد علامات مرجعية محفوظة بعد. يمكنك حفظ أي آية أثناء القراءة للعودة إليها سريعًا.", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.bookmarks) { b ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MidnightNavySurface)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "سورة ${b.surahName} • آية ${b.ayahNumber}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = SoftGold)
                                    Text(text = b.ayahText, style = MaterialTheme.typography.bodySmall, color = IvoryWhite, maxLines = 1)
                                }
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
