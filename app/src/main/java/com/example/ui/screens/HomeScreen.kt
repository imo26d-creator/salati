package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.PrayerType
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AtmosphereTime
import com.example.ui.viewmodel.NoorUiState

@Composable
fun HomeScreen(
    uiState: NoorUiState,
    onNavigateToQibla: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToQuran: () -> Unit,
    onNavigateToPrayers: () -> Unit,
    onToggleMosqueMode: () -> Unit,
    onShowWidgetsPreview: () -> Unit,
    onChecklistToggle: (String) -> Unit,
    onIncrementDhikr: () -> Unit,
    onOpenSettings: () -> Unit,
    onToggleListenAdhanEarly: (PrayerType) -> Unit = {},
    onSetPreAlertMinutes: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showPrepareDialog by remember { mutableStateOf(false) }
    var showPreAlertDialog by remember { mutableStateOf(false) }

    val backgroundGradient = when (uiState.atmosphere) {
        AtmosphereTime.FAJR -> AtmosphereGradients.FajrGradient
        AtmosphereTime.MORNING -> AtmosphereGradients.MorningGradient
        AtmosphereTime.DHUHR -> AtmosphereGradients.DhuhrGradient
        AtmosphereTime.ASR -> AtmosphereGradients.AsrGradient
        AtmosphereTime.MAGHRIB -> AtmosphereGradients.MaghribGradient
        AtmosphereTime.ISHA -> AtmosphereGradients.IshaGradient
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .testTag("home_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top App Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = uiState.timeOfDayGreeting,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = IvoryWhite
                        )
                        Text(
                            text = uiState.gregorianDateFormatted,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = IvoryMuted
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassIconButton(
                            icon = Icons.Default.Mosque,
                            contentDescription = "وضع المسجد",
                            onClick = onToggleMosqueMode,
                            tint = if (uiState.isMosqueMode) EmeraldLight else SoftGold
                        )

                        GlassIconButton(
                            icon = Icons.Default.Settings,
                            contentDescription = "الإعدادات",
                            onClick = onOpenSettings,
                            tint = SoftGold
                        )
                    }
                }
            }

            // Hero Next Prayer Countdown
            item {
                PrayerCountdownHero(
                    uiState = uiState,
                    onPrepareClick = { showPrepareDialog = true },
                    onToggleListenAdhanEarly = onToggleListenAdhanEarly,
                    onOpenPreAlertSettings = { showPreAlertDialog = true }
                )
            }

            // Prayers Timeline (5 daily prayers checkmark row)
            item {
                PrayersTimeline(
                    todayPrayers = uiState.todayPrayerTimes,
                    prayerLogs = uiState.todayPrayerLogs,
                    modifier = Modifier.clickable(onClick = onNavigateToPrayers)
                )
            }

            // Quick Access Shortcut Grid (Qibla, Tasbih, Quran, Widgets)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionTile(
                        title = "القبلة",
                        icon = Icons.Default.Explore,
                        accentColor = EmeraldPrimary,
                        onClick = onNavigateToQibla,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        title = "السبحة",
                        icon = Icons.Default.Fingerprint,
                        accentColor = SoftGold,
                        onClick = onNavigateToTasbih,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        title = "المصحف",
                        icon = Icons.Default.MenuBook,
                        accentColor = EmeraldLight,
                        onClick = onNavigateToQuran,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        title = "الويدجت",
                        icon = Icons.Default.Widgets,
                        accentColor = SoftGoldBright,
                        onClick = onShowWidgetsPreview,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Daily Companion Card ("يومك مع الله")
            item {
                DailyCompanionCard(
                    uiState = uiState,
                    onChecklistToggle = onChecklistToggle
                )
            }

            // Daily Dhikr Interactive Card
            item {
                DailyDhikrCard(
                    dhikrText = uiState.randomOpeningDhikr.ifEmpty { "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ" },
                    tasbihCount = uiState.tasbihCount,
                    onIncrement = onIncrementDhikr
                )
            }

            // Daily Hadith & Ayah Reflections
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
                    borderColor = SoftGold.copy(alpha = 0.3f)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FormatQuote, contentDescription = null, tint = SoftGold)
                            Text(
                                text = "حديث اليوم",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.dailyHadith.matn,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = IvoryWhite
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${uiState.dailyHadith.narrator} — ${uiState.dailyHadith.source}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }
                }
            }

            // Daily Sunnah & Good Deed Proposal ("رفيق اليوم")
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
                    borderColor = EmeraldPrimary.copy(alpha = 0.3f)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, tint = EmeraldLight)
                                Text(
                                    text = "رفيق اليوم (سُنّة مقترحة)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = EmeraldLight
                                )
                            }
                            GlassTag(text = uiState.dailyDeed.tag, accentColor = EmeraldPrimary, textColor = EmeraldLight)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.dailyDeed.title,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )

                        Text(
                            text = uiState.dailyDeed.description,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = IvoryMuted
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "الأجر: ${uiState.dailyDeed.rewardText}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = SoftGoldBright
                        )
                    }
                }
            }
        }
    }

    // "استعد للصلاة" Preparation dialog with Wudu steps and Dua
    if (showPrepareDialog) {
        AlertDialog(
            onDismissRequest = { showPrepareDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = EmeraldLight)
                    Text(
                        text = "الاستعداد للصلاة والطهارة 🤍",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "١. الوضوء وإسباغه بالنية الخالصة لله تعالى.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = IvoryWhite
                    )
                    Text(
                        text = "٢. دعاء ما بعد الوضوء: «أَشْهَدُ أَنْ لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّداً عَبْدُهُ وَرَسُولُهُ، اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ».",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGoldBright
                    )
                    Text(
                        text = "٣. المشي إلى المسجد أو التوجه لمكان الصلاة بسكينة ووقار.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = IvoryWhite
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrepareDialog = false }) {
                    Text(text = "تقبل الله طاعتكم", color = EmeraldLight)
                }
            }
        )
    }

    // "التنبيه وسماع الأذان قبل وقته" Pre-prayer early alert config dialog
    if (showPreAlertDialog && uiState.nextPrayer != null) {
        PreAdhanAlertConfigDialog(
            currentMinutes = uiState.prePrayerAlertMinutes,
            isAzanPlaying = uiState.isAzanAudioPlaying,
            nextPrayerName = uiState.nextPrayer.type.arabicName,
            onSetMinutes = onSetPreAlertMinutes,
            onToggleListenEarly = { onToggleListenAdhanEarly(uiState.nextPrayer.type) },
            onDismiss = { showPreAlertDialog = false }
        )
    }
}

@Composable
fun QuickActionTile(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MidnightNavyCard.copy(alpha = 0.8f))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = IvoryWhite
            )
        }
    }
}
