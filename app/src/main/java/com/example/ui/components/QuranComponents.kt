package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranVerse
import com.example.data.model.Surah
import com.example.data.repository.QuranDataRepository
import com.example.ui.theme.*

@Composable
fun DailyQuranGoalCard(
    pagesRead: Int,
    targetPages: Int,
    lastSurahName: String,
    onAddPage: () -> Unit,
    onOpenLastRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (pagesRead.toFloat() / targetPages.coerceAtLeast(1)).coerceIn(0f, 1f)

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_quran_goal_card"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.85f),
        borderColor = SoftGold.copy(alpha = 0.35f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = SoftGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "وردك اليومي من القرآن",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }

                GlassTag(
                    text = "$pagesRead / $targetPages صفحات",
                    accentColor = if (pagesRead >= targetPages) EmeraldPrimary else SoftGold,
                    textColor = if (pagesRead >= targetPages) EmeraldLight else SoftGoldBright
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (pagesRead >= targetPages) EmeraldLight else SoftGold,
                trackColor = MidnightNavySurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable(onClick = onOpenLastRead),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = SoftGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "آخر موضع قراءة: سورة $lastSurahName",
                        style = MaterialTheme.typography.bodySmall,
                        color = IvoryMuted
                    )
                }

                Button(
                    onClick = onAddPage,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+ إتمام صفحة",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldLight
                    )
                }
            }
        }
    }
}

@Composable
fun SurahItemCard(
    surah: Surah,
    isLastRead: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("surah_item_${surah.number}"),
        backgroundColor = if (isLastRead) MidnightNavySurface else MidnightNavyCard.copy(alpha = 0.7f),
        borderColor = if (isLastRead) SoftGold else GlassBorder.copy(alpha = 0.15f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Surah Number Diamond Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isLastRead) SoftGoldDark else MidnightNavySurface)
                        .border(1.dp, if (isLastRead) SoftGoldBright else GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isLastRead) SoftGoldBright else IvoryWhite
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "سورة ${surah.arabicName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        GlassTag(
                            text = surah.revelationType,
                            accentColor = if (surah.revelationType == "مكية") SoftGold else EmeraldPrimary,
                            textColor = IvoryWhite
                        )
                    }
                    Text(
                        text = "${surah.englishName} • ${surah.totalVerses} آية",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = SoftGold,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SurahReaderDialog(
    surah: Surah,
    onDismiss: () -> Unit,
    onBookmarkAyah: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlayingAudio by remember { mutableStateOf(false) }
    var currentPlayingAyah by remember { mutableStateOf(1) }
    var selectedTafsirVerse by remember { mutableStateOf<QuranVerse?>(null) }
    var showShareDialogVerse by remember { mutableStateOf<QuranVerse?>(null) }
    var selectedReader by remember { mutableStateOf(QuranDataRepository.readersList[0]) }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        modifier = modifier.fillMaxHeight(0.92f),
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "سورة ${surah.arabicName}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SoftGoldBright
                        )
                        Text(
                            text = "${surah.revelationType} • ${surah.totalVerses} آية",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Audio Player Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavySurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { isPlayingAudio = !isPlayingAudio },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlayingAudio) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                contentDescription = "استماع",
                                tint = EmeraldLight,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isPlayingAudio) "جاري التلاوة • آية $currentPlayingAyah" else "استماع للسورة",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = IvoryWhite
                            )
                            Text(
                                text = "القارئ: $selectedReader",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = SoftGold
                            )
                        }
                    }
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Bismillah Header (except for Surah At-Tawbah)
                if (surah.number != 9) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = SoftGold
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val versesToShow = if (surah.verses.isNotEmpty()) surah.verses else listOf(
                        QuranVerse(1, "الرَّحْمَٰنُ عَلَّمَ الْقُرْآنَ خَلَقَ الْإِنسَانَ عَلَّمَهُ الْبَيَانَ", "The Entirely Merciful, Taught the Quran, Created man, Taught him eloquence.", "بيان فضل القرآن ونعمة البيان."),
                        QuranVerse(2, "الشَّمْسُ وَالْقَمَرُ بِحُسْبَانٍ وَالنَّجْمُ وَالشَّجَرُ يَسْجُدَانِ", "The sun and the moon [move] by precise calculation, And the stars and trees prostrate.", "تسبيح ودقة خلق السماوات والأرض.")
                    )

                    items(versesToShow) { verse ->
                        AyahRowCard(
                            verse = verse,
                            isPlaying = isPlayingAudio && currentPlayingAyah == verse.ayahNumber,
                            onBookmark = { onBookmarkAyah(verse.ayahNumber, verse.textArabic) },
                            onShowTafsir = { selectedTafsirVerse = verse },
                            onShare = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "﴿ ${verse.textArabic} ﴾ [سورة ${surah.arabicName}: ${verse.ayahNumber}] — تطبيق «نُور»")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "مشاركة الآية"))
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "تمت القراءة", color = EmeraldLight)
            }
        }
    )

    // Tafsir Dialog
    if (selectedTafsirVerse != null) {
        val v = selectedTafsirVerse!!
        AlertDialog(
            onDismissRequest = { selectedTafsirVerse = null },
            containerColor = MidnightNavyCard,
            title = {
                Text(
                    text = "تفسير الآية (${v.ayahNumber})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "﴿ ${v.textArabic} ﴾",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = IvoryWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (v.tafsirSummary.isNotEmpty()) v.tafsirSummary else "تفسير ميسر يبين مقاصد الآية ومعاني مفرداتها القرآنية.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = IvoryMuted
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedTafsirVerse = null }) {
                    Text("إغلاق", color = SoftGold)
                }
            }
        )
    }
}

@Composable
fun AyahRowCard(
    verse: QuranVerse,
    isPlaying: Boolean,
    onBookmark: () -> Unit,
    onShowTafsir: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isPlaying) EmeraldDark.copy(alpha = 0.35f) else MidnightNavySurface.copy(alpha = 0.5f))
            .border(1.dp, if (isPlaying) EmeraldLight else GlassBorder.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // Arabic text in prominent typography
        Text(
            text = "${verse.textArabic} ﴿${verse.ayahNumber}﴾",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 19.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Right
            ),
            color = IvoryWhite,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // English translation
        Text(
            text = verse.translation,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Action Toolbar (Bookmark, Tafsir, Share)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onShowTafsir,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = SoftGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "التفسير", style = MaterialTheme.typography.labelSmall, color = SoftGold)
            }

            IconButton(onClick = onBookmark, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.BookmarkAdd, contentDescription = "حفظ علامة", tint = SoftGold, modifier = Modifier.size(18.dp))
            }

            IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = IvoryMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}
