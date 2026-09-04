package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerStatus
import com.example.data.model.PrayerType
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PrayersScreen(
    uiState: NoorUiState,
    onSelectDate: (Calendar) -> Unit,
    onLogStatus: (PrayerType, PrayerStatus) -> Unit,
    onToggleAzan: (PrayerType) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenMuezzinSelection: () -> Unit = onOpenSettings,
    onToggleListenAdhanEarly: (PrayerType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showMonthlyDialog by remember { mutableStateOf(false) }
    val displayFormat = SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar"))


    val todayCal = remember { Calendar.getInstance() }
    val yesterdayCal = remember { (Calendar.getInstance().clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) } }
    val tomorrowCal = remember { (Calendar.getInstance().clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) } }

    val isToday = uiState.selectedDate.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR)
    val isYesterday = uiState.selectedDate.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)
    val isTomorrow = uiState.selectedDate.get(Calendar.DAY_OF_YEAR) == tomorrowCal.get(Calendar.DAY_OF_YEAR)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("prayers_screen")
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
                            text = "مواقيت الصلاة",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        Text(
                            text = "${uiState.cityName} • طريقة ${uiState.calculationMethod.arabicName}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassIconButton(
                            icon = Icons.Default.CalendarMonth,
                            contentDescription = "التقويم الشهري",
                            onClick = { showMonthlyDialog = true },
                            tint = SoftGold
                        )
                        GlassIconButton(
                            icon = Icons.Default.Tune,
                            contentDescription = "إعدادات الأذان",
                            onClick = onOpenSettings,
                            tint = EmeraldLight
                        )
                    }
                }
            }

            // Quick Date Selector Pills (Yesterday / Today / Tomorrow)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatePill(
                        label = "أمس",
                        isSelected = isYesterday,
                        onClick = { onSelectDate(yesterdayCal) },
                        modifier = Modifier.weight(1f)
                    )
                    DatePill(
                        label = "اليوم",
                        isSelected = isToday,
                        onClick = { onSelectDate(todayCal) },
                        modifier = Modifier.weight(1f)
                    )
                    DatePill(
                        label = "غداً",
                        isSelected = isTomorrow,
                        onClick = { onSelectDate(tomorrowCal) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Hijri Date Banner
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavyCard.copy(alpha = 0.7f),
                    borderColor = SoftGold.copy(alpha = 0.3f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = uiState.hijriDate.formatted,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                            Text(
                                text = displayFormat.format(uiState.selectedDate.time),
                                style = MaterialTheme.typography.bodySmall,
                                color = IvoryMuted
                            )
                        }

                        GlassTag(
                            text = "المواقيت الفلكية دقيقة 100%",
                            accentColor = EmeraldPrimary,
                            textColor = EmeraldLight
                        )
                    }
                }
            }

            // Muezzin & Volume Quick Bar
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenMuezzinSelection),
                    backgroundColor = MidnightNavySurface.copy(alpha = 0.85f),
                    borderColor = SoftGold.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldDark.copy(alpha = 0.6f))
                                    .border(1.dp, EmeraldLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (uiState.isAzanAudioPlaying) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = if (uiState.isAzanAudioPlaying) SoftGoldBright else EmeraldLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "تخصيص صوت ومستوى الأذان لكل صلاة",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = IvoryWhite
                                )
                                Text(
                                    text = "المؤذن الحالي: ${uiState.selectedMuezzin.titleArabic.substringBefore(" (")} • اضغط للتخصيص الكامل",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = SoftGoldBright
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "تعديل",
                            tint = SoftGold
                        )
                    }
                }
            }

            // Early Adhan Hearing Quick Banner for the next prayer
            if (isToday && uiState.nextPrayer != null) {
                val nextP = uiState.nextPrayer
                val isPlayingNext = uiState.isAzanAudioPlaying && (uiState.playingPrayer == nextP.type || uiState.playingPrayer == null)
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = if (isPlayingNext) EmeraldDark.copy(alpha = 0.85f) else MidnightNavyCard.copy(alpha = 0.85f),
                        borderColor = if (isPlayingNext) EmeraldLight else SoftGoldBright.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "الأذان القادم: صلاة ${nextP.type.arabicName} (${nextP.timeFormatted})",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SoftGoldBright
                                )
                                Text(
                                    text = if (isPlayingNext) "جارٍ سماع الأذان الآن..." else "يمكنك سماع الأذان قبل حلول الموعد",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = IvoryWhite
                                )
                            }

                            Button(
                                onClick = { onToggleListenAdhanEarly(nextP.type) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPlayingNext) Color(0xFFC62828) else EmeraldDark
                                ),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, if (isPlayingNext) Color.White else EmeraldLight),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingNext) Icons.Default.Stop else Icons.Default.Hearing,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isPlayingNext) "إيقاف ⏹️" else "سماع الأذان 🔊",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // List of Prayer Cards (Fajr, Sunrise, Dhuhr, Asr, Maghrib, Isha)
            items(uiState.selectedDatePrayerTimes) { prayer ->
                val prayerConfig = uiState.prayerAzanConfigs[prayer.type]
                val isAzanActive = prayerConfig?.isEnabled ?: (uiState.prayerAzanEnabled[prayer.type] ?: true)
                val assignedMuezzinName = prayerConfig?.muezzin?.titleArabic?.substringBefore(" (") ?: uiState.selectedMuezzin.titleArabic.substringBefore(" (")
                val assignedVol = ((prayerConfig?.volume ?: uiState.azanVolume) * 100).toInt()
                val isPlayingThis = uiState.isAzanAudioPlaying && (uiState.playingPrayer == prayer.type)

                PrayerTimeScheduleCard(
                    prayer = prayer,
                    isAzanEnabled = isAzanActive,
                    onToggleAzan = { onToggleAzan(prayer.type) },
                    muezzinName = assignedMuezzinName,
                    volumePercent = assignedVol,
                    onConfigureSound = onOpenMuezzinSelection,
                    isAdhanPlaying = isPlayingThis,
                    onToggleListenAdhanEarly = { onToggleListenAdhanEarly(prayer.type) }
                )
            }

            // Interactive "صلاتي اليوم" Tracker (only on today)
            if (isToday) {
                item {
                    PrayersTrackerSection(
                        todayPrayers = uiState.todayPrayerTimes,
                        prayerLogs = uiState.todayPrayerLogs,
                        onLogStatus = onLogStatus
                    )
                }
            }
        }
    }

    if (showMonthlyDialog) {
        MonthlyPrayerCalendarDialog(onDismiss = { showMonthlyDialog = false })
    }
}

@Composable
fun DatePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) EmeraldDark else MidnightNavySurface)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) EmeraldLight else IvoryMuted
        )
    }
}
