package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerStatus
import com.example.data.model.PrayerTimeInfo
import com.example.data.model.PrayerType
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState

@Composable
fun PrayerCountdownHero(
    uiState: NoorUiState,
    onPrepareClick: () -> Unit,
    onToggleListenAdhanEarly: (PrayerType) -> Unit = {},
    onOpenPreAlertSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val next = uiState.nextPrayer ?: return

    // Pulse animation for the ready button and glow
    val infiniteTransition = rememberInfiniteTransition(label = "heroPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_countdown_hero"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.85f),
        borderColor = SoftGold.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: City and Hijri date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SoftGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = uiState.cityName,
                        style = MaterialTheme.typography.bodySmall,
                        color = IvoryMuted
                    )
                }

                GlassTag(
                    text = uiState.hijriDate.formatted,
                    accentColor = SoftGold,
                    textColor = SoftGoldBright
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Big Central Countdown & Prayer Name
            Box(
                modifier = Modifier
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circular Progress Indicator
                CircularProgressIndicator(
                    progress = { uiState.countdownProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = SoftGold,
                    trackColor = MidnightNavyLight.copy(alpha = 0.5f),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "الصلاة القادمة",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = next.type.arabicName,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = IvoryWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = next.timeFormatted,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        ),
                        color = SoftGoldBright
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = uiState.countdownText,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        ),
                        color = EmeraldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            val isPlayingThis = uiState.isAzanAudioPlaying && (uiState.playingPrayer == next.type || uiState.playingPrayer == null)
            val prayerConfig = uiState.prayerAzanConfigs[next.type]
            val assignedMuezzin = prayerConfig?.muezzin ?: uiState.selectedMuezzin

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. زر "استعد للصلاة 🤍" بالأعلى
                Button(
                    onClick = onPrepareClick,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(48.dp)
                        .scale(pulseScale)
                        .testTag("prepare_prayer_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldDark
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.2.dp, EmeraldLight.copy(alpha = 0.8f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = EmeraldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "استعد للصلاة 🤍",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                    }
                }

                // 2. زر "سماع الأذان قبل وقته 🔊" تحت زر استعداد للصلاة مباشرة
                Button(
                    onClick = { onToggleListenAdhanEarly(next.type) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(48.dp)
                        .testTag("listen_adhan_early_hero_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlayingThis) Color(0xFFC62828) else SoftGoldDark.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(
                        1.2.dp,
                        if (isPlayingThis) Color.White else SoftGoldBright
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlayingThis) Icons.Default.StopCircle else Icons.Default.Campaign,
                            contentDescription = if (isPlayingThis) "إيقاف الأذان" else "سماع الأذان قبل وقته",
                            tint = if (isPlayingThis) Color.White else SoftGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isPlayingThis) {
                                "إيقاف أذان ${next.type.arabicName} ⏹️"
                            } else {
                                "سماع أذان ${next.type.arabicName} قبل وقته 🔊"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = IvoryWhite
                        )
                    }
                }

                // 3. صف المعلومات وخيار التنبيه المسبق
                Row(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isPlayingThis) {
                            "جارٍ سماع صوت ${assignedMuezzin.titleArabic.substringBefore(" (")} 🕌"
                        } else {
                            "المؤذن: ${assignedMuezzin.titleArabic.substringBefore(" (")}"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = if (isPlayingThis) EmeraldLight else TextMuted
                    )

                    OutlinedButton(
                        onClick = onOpenPreAlertSettings,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("pre_alert_setting_button"),
                        shape = RoundedCornerShape(17.dp),
                        border = BorderStroke(0.8.dp, SoftGold.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IvoryWhite),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = SoftGoldBright,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "تنبيه قبل ${uiState.prePrayerAlertMinutes}د ⏱️",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                ),
                                color = SoftGoldBright
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayersTimeline(
    todayPrayers: List<PrayerTimeInfo>,
    prayerLogs: Map<PrayerType, PrayerStatus>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayers_timeline_card"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.75f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "صلوات اليوم الخمس",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )
                val completedCount = prayerLogs.values.count { it == PrayerStatus.ON_TIME || it == PrayerStatus.LATE }
                Text(
                    text = "$completedCount من 5 مكتملة",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (completedCount == 5) EmeraldLight else SoftGold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val mainPrayers = listOf(
                    PrayerType.FAJR,
                    PrayerType.DHUHR,
                    PrayerType.ASR,
                    PrayerType.MAGHRIB,
                    PrayerType.ISHA
                )

                mainPrayers.forEach { type ->
                    val info = todayPrayers.find { it.type == type }
                    val status = prayerLogs[type] ?: PrayerStatus.NOT_RECORDED
                    val isDone = status == PrayerStatus.ON_TIME || status == PrayerStatus.LATE
                    val isNext = info?.isNext == true

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDone -> EmeraldDark
                                        isNext -> SoftGoldDark
                                        else -> MidnightNavySurface
                                    }
                                )
                                .border(
                                    width = if (isNext) 2.dp else 1.dp,
                                    color = when {
                                        isDone -> EmeraldLight
                                        isNext -> SoftGoldBright
                                        else -> GlassBorder.copy(alpha = 0.3f)
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "تمت الصلاة",
                                    tint = EmeraldLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = type.arabicName.take(3),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isNext) SoftGoldBright else IvoryMuted
                                )
                            }
                        }

                        Text(
                            text = type.arabicName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isNext) SoftGoldBright else IvoryMuted
                        )

                        Text(
                            text = info?.timeFormatted ?: "--:--",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyCompanionCard(
    uiState: NoorUiState,
    onChecklistToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_companion_card"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.8f)
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
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = EmeraldLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "يومك مع الله 🤍",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }

                Text(
                    text = "خطوتك القادمة: ${uiState.nextPrayer?.type?.arabicName ?: "الصلاة"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftGoldBright
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive checklist items in a 2-column or list view
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ChecklistItemRow(
                    title = "أذكار الصباح",
                    subtitle = "حصن المسلم اليومي",
                    isDone = uiState.dailyChecklist.morningAzkarDone,
                    onToggle = { onChecklistToggle("morning") }
                )

                ChecklistItemRow(
                    title = "ورد القرآن الكريم",
                    subtitle = "${uiState.quranPagesRead} / ${uiState.quranTargetPages} صفحات اليوم",
                    isDone = uiState.quranPagesRead >= uiState.quranTargetPages,
                    onToggle = { }
                )

                ChecklistItemRow(
                    title = "أذكار المساء",
                    subtitle = "سكينة وطمأنينة المساء",
                    isDone = uiState.dailyChecklist.eveningAzkarDone,
                    onToggle = { onChecklistToggle("evening") }
                )

                ChecklistItemRow(
                    title = "صلاة الوتر وقيام الليل",
                    subtitle = "ختام صلوات اليوم",
                    isDone = uiState.dailyChecklist.witrPrayerDone,
                    onToggle = { onChecklistToggle("witr") }
                )
            }
        }
    }
}

@Composable
fun ChecklistItemRow(
    title: String,
    subtitle: String,
    isDone: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isDone) EmeraldDark.copy(alpha = 0.25f) else MidnightNavySurface.copy(alpha = 0.4f))
            .border(
                1.dp,
                if (isDone) EmeraldPrimary.copy(alpha = 0.35f) else GlassBorder.copy(alpha = 0.15f),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onToggle)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isDone) EmeraldLight else IvoryWhite
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = TextMuted
            )
        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isDone) EmeraldPrimary else Color.Transparent)
                .border(
                    1.5.dp,
                    if (isDone) EmeraldPrimary else TextMuted.copy(alpha = 0.5f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "تم",
                    tint = MidnightNavyDark,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun DailyDhikrCard(
    dhikrText: String,
    tasbihCount: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_dhikr_card"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
        borderColor = EmeraldPrimary.copy(alpha = 0.3f),
        onClick = onIncrement
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SoftGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "ذكر الليلة / اليوم",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftGold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = dhikrText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    color = IvoryWhite
                )
                Text(
                    text = "اضغط في أي مكان لزيادة التسبيح",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(EmeraldDark)
                    .border(1.5.dp, EmeraldLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$tasbihCount",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldLight
                    )
                }
            }
        }
    }
}

/**
 * Floating mini-player shown when Adhan is actively playing,
 * providing the user with real-time feedback and a prominent one-tap stop button.
 */
@Composable
fun FloatingAdhanPlayerBar(
    uiState: NoorUiState,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prayerName = uiState.playingPrayer?.arabicName ?: uiState.nextPrayer?.type?.arabicName ?: "الصلاة"
    val muezzinName = uiState.playingMuezzin?.titleArabic?.substringBefore(" (")
        ?: uiState.selectedMuezzin.titleArabic.substringBefore(" (")

    val infiniteTransition = rememberInfiniteTransition(label = "audioWave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MidnightNavyCard.copy(alpha = 0.98f),
                        EmeraldDark.copy(alpha = 0.95f),
                        MidnightNavyCard.copy(alpha = 0.98f)
                    )
                )
            )
            .border(1.5.dp, EmeraldLight.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EmeraldDark)
                        .border(1.dp, SoftGoldBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = SoftGoldBright,
                        modifier = Modifier
                            .size(22.dp)
                            .scale(waveScale)
                    )
                }

                Column {
                    Text(
                        text = "أذان $prayerName يعمل الآن (سماع مسبق)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = IvoryWhite
                    )
                    Text(
                        text = "صوت المؤذن: $muezzinName 🕌",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = SoftGoldBright
                    )
                }
            }

            Button(
                onClick = onStop,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "إيقاف",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "إيقاف",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Dialog for setting up pre-prayer reminders and testing the Adhan playback before the prayer.
 */
@Composable
fun PreAdhanAlertConfigDialog(
    currentMinutes: Int,
    isAzanPlaying: Boolean,
    nextPrayerName: String,
    onSetMinutes: (Int) -> Unit,
    onToggleListenEarly: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = null,
                    tint = SoftGoldBright,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "التنبيه وسماع الأذان قبل وقته ⏱️🔊",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "اختر توقيت التنبيه المسبق للاستعداد والوضوء لصلاة $nextPrayerName:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = IvoryWhite
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(3, 5, 10, 15, 20).forEach { mins ->
                        val isSelected = currentMinutes == mins
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) EmeraldDark else MidnightNavySurface)
                                .border(
                                    1.dp,
                                    if (isSelected) SoftGoldBright else GlassBorder.copy(alpha = 0.3f),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSetMinutes(mins) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$mins د",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) SoftGoldBright else IvoryMuted
                            )
                        }
                    }
                }

                Divider(color = GlassBorder.copy(alpha = 0.3f), thickness = 0.8.dp)

                Text(
                    text = "سماع الأذان المسبق مباشرة قبل حلول الموعد:",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = IvoryMuted
                )

                Button(
                    onClick = onToggleListenEarly,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAzanPlaying) EmeraldDark else SoftGoldDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isAzanPlaying) EmeraldLight else SoftGoldBright)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isAzanPlaying) Icons.Default.StopCircle else Icons.Default.Campaign,
                            contentDescription = null,
                            tint = if (isAzanPlaying) EmeraldLight else SoftGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isAzanPlaying) "إيقاف أذان $nextPrayerName ⏹️" else "استمع لأذان $nextPrayerName الآن 🔊",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "تم وحفظ", color = SoftGoldBright, fontWeight = FontWeight.Bold)
            }
        }
    )
}
