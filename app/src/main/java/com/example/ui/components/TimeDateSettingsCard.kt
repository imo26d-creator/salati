package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.calculator.HijriCalendarHelper
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimeDateSettingsCard(
    uiState: NoorUiState,
    onToggleAutoPhoneTime: (Boolean) -> Unit,
    onSyncWithPhoneNow: () -> Unit,
    onToggle24HourFormat: (Boolean) -> Unit,
    onAdjustManualTimeMinutes: (Int) -> Unit,
    onSetManualTimeOffset: (Int) -> Unit,
    onSetSpecificCustomTime: (Int, Int) -> Unit,
    onSetHijriDateAdjustment: (Int) -> Unit,
    onSelectCustomDate: (Calendar) -> Unit,
    onSetSpecificCustomDate: (Int, Int, Int) -> Unit,
    onResetDateToToday: () -> Unit,
    onResetAllTimeAndDateSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val hasCustomModifications = !uiState.isAutoPhoneTime ||
            uiState.manualTimeOffsetMinutes != 0 ||
            uiState.hijriDateAdjustmentDays != 0 ||
            uiState.isCustomDateSelected

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MidnightNavySurface.copy(alpha = 0.95f))
            .border(1.dp, SoftGoldBright.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Card Header
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
                        .clip(RoundedCornerShape(10.dp))
                        .background(EmeraldDark.copy(alpha = 0.6f))
                        .border(1.dp, EmeraldLight.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = SoftGoldBright,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "تعديل الوقت والتاريخ ⏱️📅",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                    Text(
                        text = "ضبط التوقيت، التاريخ الهجري والميلادي",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = IvoryMuted
                    )
                }
            }

            // Status Badge
            Surface(
                color = if (hasCustomModifications) SoftGoldDark.copy(alpha = 0.25f) else EmeraldDark.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    0.8.dp,
                    if (hasCustomModifications) SoftGoldBright.copy(alpha = 0.6f) else EmeraldPrimary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (hasCustomModifications) "معدل يدوياً ⚙️" else "تلقائي مع الهاتف 🟢",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                    color = if (hasCustomModifications) SoftGoldBright else EmeraldLight
                )
            }
        }

        // Live Status Panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MidnightNavyCard.copy(alpha = 0.85f))
                .border(0.8.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الوقت المعتمد في التطبيق:",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted
                )
                Text(
                    text = if (uiState.deviceCurrentTimeFormatted.isNotEmpty()) uiState.deviceCurrentTimeFormatted else "جاري التحديث...",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = SoftGoldBright
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "التاريخ الهجري المعتمد:",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted
                )
                Text(
                    text = uiState.hijriDate.formatted,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = EmeraldLight
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "التاريخ الميلادي المعتمد:",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted
                )
                Text(
                    text = uiState.gregorianDateFormatted,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = IvoryWhite
                )
            }

            if (uiState.deviceTimeZoneName.isNotEmpty()) {
                Text(
                    text = "المنطقة الزمنية: ${uiState.deviceTimeZoneName}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextDim
                )
            }
        }

        // Feedback Banner
        AnimatedVisibility(
            visible = uiState.timeSyncSuccessMessage != null || uiState.dateSyncSuccessMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val msg = uiState.timeSyncSuccessMessage ?: uiState.dateSyncSuccessMessage ?: ""
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(EmeraldDark.copy(alpha = 0.85f))
                    .border(0.8.dp, EmeraldLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldLight,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )
            }
        }

        HorizontalDivider(color = GlassBorder.copy(alpha = 0.25f), thickness = 0.8.dp)

        // ================= SECTION 1: تعديل الوقت =================
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "1. تعديل وضبط الوقت 🕒",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = SoftGoldBright
            )

            // Auto Phone Time Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "المزامنة التلقائية مع وقت الهاتف",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = IvoryWhite
                    )
                    Text(
                        text = "تحديث الثواني والدقائق مباشرة مع ساعة هاتفك",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = IvoryMuted
                    )
                }
                Switch(
                    checked = uiState.isAutoPhoneTime,
                    onCheckedChange = onToggleAutoPhoneTime,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SoftGoldBright,
                        checkedTrackColor = EmeraldPrimary,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = MidnightNavyCard
                    )
                )
            }

            // Quick Stepper Chips for Time Offset
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MidnightNavyCard.copy(alpha = 0.6f))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تقديم / تأخير الوقت (بالدقائق):",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                        color = IvoryWhite
                    )
                    Text(
                        text = if (uiState.manualTimeOffsetMinutes == 0) "0 دقيقة (مضبوط)"
                        else "${if (uiState.manualTimeOffsetMinutes > 0) "+${uiState.manualTimeOffsetMinutes}" else "${uiState.manualTimeOffsetMinutes}"} دقيقة",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.manualTimeOffsetMinutes != 0) SoftGoldBright else EmeraldLight
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TimeOffsetChip(label = "-15د", onClick = { onAdjustManualTimeMinutes(-15) }, modifier = Modifier.weight(1f))
                    TimeOffsetChip(label = "-5د", onClick = { onAdjustManualTimeMinutes(-5) }, modifier = Modifier.weight(1f))
                    TimeOffsetChip(label = "-1د", onClick = { onAdjustManualTimeMinutes(-1) }, modifier = Modifier.weight(1f))
                    TimeOffsetChip(
                        label = "إعادة 0",
                        isZero = true,
                        onClick = { onSetManualTimeOffset(0) },
                        modifier = Modifier.weight(1.2f)
                    )
                    TimeOffsetChip(label = "+1د", onClick = { onAdjustManualTimeMinutes(1) }, modifier = Modifier.weight(1f))
                    TimeOffsetChip(label = "+5د", onClick = { onAdjustManualTimeMinutes(5) }, modifier = Modifier.weight(1f))
                    TimeOffsetChip(label = "+15د", onClick = { onAdjustManualTimeMinutes(15) }, modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = { showTimePickerDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = SoftGoldBright, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ضبط وقت محدد يدويًا 🕒", fontSize = 11.sp, color = SoftGoldBright)
                    }
                }
            }

            // 24 Hour Format Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "تنسيق 24 ساعة",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = IvoryWhite
                    )
                    Text(
                        text = if (uiState.is24HourFormat) "صيغة 24 ساعة مفعلة" else "صيغة 12 ساعة (ص/م)",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = IvoryMuted
                    )
                }
                Switch(
                    checked = uiState.is24HourFormat,
                    onCheckedChange = onToggle24HourFormat,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SoftGoldBright,
                        checkedTrackColor = EmeraldPrimary,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = MidnightNavyCard
                    )
                )
            }
        }

        HorizontalDivider(color = GlassBorder.copy(alpha = 0.25f), thickness = 0.8.dp)

        // ================= SECTION 2: تعديل التاريخ الهجري =================
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2. تعديل التاريخ الهجري (رؤية الهلال) 🌙",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
                Text(
                    text = if (uiState.hijriDateAdjustmentDays == 0) "تلقائي"
                    else "${if (uiState.hijriDateAdjustmentDays > 0) "+${uiState.hijriDateAdjustmentDays}" else "${uiState.hijriDateAdjustmentDays}"} يوم",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.hijriDateAdjustmentDays != 0) SoftGoldBright else EmeraldLight
                    )
                )
            }

            Text(
                text = "تقديم أو تأخير التاريخ الهجري بحسب الرؤية الشرعية للهلال في بلدكم:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = IvoryMuted
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(-2, -1, 0, 1, 2).forEach { days ->
                    val isSelected = uiState.hijriDateAdjustmentDays == days
                    val label = when (days) {
                        -2 -> "-2 يوم"
                        -1 -> "-1 يوم"
                        0 -> "تلقائي (0)"
                        1 -> "+1 يوم"
                        2 -> "+2 يوم"
                        else -> "$days"
                    }

                    Surface(
                        onClick = { onSetHijriDateAdjustment(days) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) EmeraldDark else MidnightNavyCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) EmeraldLight else GlassBorder.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) IvoryWhite else IvoryMuted
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = GlassBorder.copy(alpha = 0.25f), thickness = 0.8.dp)

        // ================= SECTION 3: تعديل التاريخ الميلادي واستعراض الأيام =================
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "3. تعديل التاريخ الميلادي واستعراض الأيام 📅",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = SoftGoldBright
            )

            Text(
                text = "حساب المواقيت والتقويم لأيام ماضية أو قادمة:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = IvoryMuted
            )

            // Quick date shift: Yesterday, Today, Tomorrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val cal = (Calendar.getInstance().clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
                        onSelectCustomDate(cal)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Text("الأمس ⬅️", fontSize = 11.sp, color = IvoryMuted)
                }

                Button(
                    onClick = onResetDateToToday,
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!uiState.isCustomDateSelected) EmeraldPrimary else MidnightNavyCard
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (!uiState.isCustomDateSelected) EmeraldLight else GlassBorder.copy(alpha = 0.4f)
                    ),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Text("اليوم 🌟", fontSize = 11.sp, color = IvoryWhite, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val cal = (Calendar.getInstance().clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
                        onSelectCustomDate(cal)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Text("➡️ غداً", fontSize = 11.sp, color = IvoryMuted)
                }
            }

            // Pick specific date button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { showDatePickerDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MidnightNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = SoftGoldBright, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("اختيار تاريخ محدد من التقويم 🗓️", fontSize = 11.sp, color = SoftGoldBright)
                }

                if (uiState.isCustomDateSelected) {
                    OutlinedButton(
                        onClick = onResetDateToToday,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight.copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("العودة لليوم", fontSize = 11.sp, color = EmeraldLight)
                    }
                }
            }
        }

        HorizontalDivider(color = GlassBorder.copy(alpha = 0.25f), thickness = 0.8.dp)

        // ================= SECTION 4: المزامنة السريعة واستعادة الافتراضي =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = onSyncWithPhoneNow,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = IvoryWhite, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("مزامنة فورية مع الهاتف ⏱️", fontSize = 11.sp, color = IvoryWhite, fontWeight = FontWeight.Bold)
            }

            if (hasCustomModifications) {
                OutlinedButton(
                    onClick = onResetAllTimeAndDateSettings,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.4f)),
                    modifier = Modifier.weight(0.9f),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Restore, contentDescription = null, tint = SoftGoldBright, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("استعادة الافتراضي", fontSize = 11.sp, color = SoftGoldBright)
                }
            }
        }
    }

    // Dialogs
    if (showTimePickerDialog) {
        TimePickerAdjustmentDialog(
            currentTimeCal = uiState.selectedDate,
            onConfirm = { hour, minute ->
                onSetSpecificCustomTime(hour, minute)
                showTimePickerDialog = false
            },
            onDismiss = { showTimePickerDialog = false }
        )
    }

    if (showDatePickerDialog) {
        DatePickerAdjustmentDialog(
            currentDate = uiState.selectedDate,
            onConfirm = { year, monthIndex, day ->
                onSetSpecificCustomDate(year, monthIndex, day)
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
}

@Composable
private fun TimeOffsetChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isZero: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = if (isZero) EmeraldDark.copy(alpha = 0.5f) else MidnightNavySurface,
        border = androidx.compose.foundation.BorderStroke(
            0.8.dp,
            if (isZero) EmeraldLight.copy(alpha = 0.4f) else GlassBorder.copy(alpha = 0.3f)
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(vertical = 6.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
            color = if (isZero) EmeraldLight else IvoryWhite
        )
    }
}

/**
 * Modern Arabic Time Picker Dialog
 */
@Composable
fun TimePickerAdjustmentDialog(
    currentTimeCal: Calendar,
    onConfirm: (hourOfDay: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val initialHour = currentTimeCal.get(Calendar.HOUR_OF_DAY)
    val initialMinute = currentTimeCal.get(Calendar.MINUTE)

    var hour12 by remember {
        mutableStateOf(
            when (val h = initialHour % 12) {
                0 -> 12
                else -> h
            }
        )
    }
    var isPm by remember { mutableStateOf(initialHour >= 12) }
    var minute by remember { mutableStateOf(initialMinute) }

    val calculatedHourOfDay = when {
        isPm && hour12 < 12 -> hour12 + 12
        !isPm && hour12 == 12 -> 0
        else -> hour12
    }

    val previewText = String.format("%02d:%02d %s", hour12, minute, if (isPm) "م" else "ص")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MidnightNavyCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SoftGoldBright.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ضبط وقت محدد يدويًا 🕒",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                    }
                }

                // Digital Preview Clock
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavyDark)
                        .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = previewText,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        ),
                        color = SoftGoldBright
                    )
                }

                // Hours and Minutes controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hours Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("الساعة", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        IconButton(
                            onClick = { hour12 = if (hour12 >= 12) 1 else hour12 + 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "زيادة الساعة", tint = SoftGoldBright)
                        }
                        Text(
                            text = String.format("%02d", hour12),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        IconButton(
                            onClick = { hour12 = if (hour12 <= 1) 12 else hour12 - 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "إنقاص الساعة", tint = SoftGoldBright)
                        }
                    }

                    Text(":", style = MaterialTheme.typography.headlineLarge, color = SoftGoldBright)

                    // Minutes Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("الدقيقة", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        IconButton(
                            onClick = { minute = (minute + 1) % 60 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "زيادة الدقيقة", tint = SoftGoldBright)
                        }
                        Text(
                            text = String.format("%02d", minute),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        IconButton(
                            onClick = { minute = if (minute <= 0) 59 else minute - 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "إنقاص الدقيقة", tint = SoftGoldBright)
                        }
                    }

                    // AM / PM Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("الفترة", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Surface(
                            onClick = { isPm = false },
                            shape = RoundedCornerShape(8.dp),
                            color = if (!isPm) EmeraldDark else MidnightNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (!isPm) EmeraldLight else GlassBorder.copy(alpha = 0.3f))
                        ) {
                            Text(
                                "صباحاً (ص)",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 11.sp,
                                fontWeight = if (!isPm) FontWeight.Bold else FontWeight.Normal,
                                color = if (!isPm) IvoryWhite else TextMuted
                            )
                        }
                        Surface(
                            onClick = { isPm = true },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isPm) EmeraldDark else MidnightNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isPm) EmeraldLight else GlassBorder.copy(alpha = 0.3f))
                        ) {
                            Text(
                                "مساءً (م)",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 11.sp,
                                fontWeight = if (isPm) FontWeight.Bold else FontWeight.Normal,
                                color = if (isPm) IvoryWhite else TextMuted
                            )
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder.copy(alpha = 0.3f))
                    ) {
                        Text("إلغاء", color = IvoryMuted)
                    }

                    Button(
                        onClick = { onConfirm(calculatedHourOfDay, minute) },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("حفظ الوقت 💾", color = IvoryWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Modern Arabic Date Picker Dialog
 */
@Composable
fun DatePickerAdjustmentDialog(
    currentDate: Calendar,
    onConfirm: (year: Int, monthIndex: Int, dayOfMonth: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedYear by remember { mutableStateOf(currentDate.get(Calendar.YEAR)) }
    var selectedMonthIndex by remember { mutableStateOf(currentDate.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(currentDate.get(Calendar.DAY_OF_MONTH)) }

    val arabicMonths = remember {
        listOf(
            "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
            "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
        )
    }

    val maxDaysInMonth = remember(selectedYear, selectedMonthIndex) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    LaunchedEffect(maxDaysInMonth) {
        if (selectedDay > maxDaysInMonth) {
            selectedDay = maxDaysInMonth
        }
    }

    val previewCal = remember(selectedYear, selectedMonthIndex, selectedDay) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonthIndex)
            set(Calendar.DAY_OF_MONTH, selectedDay)
        }
    }

    val displayFormat = remember { SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar")) }
    val hijriPreview = remember(previewCal) {
        HijriCalendarHelper.getHijriDate(previewCal)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MidnightNavyCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SoftGoldBright.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اختيار تاريخ محدد 📅",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                    }
                }

                // Date Preview Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavyDark)
                        .border(1.dp, SoftGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = displayFormat.format(previewCal.time),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = hijriPreview.formatted,
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldLight,
                        textAlign = TextAlign.Center
                    )
                }

                // Selectors: Day, Month, Year
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Day Selector
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("اليوم", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        IconButton(
                            onClick = { selectedDay = if (selectedDay >= maxDaysInMonth) 1 else selectedDay + 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "زيادة اليوم", tint = SoftGoldBright)
                        }
                        Text(
                            text = String.format("%02d", selectedDay),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        IconButton(
                            onClick = { selectedDay = if (selectedDay <= 1) maxDaysInMonth else selectedDay - 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "إنقاص اليوم", tint = SoftGoldBright)
                        }
                    }

                    // Month Selector
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("الشهر", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        IconButton(
                            onClick = { selectedMonthIndex = (selectedMonthIndex + 1) % 12 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "زيادة الشهر", tint = SoftGoldBright)
                        }
                        Text(
                            text = arabicMonths[selectedMonthIndex],
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = SoftGoldBright
                        )
                        IconButton(
                            onClick = { selectedMonthIndex = if (selectedMonthIndex <= 0) 11 else selectedMonthIndex - 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "إنقاص الشهر", tint = SoftGoldBright)
                        }
                    }

                    // Year Selector
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("السنة", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        IconButton(
                            onClick = { selectedYear += 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "زيادة السنة", tint = SoftGoldBright)
                        }
                        Text(
                            text = "$selectedYear",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        IconButton(
                            onClick = { selectedYear -= 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MidnightNavySurface, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "إنقاص السنة", tint = SoftGoldBright)
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder.copy(alpha = 0.3f))
                    ) {
                        Text("إلغاء", color = IvoryMuted)
                    }

                    Button(
                        onClick = { onConfirm(selectedYear, selectedMonthIndex, selectedDay) },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("اعتماد التاريخ 📅", color = IvoryWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
