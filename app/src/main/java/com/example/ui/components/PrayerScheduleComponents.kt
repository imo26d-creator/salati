package com.example.ui.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerStatus
import com.example.data.model.PrayerTimeInfo
import com.example.data.model.PrayerType
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PrayerTimeScheduleCard(
    prayer: PrayerTimeInfo,
    isAzanEnabled: Boolean,
    onToggleAzan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNext = prayer.isNext
    val isPast = prayer.isPast

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_row_${prayer.type.name.lowercase()}"),
        backgroundColor = if (isNext) MidnightNavySurface.copy(alpha = 0.9f) else MidnightNavyCard.copy(alpha = 0.7f),
        borderColor = if (isNext) SoftGold else GlassBorder.copy(alpha = 0.2f),
        borderWidth = if (isNext) 1.5.dp else 1.dp
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
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isNext -> SoftGoldDark
                                isPast -> MidnightNavySurface
                                else -> EmeraldDark
                            }
                        )
                        .border(
                            1.dp,
                            when {
                                isNext -> SoftGoldBright
                                else -> GlassBorder.copy(alpha = 0.3f)
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (prayer.type) {
                            PrayerType.FAJR -> Icons.Default.NightlightRound
                            PrayerType.SUNRISE -> Icons.Default.WbSunny
                            PrayerType.DHUHR -> Icons.Default.LightMode
                            PrayerType.ASR -> Icons.Default.WbTwilight
                            PrayerType.MAGHRIB -> Icons.Default.Brightness4
                            PrayerType.ISHA -> Icons.Default.Bedtime
                        },
                        contentDescription = null,
                        tint = if (isNext) SoftGoldBright else if (isPast) TextMuted else EmeraldLight,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = prayer.type.arabicName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold
                            ),
                            color = if (isNext) SoftGoldBright else IvoryWhite
                        )
                        if (isNext) {
                            GlassTag(
                                text = "القادمة",
                                accentColor = SoftGold,
                                textColor = SoftGoldBright
                            )
                        }
                    }
                    Text(
                        text = prayer.type.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = prayer.timeFormatted,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = if (isNext) SoftGoldBright else IvoryWhite
                )

                IconButton(
                    onClick = onToggleAzan,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isAzanEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                        contentDescription = "تنبيه الأذان",
                        tint = if (isAzanEnabled) EmeraldLight else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PrayersTrackerSection(
    todayPrayers: List<PrayerTimeInfo>,
    prayerLogs: Map<PrayerType, PrayerStatus>,
    onLogStatus: (PrayerType, PrayerStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainPrayers = listOf(
        PrayerType.FAJR,
        PrayerType.DHUHR,
        PrayerType.ASR,
        PrayerType.MAGHRIB,
        PrayerType.ISHA
    )

    val completedCount = mainPrayers.count {
        val s = prayerLogs[it]
        s == PrayerStatus.ON_TIME || s == PrayerStatus.LATE
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayers_tracker_section"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.85f),
        borderColor = EmeraldPrimary.copy(alpha = 0.35f)
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
                        imageVector = Icons.Default.TaskAlt,
                        contentDescription = null,
                        tint = EmeraldLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "صلاتي اليوم",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }

                if (completedCount == 5) {
                    GlassTag(
                        text = "⭐ أتممت 5 من 5 صلوات",
                        accentColor = EmeraldPrimary,
                        textColor = EmeraldLight
                    )
                } else {
                    Text(
                        text = "$completedCount / 5 صلوات",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            mainPrayers.forEach { type ->
                val currentStatus = prayerLogs[type] ?: PrayerStatus.NOT_RECORDED

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavySurface.copy(alpha = 0.4f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = type.arabicName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = IvoryWhite
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Option 1: في الوقت
                        StatusChip(
                            label = "في الوقت ✓",
                            isSelected = currentStatus == PrayerStatus.ON_TIME,
                            selectedColor = EmeraldPrimary,
                            onClick = { onLogStatus(type, PrayerStatus.ON_TIME) }
                        )

                        // Option 2: متأخراً
                        StatusChip(
                            label = "متأخراً ⏳",
                            isSelected = currentStatus == PrayerStatus.LATE,
                            selectedColor = SoftGold,
                            onClick = { onLogStatus(type, PrayerStatus.LATE) }
                        )

                        // Option 3: لم أصلِّ
                        StatusChip(
                            label = "لم أصلِّ",
                            isSelected = currentStatus == PrayerStatus.MISSED,
                            selectedColor = Color(0xFFEF4444),
                            onClick = { onLogStatus(type, PrayerStatus.MISSED) }
                        )
                    }
                }
            }

            if (completedCount > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (completedCount == 5) "بارك الله فيك! حافظ على صلاتك تنل رضا ربك وسكينة قلبك 🤍" else "استمر في المحافظة على صلاتك، فكل خطوة وسجدة ترفعك درجة.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, textAlign = TextAlign.Center),
                    color = IvoryMuted,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun StatusChip(
    label: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) selectedColor.copy(alpha = 0.25f) else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) selectedColor else GlassBorder.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) selectedColor else TextMuted
        )
    }
}

@Composable
fun MonthlyPrayerCalendarDialog(
    onDismiss: () -> Unit
) {
    val days = (1..30).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تقويم مواقيت الصلاة الشهري",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = SoftGold)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "جدول تقريبي لمواقيت الصلاة خلال الشهر الجاري",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MidnightNavySurface)
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = "اليوم", style = MaterialTheme.typography.labelSmall, color = SoftGold)
                    Text(text = "الفجر", style = MaterialTheme.typography.labelSmall, color = IvoryMuted)
                    Text(text = "الظهر", style = MaterialTheme.typography.labelSmall, color = IvoryMuted)
                    Text(text = "العصر", style = MaterialTheme.typography.labelSmall, color = IvoryMuted)
                    Text(text = "المغرب", style = MaterialTheme.typography.labelSmall, color = IvoryMuted)
                    Text(text = "العشاء", style = MaterialTheme.typography.labelSmall, color = IvoryMuted)
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(days) { day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (day % 2 == 0) MidnightNavySurface.copy(alpha = 0.3f) else Color.Transparent)
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = "$day", style = MaterialTheme.typography.bodySmall, color = SoftGold)
                            Text(text = "04:22", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                            Text(text = "12:08", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                            Text(text = "15:35", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                            Text(text = "18:24", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                            Text(text = "19:54", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "إغلاق", color = SoftGold)
            }
        }
    )
}
