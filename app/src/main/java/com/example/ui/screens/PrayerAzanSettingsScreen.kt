package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzanAlertType
import com.example.data.model.MuezzinVoice
import com.example.data.model.PrayerAzanConfig
import com.example.data.model.PrayerType
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassTag
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerAzanSettingsScreen(
    uiState: NoorUiState,
    onBack: () -> Unit,
    onSetPrayerMuezzin: (PrayerType, MuezzinVoice) -> Unit,
    onSetPrayerVolume: (PrayerType, Float) -> Unit,
    onSetPrayerAlertType: (PrayerType, AzanAlertType) -> Unit,
    onTogglePrayerEnabled: (PrayerType) -> Unit,
    onApplyMuezzinToAll: (MuezzinVoice) -> Unit,
    onApplyVolumeToAll: (Float) -> Unit,
    onTogglePrayerPreview: (PrayerType) -> Unit,
    onStopAudioPreview: () -> Unit,
    onPreAlertChange: (Int) -> Unit,
    onSendTestNotification: (PrayerType) -> Unit = {},
    onToggleAutoPhoneTime: (Boolean) -> Unit = {},
    onSyncWithPhoneNow: () -> Unit = {},
    onToggle24HourFormat: (Boolean) -> Unit = {},
    onSetPrayerManualOffset: (PrayerType, Int) -> Unit = { _, _ -> },
    onResetPrayerManualOffsets: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilterPrayer by remember { mutableStateOf<PrayerType?>(null) }
    var showBatchMuezzinPicker by remember { mutableStateOf(false) }
    var activeMuezzinPickerPrayer by remember { mutableStateOf<PrayerType?>(null) }
    var showManualOffsetsSection by remember { mutableStateOf(false) }

    val prayersList = remember {
        listOf(
            PrayerType.FAJR,
            PrayerType.SUNRISE,
            PrayerType.DHUHR,
            PrayerType.ASR,
            PrayerType.MAGHRIB,
            PrayerType.ISHA
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "إعدادات الأذان والوقت",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = IvoryWhite
                        )
                        Text(
                            text = "مزامنة التوقيت مع هاتفك وتخصيص الأذان والتكبيرات",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = SoftGoldBright
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = SoftGoldBright
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MidnightNavyDark.copy(alpha = 0.95f)
                ),
                actions = {
                    if (uiState.isAzanAudioPlaying) {
                        IconButton(onClick = onStopAudioPreview) {
                            Icon(
                                imageVector = Icons.Default.StopCircle,
                                contentDescription = "إيقاف الصوت",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            )
        },
        containerColor = MidnightNavyDark,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            // Section 0: Device Time & Automatic Phone Synchronization (Directly from phone)
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavySurface.copy(alpha = 0.95f),
                    borderColor = SoftGoldBright.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header: Phone Time Live Display
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldDark.copy(alpha = 0.6f))
                                        .border(1.dp, SoftGoldBright, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = SoftGoldBright,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "توقيت الهاتف الحالي والمزامنة",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        color = IvoryWhite
                                    )
                                    Text(
                                        text = if (uiState.deviceCurrentTimeFormatted.isNotEmpty())
                                            "الساعة الآن: ${uiState.deviceCurrentTimeFormatted}"
                                        else
                                            "مربوط مباشرة بساعة هاتفك الذكي",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        ),
                                        color = SoftGoldBright
                                    )
                                }
                            }

                            GlassTag(
                                text = "مباشر ⏱️",
                                accentColor = EmeraldPrimary,
                                textColor = EmeraldLight
                            )
                        }

                        // Timezone and sync status row
                        if (uiState.deviceTimeZoneName.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MidnightNavyCard.copy(alpha = 0.6f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "المنطقة الزمنية للجهاز:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = IvoryMuted
                                )
                                Text(
                                    text = uiState.deviceTimeZoneName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    color = SoftGoldBright
                                )
                            }
                        }

                        // Confirmation notification if user just pressed sync
                        AnimatedVisibility(visible = uiState.timeSyncSuccessMessage != null) {
                            uiState.timeSyncSuccessMessage?.let { msg ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(EmeraldDark.copy(alpha = 0.8f))
                                        .border(1.dp, EmeraldLight, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = EmeraldLight,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = msg,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            ),
                                            color = IvoryWhite
                                        )
                                    }
                                }
                            }
                        }

                        Divider(color = GlassBorder.copy(alpha = 0.3f), thickness = 0.8.dp)

                        // Toggle 1: Auto Phone Time Sync Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "المزامنة التلقائية حسب وقت الهاتف مباشرة",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = IvoryWhite
                                )
                                Text(
                                    text = "تحديث مواقيت الصلاة والعد التنازلي لحظياً وفق ساعة الهاتف",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
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

                        // Toggle 2: 12-Hour vs 24-Hour Format Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "تنسيق عرض الوقت",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = IvoryWhite
                                )
                                Text(
                                    text = if (uiState.is24HourFormat) "صيغة 24 ساعة (مثال: 15:30)" else "صيغة 12 ساعة (مثال: 03:30 م)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = SoftGoldBright
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

                        // Action Buttons: Instant Sync & Manual Adjustments Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onSyncWithPhoneNow,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = IvoryWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "مزامنة فورية الآن 🔄",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    ),
                                    color = IvoryWhite
                                )
                            }

                            OutlinedButton(
                                onClick = { showManualOffsetsSection = !showManualOffsetsSection },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGoldBright),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (showManualOffsetsSection) Icons.Default.ExpandLess else Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = SoftGoldBright,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (showManualOffsetsSection) "إخفاء الضبط اليدوي" else "ضبط المسجد (±دقائق)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                                    color = SoftGoldBright
                                )
                            }
                        }

                        // Expandable Manual Offsets Section (ضبط دقائق الصلاة حسب المسجد المحلي)
                        AnimatedVisibility(visible = showManualOffsetsSection) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MidnightNavyCard.copy(alpha = 0.9f))
                                    .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "تعديل دقائق الصلاة يدوياً (للتطابق مع مسجد حيك):",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = SoftGoldBright
                                    )
                                    TextButton(
                                        onClick = onResetPrayerManualOffsets,
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("إعادة ضبط (0)", fontSize = 11.sp, color = IvoryMuted)
                                    }
                                }

                                prayersList.filter { it != PrayerType.SUNRISE }.forEach { prayer ->
                                    val offset = uiState.prayerManualOffsets[prayer] ?: 0
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MidnightNavySurface.copy(alpha = 0.5f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "صلاة ${prayer.arabicName}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = IvoryWhite
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            IconButton(
                                                onClick = { onSetPrayerManualOffset(prayer, offset - 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.RemoveCircleOutline,
                                                    contentDescription = "تقليل دقيقة",
                                                    tint = SoftGold,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            Text(
                                                text = if (offset > 0) "+$offset د" else if (offset < 0) "$offset د" else "0 د",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                ),
                                                color = if (offset != 0) EmeraldLight else IvoryMuted,
                                                modifier = Modifier.width(36.dp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )

                                            IconButton(
                                                onClick = { onSetPrayerManualOffset(prayer, offset + 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AddCircleOutline,
                                                    contentDescription = "زيادة دقيقة",
                                                    tint = SoftGoldBright,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Section 1: Hero Banner & Quick Unified Actions
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavySurface.copy(alpha = 0.9f),
                    borderColor = SoftGold.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    SoftGold.copy(alpha = 0.3f),
                                                    EmeraldDark.copy(alpha = 0.6f)
                                                )
                                            )
                                        )
                                        .border(1.dp, SoftGoldBright, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Campaign,
                                        contentDescription = null,
                                        tint = SoftGoldBright,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "التحكم الشامل والسريع",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        color = IvoryWhite
                                    )
                                    Text(
                                        text = "تطبيق إعداد موحد أو التخصيص لكل صلاة بالأسفل",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = IvoryMuted
                                    )
                                }
                            }

                            GlassTag(
                                text = "مستقل لكل صلاة",
                                accentColor = EmeraldPrimary,
                                textColor = EmeraldLight
                            )
                        }

                        Divider(color = GlassBorder.copy(alpha = 0.3f), thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showBatchMuezzinPicker = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MidnightNavyCard),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonPin,
                                    contentDescription = null,
                                    tint = SoftGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "مؤذن موحد للكل",
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                    color = SoftGoldBright
                                )
                            }

                            Button(
                                onClick = { onApplyVolumeToAll(0.85f) },
                                colors = ButtonDefaults.buttonColors(containerColor = MidnightNavyCard),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = EmeraldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "صوت موحد (85%)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                    color = EmeraldLight
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Prayer Quick Filter Tabs
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "اختر صلاة لتعديل إعداداتها أو استعرض كافة الصلوات:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SoftGold
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedFilterPrayer == null,
                                onClick = { selectedFilterPrayer = null },
                                label = { Text("جميع الصلوات (6)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = IvoryWhite,
                                    containerColor = MidnightNavyCard,
                                    labelColor = IvoryMuted
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedFilterPrayer == null,
                                    borderColor = if (selectedFilterPrayer == null) SoftGoldBright else GlassBorder.copy(alpha = 0.3f)
                                )
                            )
                        }

                        items(prayersList) { prayer ->
                            val isSelected = selectedFilterPrayer == prayer
                            val config = uiState.prayerAzanConfigs[prayer]
                            val isPlayingThis = uiState.isAzanAudioPlaying && uiState.playingPrayer == prayer

                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilterPrayer = if (isSelected) null else prayer },
                                label = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(prayer.arabicName)
                                        if (isPlayingThis) {
                                            Icon(
                                                imageVector = Icons.Default.GraphicEq,
                                                contentDescription = null,
                                                tint = SoftGoldBright,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "${((config?.volume ?: 0.85f) * 100).toInt()}%",
                                                fontSize = 10.sp,
                                                color = if (isSelected) IvoryWhite else SoftGold
                                            )
                                        }
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = IvoryWhite,
                                    containerColor = MidnightNavyCard,
                                    labelColor = IvoryMuted
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) SoftGoldBright else GlassBorder.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }

            // Section 3: List of Individual Prayer Azan Cards
            val displayedPrayers = if (selectedFilterPrayer != null) {
                prayersList.filter { it == selectedFilterPrayer }
            } else {
                prayersList
            }

            items(displayedPrayers) { prayer ->
                val config = uiState.prayerAzanConfigs[prayer] ?: PrayerAzanConfig(prayerType = prayer)
                val isPlayingThis = uiState.isAzanAudioPlaying && uiState.playingPrayer == prayer

                PrayerAzanConfigCard(
                    prayer = prayer,
                    config = config,
                    isPlaying = isPlayingThis,
                    onToggleEnabled = { onTogglePrayerEnabled(prayer) },
                    onOpenMuezzinPicker = { activeMuezzinPickerPrayer = prayer },
                    onVolumeChange = { onSetPrayerVolume(prayer, it) },
                    onAlertTypeChange = { onSetPrayerAlertType(prayer, it) },
                    onTogglePreview = { onTogglePrayerPreview(prayer) },
                    onSendTestNotification = { onSendTestNotification(prayer) }
                )
            }

            // Section 4: Pre-Prayer Alert Settings (التنبيه المسبق)
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavySurface.copy(alpha = 0.85f),
                    borderColor = GlassBorder.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = SoftGoldBright,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "التنبيه المسبق قبل دخول وقت الصلاة",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = IvoryWhite
                            )
                        }

                        Text(
                            text = "إصدار تكبيرات الأذان الشرعية أو صوت مقرئ للتنبيه قبل رفع الأذان (خالٍ تماماً من الموسيقى):",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = IvoryMuted
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                0 to "معطل",
                                3 to "3 دقائق",
                                5 to "5 دقائق",
                                10 to "10 دقائق",
                                15 to "15 دقيقة"
                            ).forEach { (mins, label) ->
                                val isSelected = uiState.prePrayerAlertMinutes == mins
                                Button(
                                    onClick = { onPreAlertChange(mins) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) EmeraldPrimary else MidnightNavyCard
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        ),
                                        color = if (isSelected) IvoryWhite else TextMuted
                                    )
                                }
                            }
                        }

                        // Preview pure Takbeer alert button (no music)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    com.example.util.AzanSoundPlayer.playTakbeerAlert(uiState.azanVolume)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGoldBright),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = SoftGoldBright,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "سماع تكبيرات التنبيه 🕋",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = IvoryWhite
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    com.example.util.AzanSoundPlayer.playReciterAyahAlert(uiState.azanVolume)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldLight),
                                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight.copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = EmeraldLight,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "سماع صوت المقرئ 📖",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = IvoryWhite
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for picking muezzin for a specific prayer
    if (activeMuezzinPickerPrayer != null) {
        val prayer = activeMuezzinPickerPrayer!!
        val currentConfig = uiState.prayerAzanConfigs[prayer] ?: PrayerAzanConfig(prayerType = prayer)

        MuezzinPickerDialog(
            title = "اختيار مؤذن صلاة ${prayer.arabicName} 🕌",
            selectedMuezzin = currentConfig.muezzin,
            isPlaying = uiState.isAzanAudioPlaying && uiState.playingPrayer == prayer,
            playingMuezzin = uiState.playingMuezzin,
            onSelectMuezzin = { muezzin ->
                onSetPrayerMuezzin(prayer, muezzin)
            },
            onTogglePreview = { muezzin ->
                onSetPrayerMuezzin(prayer, muezzin)
                onTogglePrayerPreview(prayer)
            },
            onDismiss = { activeMuezzinPickerPrayer = null }
        )
    }

    // Dialog for applying unified muezzin to all prayers
    if (showBatchMuezzinPicker) {
        MuezzinPickerDialog(
            title = "تعيين مؤذن موحد لجميع الصلوات 🕌",
            selectedMuezzin = uiState.selectedMuezzin,
            isPlaying = uiState.isAzanAudioPlaying,
            playingMuezzin = uiState.playingMuezzin,
            onSelectMuezzin = { muezzin ->
                onApplyMuezzinToAll(muezzin)
            },
            onTogglePreview = { muezzin ->
                onApplyMuezzinToAll(muezzin)
                onTogglePrayerPreview(PrayerType.FAJR)
            },
            onDismiss = { showBatchMuezzinPicker = false }
        )
    }
}

@Composable
fun PrayerAzanConfigCard(
    prayer: PrayerType,
    config: PrayerAzanConfig,
    isPlaying: Boolean,
    onToggleEnabled: () -> Unit,
    onOpenMuezzinPicker: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onAlertTypeChange: (AzanAlertType) -> Unit,
    onTogglePreview: () -> Unit,
    onSendTestNotification: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val prayerIcon = getPrayerIcon(prayer)
    val volumePercent = (config.volume * 100).toInt()
    val volumeIcon = when {
        !config.isEnabled || config.volume <= 0.05f -> Icons.Default.VolumeMute
        config.volume < 0.5f -> Icons.Default.VolumeDown
        else -> Icons.Default.VolumeUp
    }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = if (isPlaying) MidnightNavySurface.copy(alpha = 0.98f) else MidnightNavySurface.copy(alpha = 0.85f),
        borderColor = if (isPlaying) SoftGoldBright else if (config.isEnabled) GlassBorder.copy(alpha = 0.4f) else GlassBorder.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Prayer Name, Icon, Enable Switch & Preview Button
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
                            .background(
                                if (config.isEnabled) EmeraldDark.copy(alpha = 0.6f) else MidnightNavyCard
                            )
                            .border(
                                1.dp,
                                if (config.isEnabled) EmeraldLight else GlassBorder.copy(alpha = 0.3f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = prayerIcon,
                            contentDescription = null,
                            tint = if (config.isEnabled) SoftGoldBright else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "صلاة ${prayer.arabicName}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = if (config.isEnabled) IvoryWhite else TextMuted
                            )
                            if (isPlaying) {
                                GlassTag(
                                    text = "جاري الاستماع 🎵",
                                    accentColor = SoftGoldBright,
                                    textColor = SoftGoldBright
                                )
                            }
                        }
                        Text(
                            text = prayer.description,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = IvoryMuted
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Preview Button
                    IconButton(
                        onClick = onTogglePreview,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (isPlaying) EmeraldPrimary else MidnightNavyCard
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "إيقاف" else "تجربة الصوت",
                            tint = if (isPlaying) IvoryWhite else SoftGoldBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Enable/Disable Switch
                    Switch(
                        checked = config.isEnabled,
                        onCheckedChange = { onToggleEnabled() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SoftGoldBright,
                            checkedTrackColor = EmeraldPrimary,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = MidnightNavyCard
                        )
                    )
                }
            }

            if (config.isEnabled) {
                Divider(color = GlassBorder.copy(alpha = 0.2f), thickness = 0.8.dp)

                // Row: Selected Muezzin Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavyCard.copy(alpha = 0.8f))
                        .border(1.dp, SoftGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable(onClick = onOpenMuezzinPicker)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = SoftGoldBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "صوت المؤذن:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = SoftGold
                                )
                                Text(
                                    text = config.muezzin.titleArabic,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    ),
                                    color = IvoryWhite
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "تغيير",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "تغيير المؤذن",
                                tint = SoftGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Volume Level Section with Slider & Quick Preset Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                                imageVector = volumeIcon,
                                contentDescription = null,
                                tint = SoftGoldBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "مستوى صوت أذان ${prayer.arabicName}:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = IvoryWhite
                            )
                        }

                        Text(
                            text = "$volumePercent%",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = SoftGoldBright
                        )
                    }

                    Slider(
                        value = config.volume,
                        onValueChange = onVolumeChange,
                        valueRange = 0.0f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = SoftGoldBright,
                            activeTrackColor = SoftGold,
                            inactiveTrackColor = GlassBorder.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            0.25f to "25% (خافت)",
                            0.50f to "50% (متوسط)",
                            0.75f to "75% (واضح)",
                            1.0f to "100% (أعلى)"
                        ).forEach { (v, label) ->
                            TextButton(
                                onClick = { onVolumeChange(v) },
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (kotlin.math.abs(config.volume - v) < 0.06f) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (kotlin.math.abs(config.volume - v) < 0.06f) SoftGoldBright else TextMuted
                                )
                            }
                        }
                    }
                }

                // Alert Type Selector (أذان كامل، تكبيرات، نغمة، اهتزاز، صامت)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "نوع التنبيه عند حلول الوقت:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = SoftGold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AzanAlertType.values().forEach { alertType ->
                            val isSelected = config.alertType == alertType
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) EmeraldPrimary else MidnightNavyCard
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) SoftGoldBright else GlassBorder.copy(alpha = 0.2f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onAlertTypeChange(alertType) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = alertType.titleArabic,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 10.sp
                                    ),
                                    color = if (isSelected) IvoryWhite else IvoryMuted
                                )
                            }
                        }
                    }
                }

                // Instant Test Notification Button
                OutlinedButton(
                    onClick = onSendTestNotification,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SoftGoldBright
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = SoftGoldBright,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "إرسال إشعار تجريبي وتجربة الأذان 🔔",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = IvoryWhite
                    )
                }
            }
        }
    }
}

@Composable
fun MuezzinPickerDialog(
    title: String,
    selectedMuezzin: MuezzinVoice,
    isPlaying: Boolean,
    playingMuezzin: MuezzinVoice?,
    onSelectMuezzin: (MuezzinVoice) -> Unit,
    onTogglePreview: (MuezzinVoice) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyDark,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(MuezzinVoice.values()) { muezzin ->
                    val isSelected = selectedMuezzin == muezzin
                    val isPlayingThis = isPlaying && playingMuezzin == muezzin

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MidnightNavySurface.copy(alpha = 0.95f) else MidnightNavyCard.copy(alpha = 0.6f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) SoftGoldBright else GlassBorder.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectMuezzin(muezzin) }
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onSelectMuezzin(muezzin) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = SoftGoldBright,
                                        unselectedColor = TextMuted
                                    )
                                )

                                Column {
                                    Text(
                                        text = muezzin.titleArabic,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        ),
                                        color = if (isSelected) SoftGoldBright else IvoryWhite
                                    )
                                    Text(
                                        text = muezzin.descriptionArabic,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = IvoryMuted
                                    )
                                    Text(
                                        text = "📍 ${muezzin.location}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = EmeraldLight
                                    )
                                }
                            }

                            Button(
                                onClick = { onTogglePreview(muezzin) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPlayingThis) EmeraldPrimary else MidnightNavySurface
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlayingThis) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlayingThis) "إيقاف" else "استماع",
                                    tint = if (isPlayingThis) IvoryWhite else SoftGoldBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isPlayingThis) "إيقاف" else "تجربة",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = if (isPlayingThis) IvoryWhite else SoftGoldBright
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
            ) {
                Text(text = "اعتماد واختيار", color = EmeraldLight)
            }
        }
    )
}

private fun getPrayerIcon(prayer: PrayerType): ImageVector {
    return when (prayer) {
        PrayerType.FAJR -> Icons.Default.WbTwilight
        PrayerType.SUNRISE -> Icons.Default.WbSunny
        PrayerType.DHUHR -> Icons.Default.LightMode
        PrayerType.ASR -> Icons.Default.FilterDrama
        PrayerType.MAGHRIB -> Icons.Default.NightsStay
        PrayerType.ISHA -> Icons.Default.Bedtime
    }
}
