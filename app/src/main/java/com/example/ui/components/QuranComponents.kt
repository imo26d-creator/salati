package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranReciter
import com.example.data.model.QuranRepeatMode
import com.example.data.model.QuranVerse
import com.example.data.model.Surah
import com.example.data.repository.QuranDataRepository
import com.example.ui.theme.*
import com.example.util.QuranAudioPlayer

enum class QuranDisplayMode(val titleArabic: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    READ("قراءة المصحف", Icons.Default.MenuBook),
    LISTEN("الاستماع للتلاوة", Icons.Default.Headphones)
}

@Composable
fun QuranModeSwitcher(
    selectedMode: QuranDisplayMode,
    onModeSelected: (QuranDisplayMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MidnightNavySurface)
            .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            QuranDisplayMode.values().forEach { mode ->
                val isSelected = selectedMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .then(
                            if (isSelected) {
                                Modifier.background(
                                    Brush.horizontalGradient(listOf(EmeraldDark, EmeraldPrimary))
                                )
                            } else {
                                Modifier.background(Color.Transparent)
                            }
                        )
                        .clickable { onModeSelected(mode) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = mode.icon,
                            contentDescription = null,
                            tint = if (isSelected) IvoryWhite else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = mode.titleArabic,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) IvoryWhite else TextMuted
                        )
                    }
                }
            }
        }
    }
}

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
    onListenClick: () -> Unit,
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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Surah Number Diamond Badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isLastRead) SoftGoldDark else MidnightNavySurface)
                        .border(1.dp, if (isLastRead) SoftGoldBright else GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
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
                        text = "جزء ${surah.juzNumber} • ص ${surah.pageStart} • ${surah.totalVerses} آية",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onListenClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = "استماع للسورة",
                        tint = EmeraldLight,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = null,
                    tint = SoftGold,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun CurrentReciterHeaderCard(
    currentReciter: QuranReciter,
    onChangeReciter: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("current_reciter_header_card"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.9f),
        borderColor = SoftGold.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(SoftGoldDark, MidnightNavySurface)
                            )
                        )
                        .border(1.5.dp, SoftGoldBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentReciter.emojiAvatar,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Column {
                    Text(
                        text = "القارئ المختار",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftGold
                    )
                    Text(
                        text = currentReciter.nameArabic,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "رواية ${currentReciter.rewayaArabic}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            Button(
                onClick = onChangeReciter,
                colors = ButtonDefaults.buttonColors(containerColor = SoftGoldDark),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = SoftGoldBright,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "تغيير",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
            }
        }
    }
}

@Composable
fun SurahAudioListItemCard(
    surah: Surah,
    isPlaying: Boolean,
    isCurrentSurah: Boolean,
    isBuffering: Boolean,
    onPlayClick: () -> Unit,
    onReadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("surah_audio_item_${surah.number}"),
        backgroundColor = if (isCurrentSurah) EmeraldDark.copy(alpha = 0.35f) else MidnightNavyCard.copy(alpha = 0.7f),
        borderColor = if (isCurrentSurah) EmeraldLight else GlassBorder.copy(alpha = 0.15f),
        onClick = onPlayClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Play / Pause Circle Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isCurrentSurah) EmeraldLight else MidnightNavySurface)
                        .clickable(onClick = onPlayClick),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCurrentSurah && isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MidnightNavyDark,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isCurrentSurah && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isCurrentSurah && isPlaying) "إيقاف مؤقت" else "تشغيل",
                            tint = if (isCurrentSurah) MidnightNavyDark else EmeraldLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "سورة ${surah.arabicName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isCurrentSurah) EmeraldLight else IvoryWhite
                        )
                        GlassTag(
                            text = "${surah.number}",
                            accentColor = if (isCurrentSurah) EmeraldPrimary else SoftGoldDark,
                            textColor = IvoryWhite
                        )
                    }
                    Text(
                        text = "${surah.englishName} • ${surah.revelationType} • ${surah.totalVerses} آية",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            // Quick Read Button
            OutlinedButton(
                onClick = onReadClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGold),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(SoftGold.copy(alpha = 0.5f), SoftGoldBright.copy(alpha = 0.5f)))
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = SoftGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "قراءة",
                    style = MaterialTheme.typography.labelSmall,
                    color = SoftGoldBright
                )
            }
        }
    }
}

@Composable
fun QuranMiniPlayerBar(
    surah: Surah,
    reciter: QuranReciter,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosMs: Int,
    durationMs: Int,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (durationMs > 0) (currentPosMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = EmeraldPrimary)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        MidnightNavyCard.copy(alpha = 0.98f),
                        MidnightNavyDark.copy(alpha = 0.98f)
                    )
                )
            )
            .border(1.dp, EmeraldLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Live Mini Progress Line
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = EmeraldLight,
                trackColor = MidnightNavySurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldDark.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = reciter.emojiAvatar, fontSize = 18.sp)
                    }

                    Column {
                        Text(
                            text = "سورة ${surah.arabicName}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${reciter.nameArabic} • ${QuranAudioPlayer.formatTime(currentPosMs)} / ${QuranAudioPlayer.formatTime(durationMs)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = SoftGold,
                            maxLines = 1
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(onClick = onPrev, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "السورة السابقة", tint = IvoryMuted, modifier = Modifier.size(20.dp))
                    }

                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldLight)
                    ) {
                        if (isBuffering) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = MidnightNavyDark, strokeWidth = 2.dp)
                        } else {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "إيقاف" else "تشغيل",
                                tint = MidnightNavyDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    IconButton(onClick = onNext, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.SkipNext, contentDescription = "السورة التالية", tint = IvoryMuted, modifier = Modifier.size(20.dp))
                    }

                    IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق المشغل", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FullAudioPlayerDialog(
    surah: Surah,
    reciter: QuranReciter,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosMs: Int,
    durationMs: Int,
    repeatMode: QuranRepeatMode,
    playbackSpeed: Float,
    sleepTimerMins: Int,
    sleepRemainingSecs: Int,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSeek: (Int) -> Unit,
    onToggleRepeat: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onSetSleepTimer: (Int) -> Unit,
    onSelectReciter: () -> Unit,
    onDismiss: () -> Unit
) {
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showTimerDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "player_wave")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disk_rotation"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        modifier = Modifier.fillMaxWidth(0.95f),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "مشغل التلاوة القرآنية 🎧",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                    Text(
                        text = "جودة عالية برواية ${reciter.rewayaArabic}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Glowing Disc / Reciter Badge
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(EmeraldDark, MidnightNavySurface)
                            )
                        )
                        .border(2.5.dp, if (isPlaying) EmeraldLight else SoftGold, CircleShape)
                        .rotate(if (isPlaying) rotationAngle else 0f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = reciter.emojiAvatar, fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "سورة ${surah.arabicName}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "سورة ${surah.arabicName}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )

                Text(
                    text = "بصوت القارئ: ${reciter.nameArabic}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = SoftGold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${surah.revelationType} • ${surah.totalVerses} آية • جزء ${surah.juzNumber} ص ${surah.pageStart}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Time Slider
                val progress = if (durationMs > 0) (currentPosMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f
                Slider(
                    value = progress,
                    onValueChange = { frac ->
                        onSeek((frac * durationMs).toInt())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = EmeraldLight,
                        activeTrackColor = EmeraldPrimary,
                        inactiveTrackColor = MidnightNavySurface
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = QuranAudioPlayer.formatTime(currentPosMs), style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text(text = QuranAudioPlayer.formatTime(durationMs), style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Playback Control Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Repeat Mode Button
                    IconButton(onClick = onToggleRepeat) {
                        Icon(
                            imageVector = when (repeatMode) {
                                QuranRepeatMode.ONE -> Icons.Default.RepeatOne
                                QuranRepeatMode.ALL -> Icons.Default.Repeat
                                QuranRepeatMode.OFF -> Icons.Default.Shuffle
                            },
                            contentDescription = "وضع التكرار",
                            tint = if (repeatMode != QuranRepeatMode.OFF) EmeraldLight else TextMuted
                        )
                    }

                    // Prev Surah
                    IconButton(onClick = onPrev, modifier = Modifier.size(48.dp)) {
                        Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "السورة السابقة", tint = IvoryWhite, modifier = Modifier.size(30.dp))
                    }

                    // Main Play/Pause Button
                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(EmeraldLight, EmeraldPrimary)
                                )
                            )
                    ) {
                        if (isBuffering) {
                            CircularProgressIndicator(modifier = Modifier.size(28.dp), color = MidnightNavyDark, strokeWidth = 3.dp)
                        } else {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                tint = MidnightNavyDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    // Next Surah
                    IconButton(onClick = onNext, modifier = Modifier.size(48.dp)) {
                        Icon(imageVector = Icons.Default.SkipNext, contentDescription = "السورة التالية", tint = IvoryWhite, modifier = Modifier.size(30.dp))
                    }

                    // Speed Button
                    TextButton(onClick = { showSpeedDialog = true }) {
                        Text(text = "${playbackSpeed}x", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Tools Row (Sleep Timer & Switch Reciter)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { showTimerDialog = true }) {
                        Icon(imageVector = Icons.Default.Bedtime, contentDescription = null, tint = SoftGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (sleepRemainingSecs > 0) "مؤقت النوم: ${sleepRemainingSecs / 60} د" else "مؤقت النوم",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoftGold
                        )
                    }

                    TextButton(onClick = onSelectReciter) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "اختيار قارئ آخر", style = MaterialTheme.typography.labelSmall, color = EmeraldLight)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "تصغير المشغل", color = SoftGold)
            }
        }
    )

    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            containerColor = MidnightNavyCard,
            title = { Text(text = "سرعة التلاوة", style = MaterialTheme.typography.titleMedium, color = SoftGoldBright) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { spd ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (playbackSpeed == spd) EmeraldDark.copy(alpha = 0.4f) else MidnightNavySurface)
                                .clickable {
                                    onSpeedChange(spd)
                                    showSpeedDialog = false
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${spd}x سرعة", color = IvoryWhite)
                            if (playbackSpeed == spd) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldLight)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) { Text("إلغاء", color = SoftGold) }
            }
        )
    }

    if (showTimerDialog) {
        AlertDialog(
            onDismissRequest = { showTimerDialog = false },
            containerColor = MidnightNavyCard,
            title = { Text(text = "مؤقت إيقاف التلاوة (النوم)", style = MaterialTheme.typography.titleMedium, color = SoftGoldBright) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        0 to "إيقاف المؤقت",
                        15 to "بعد 15 دقيقة",
                        30 to "بعد 30 دقيقة",
                        45 to "بعد 45 دقيقة",
                        60 to "بعد ساعة كاملة (60 دقيقة)"
                    ).forEach { (mins, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (sleepTimerMins == mins) EmeraldDark.copy(alpha = 0.4f) else MidnightNavySurface)
                                .clickable {
                                    onSetSleepTimer(mins)
                                    showTimerDialog = false
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = label, color = IvoryWhite)
                            if (sleepTimerMins == mins) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldLight)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTimerDialog = false }) { Text("إغلاق", color = SoftGold) }
            }
        )
    }
}

@Composable
fun RecitersSelectionDialog(
    reciters: List<QuranReciter>,
    selectedReciter: QuranReciter,
    onSelectReciter: (QuranReciter) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = reciters.filter {
        searchQuery.isEmpty() ||
                it.nameArabic.contains(searchQuery) ||
                it.nameEnglish.contains(searchQuery, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        modifier = Modifier.fillMaxHeight(0.85f),
        title = {
            Column {
                Text(
                    text = "اختيار المقرئ 🎙️",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
                Text(
                    text = "كبار قراء العالم الإسلامي برواية حفص عن عاصم",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("ابحث عن قارئ...", color = TextMuted) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SoftGold) },
                    shape = RoundedCornerShape(14.dp),
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
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { reciter ->
                    val isSelected = selectedReciter.id == reciter.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) EmeraldDark.copy(alpha = 0.4f) else MidnightNavySurface)
                            .border(1.dp, if (isSelected) EmeraldLight else Color.Transparent, RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectReciter(reciter)
                                onDismiss()
                            }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = reciter.emojiAvatar, fontSize = 24.sp)
                            Column {
                                Text(
                                    text = reciter.nameArabic,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) EmeraldLight else IvoryWhite
                                )
                                Text(
                                    text = reciter.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    color = TextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldLight)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = SoftGold)
            }
        }
    )
}

@Composable
fun SurahReaderDialog(
    surah: Surah,
    fontSize: Int,
    onFontSizeChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onBookmarkAyah: (Int, String) -> Unit,
    onListenToSurah: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTafsirVerse by remember { mutableStateOf<QuranVerse?>(null) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        modifier = modifier.fillMaxHeight(0.95f),
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
                            text = "${surah.revelationType} • ${surah.totalVerses} آية • جزء ${surah.juzNumber} • ص ${surah.pageStart}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Toolbar: Listen button + Font Size control
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavySurface)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onListenToSurah,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Headphones, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "استماع للسورة", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = EmeraldLight)
                    }

                    // Font Size stepper
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "حجم الخط:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        IconButton(
                            onClick = { onFontSizeChange(fontSize - 2) },
                            modifier = Modifier.size(28.dp),
                            enabled = fontSize > 16
                        ) {
                            Text(text = "A-", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = SoftGold)
                        }
                        Text(text = "$fontSize", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = IvoryWhite)
                        IconButton(
                            onClick = { onFontSizeChange(fontSize + 2) },
                            modifier = Modifier.size(28.dp),
                            enabled = fontSize < 36
                        ) {
                            Text(text = "A+", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = SoftGold)
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
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = SoftGold
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val versesToShow = if (surah.verses.isNotEmpty()) surah.verses else listOf(
                        QuranVerse(1, "الرَّحْمَٰنُ عَلَّمَ الْقُرْآنَ خَلَقَ الْإِنسَانَ عَلَّمَهُ الْبَيَانَ", "The Entirely Merciful, Taught the Quran, Created man, Taught him eloquence.", "بيان فضل القرآن ونعمة البيان."),
                        QuranVerse(2, "الشَّمْسُ وَالْقَمَرُ بِحُسْبَانٍ وَالنَّجْمُ وَالشَّجَرُ يَسْجُدَانِ", "The sun and the moon [move] by precise calculation, And the stars and trees prostrate.", "تسبيح ودقة خلق السماوات والأرض.")
                    )

                    items(versesToShow) { verse ->
                        AyahRowCard(
                            verse = verse,
                            fontSizeSp = fontSize,
                            isPlaying = false,
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
                Text(text = "إغلاق المصحف", color = EmeraldLight)
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
    fontSizeSp: Int = 20,
    isPlaying: Boolean = false,
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
        // Arabic text in prominent typography with adjustable size
        Text(
            text = "${verse.textArabic} ﴿${verse.ayahNumber}﴾",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = fontSizeSp.sp,
                lineHeight = (fontSizeSp + 13).sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Right
            ),
            color = IvoryWhite,
            modifier = Modifier.fillMaxWidth()
        )

        if (verse.translation.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = verse.translation,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
                color = TextMuted
            )
        }

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
