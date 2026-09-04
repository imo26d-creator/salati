package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun AzkarNotificationCard(
    morningEnabled: Boolean,
    morningHour: Int,
    morningMinute: Int,
    eveningEnabled: Boolean,
    eveningHour: Int,
    eveningMinute: Int,
    streakDays: Int,
    onSaveMorning: (Boolean, Int, Int) -> Unit,
    onSaveEvening: (Boolean, Int, Int) -> Unit,
    onSendTestNotification: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMorningTimePicker by remember { mutableStateOf(false) }
    var showEveningTimePicker by remember { mutableStateOf(false) }

    fun formatTimeDisplay(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "ص" else "م"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("azkar_notification_card"),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.9f),
        borderColor = EmeraldLight.copy(alpha = 0.35f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(EmeraldDark)
                        .border(1.dp, EmeraldLight.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "تنبيهات الأذكار",
                        tint = SoftGoldBright,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "إشعارات الأذكار التلقائية ⏰",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                    Text(
                        text = "أوقات مخصصة تختارها بنفسك مع تذكير يومي للمحافظة",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            // Consistency Streak Banner ("لتعزيز الاستمرارية")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MidnightNavySurface,
                                EmeraldDark.copy(alpha = 0.4f),
                                MidnightNavySurface
                            )
                        )
                    )
                    .border(1.dp, SoftGold.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🔥", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "سلسلة المحافظة على الأذكار",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                            Text(
                                text = "«أحبّ الأعمال إلى الله أدومها وإن قلّ»",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = IvoryMuted
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldDark,
                        border = borderStroke(0.5.dp, SoftGold.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "$streakDays أيام متتالية",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SoftGoldBright
                        )
                    }
                }
            }

            // Morning Azkar Reminder Row
            AzkarReminderItemRow(
                iconEmoji = "☀️",
                title = "أذكار الصباح",
                timeFormatted = formatTimeDisplay(morningHour, morningMinute),
                isEnabled = morningEnabled,
                onToggle = { isChecked ->
                    onSaveMorning(isChecked, morningHour, morningMinute)
                },
                onChangeTimeClick = { showMorningTimePicker = true },
                onSendTestClick = { onSendTestNotification(true) }
            )

            HorizontalDivider(
                color = GlassBorder.copy(alpha = 0.2f),
                thickness = 0.5.dp
            )

            // Evening Azkar Reminder Row
            AzkarReminderItemRow(
                iconEmoji = "🌙",
                title = "أذكار المساء",
                timeFormatted = formatTimeDisplay(eveningHour, eveningMinute),
                isEnabled = eveningEnabled,
                onToggle = { isChecked ->
                    onSaveEvening(isChecked, eveningHour, eveningMinute)
                },
                onChangeTimeClick = { showEveningTimePicker = true },
                onSendTestClick = { onSendTestNotification(false) }
            )
        }
    }

    // Morning Time Picker Dialog
    if (showMorningTimePicker) {
        AzkarCustomTimePickerDialog(
            title = "اختيار موعد أذكار الصباح ☀️",
            initialHour = morningHour,
            initialMinute = morningMinute,
            isMorning = true,
            onDismiss = { showMorningTimePicker = false },
            onConfirm = { h, m ->
                onSaveMorning(true, h, m)
                showMorningTimePicker = false
            }
        )
    }

    // Evening Time Picker Dialog
    if (showEveningTimePicker) {
        AzkarCustomTimePickerDialog(
            title = "اختيار موعد أذكار المساء 🌙",
            initialHour = eveningHour,
            initialMinute = eveningMinute,
            isMorning = false,
            onDismiss = { showEveningTimePicker = false },
            onConfirm = { h, m ->
                onSaveEvening(true, h, m)
                showEveningTimePicker = false
            }
        )
    }
}

@Composable
private fun AzkarReminderItemRow(
    iconEmoji: String,
    title: String,
    timeFormatted: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onChangeTimeClick: () -> Unit,
    onSendTestClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = iconEmoji, fontSize = 22.sp)

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Clickable time button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isEnabled) MidnightNavyLight.copy(alpha = 0.6f) else MidnightNavySurface)
                        .border(
                            1.dp,
                            if (isEnabled) EmeraldLight.copy(alpha = 0.4f) else GlassBorder.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(onClick = onChangeTimeClick)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "الوقت",
                        tint = if (isEnabled) SoftGoldBright else TextDim,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = if (isEnabled) IvoryWhite else TextDim
                    )
                    Text(
                        text = "تعديل",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = EmeraldLight
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Test Button
            IconButton(
                onClick = onSendTestClick,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MidnightNavySurface),
                enabled = isEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "تجربة الإشعار",
                    tint = if (isEnabled) SoftGold else TextDim,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Switch
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = EmeraldLight,
                    checkedTrackColor = EmeraldDark,
                    uncheckedThumbColor = TextDim,
                    uncheckedTrackColor = MidnightNavySurface
                ),
                modifier = Modifier.testTag("azkar_switch_${title.replace(" ", "_")}")
            )
        }
    }
}

@Composable
fun AzkarCustomTimePickerDialog(
    title: String,
    initialHour: Int,
    initialMinute: Int,
    isMorning: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(initialHour) }
    var selectedMinute by remember { mutableStateOf(initialMinute) }

    // Convert 24h to 12h representation
    var isPm by remember { mutableStateOf(initialHour >= 12) }
    var hour12 by remember {
        mutableStateOf(
            when {
                initialHour == 0 -> 12
                initialHour > 12 -> initialHour - 12
                else -> initialHour
            }
        )
    }

    fun updateHour(new12Hour: Int, newIsPm: Boolean) {
        val calculated24 = when {
            newIsPm && new12Hour < 12 -> new12Hour + 12
            !newIsPm && new12Hour == 12 -> 0
            else -> new12Hour
        }
        selectedHour = calculated24
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = MidnightNavyCard,
            border = borderStroke(1.5.dp, EmeraldLight.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "اختر الوقت الأنسب لوردك اليومي لضمان المحافظة عليه",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                // Large Time Display Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightNavySurface)
                        .border(1.dp, SoftGold.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(vertical = 14.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = String.format(Locale.US, "%02d", hour12),
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                            color = SoftGoldBright
                        )
                        Text(
                            text = " : ",
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryMuted
                        )
                        Text(
                            text = String.format(Locale.US, "%02d", selectedMinute),
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                            color = SoftGoldBright
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldDark
                        ) {
                            Text(
                                text = if (isPm) "مساءً (م)" else "صباحاً (ص)",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldLight
                            )
                        }
                    }
                }

                // Quick Presets
                Text(
                    text = "أوقات مقترحة سريعة:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = SoftGold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isMorning) {
                        PresetChip(label = "05:30 ص", onClick = {
                            hour12 = 5; isPm = false; selectedMinute = 30; updateHour(5, false)
                        }, modifier = Modifier.weight(1f))
                        PresetChip(label = "06:30 ص", onClick = {
                            hour12 = 6; isPm = false; selectedMinute = 30; updateHour(6, false)
                        }, modifier = Modifier.weight(1f))
                        PresetChip(label = "07:30 ص", onClick = {
                            hour12 = 7; isPm = false; selectedMinute = 30; updateHour(7, false)
                        }, modifier = Modifier.weight(1f))
                    } else {
                        PresetChip(label = "04:30 م", onClick = {
                            hour12 = 4; isPm = true; selectedMinute = 30; updateHour(4, true)
                        }, modifier = Modifier.weight(1f))
                        PresetChip(label = "05:30 م", onClick = {
                            hour12 = 5; isPm = true; selectedMinute = 30; updateHour(5, true)
                        }, modifier = Modifier.weight(1f))
                        PresetChip(label = "07:00 م", onClick = {
                            hour12 = 7; isPm = true; selectedMinute = 0; updateHour(7, true)
                        }, modifier = Modifier.weight(1f))
                    }
                }

                // Steppers for Hour and Minute
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Hour Stepper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "الساعة", style = MaterialTheme.typography.bodyMedium, color = IvoryWhite)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StepperButton(text = "-", onClick = {
                                hour12 = if (hour12 <= 1) 12 else hour12 - 1
                                updateHour(hour12, isPm)
                            })
                            Text(
                                text = "$hour12",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = IvoryWhite,
                                modifier = Modifier.width(30.dp),
                                textAlign = TextAlign.Center
                            )
                            StepperButton(text = "+", onClick = {
                                hour12 = if (hour12 >= 12) 1 else hour12 + 1
                                updateHour(hour12, isPm)
                            })
                        }
                    }

                    // Minute Stepper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "الدقيقة", style = MaterialTheme.typography.bodyMedium, color = IvoryWhite)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StepperButton(text = "-5", onClick = {
                                selectedMinute = (selectedMinute - 5 + 60) % 60
                            })
                            Text(
                                text = String.format(Locale.US, "%02d", selectedMinute),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = IvoryWhite,
                                modifier = Modifier.width(30.dp),
                                textAlign = TextAlign.Center
                            )
                            StepperButton(text = "+5", onClick = {
                                selectedMinute = (selectedMinute + 5) % 60
                            })
                        }
                    }

                    // AM / PM Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "الفترة", style = MaterialTheme.typography.bodyMedium, color = IvoryWhite)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AmPmButton(label = "صباحاً (ص)", isSelected = !isPm, onClick = {
                                isPm = false
                                updateHour(hour12, false)
                            })
                            AmPmButton(label = "مساءً (م)", isSelected = isPm, onClick = {
                                isPm = true
                                updateHour(hour12, true)
                            })
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IvoryMuted),
                        border = borderStroke(1.dp, GlassBorder.copy(alpha = 0.4f))
                    ) {
                        Text("إلغاء")
                    }

                    Button(
                        onClick = {
                            updateHour(hour12, isPm)
                            onConfirm(selectedHour, selectedMinute)
                        },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldLight,
                            contentColor = MidnightNavyDark
                        )
                    ) {
                        Text("تأكيد وحفظ 💾", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MidnightNavySurface)
            .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = SoftGoldBright
        )
    }
}

@Composable
private fun StepperButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MidnightNavySurface)
            .border(1.dp, EmeraldLight.copy(alpha = 0.3f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = EmeraldLight
        )
    }
}

@Composable
private fun AmPmButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) EmeraldDark else MidnightNavySurface)
            .border(
                1.dp,
                if (isSelected) EmeraldLight else GlassBorder.copy(alpha = 0.3f),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) EmeraldLight else TextDim
        )
    }
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) =
    androidx.compose.foundation.BorderStroke(width, color)
