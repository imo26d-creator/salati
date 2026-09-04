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
import com.example.data.calculator.HijriCalendarHelper
import com.example.data.model.CalculationMethod
import com.example.data.model.DuaItem
import com.example.data.model.JuristicMethod
import com.example.data.model.MuezzinVoice
import com.example.data.model.PrayerType
import com.example.data.repository.DuasDataRepository
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState
import java.util.Calendar


@Composable
fun DuasLibrarySection(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("الكل") }
    val context = LocalContext.current

    val allCats = listOf("الكل") + DuasDataRepository.categories
    val filtered = DuasDataRepository.allDuas.filter { dua ->
        (selectedCategory == "الكل" || dua.category == selectedCategory) &&
                (searchQuery.isEmpty() || dua.title.contains(searchQuery) || dua.arabicText.contains(searchQuery))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("duas_library_section")
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("ابحث في الأدعية القرآنية والنبوية...", color = TextMuted) },
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

        Spacer(modifier = Modifier.height(14.dp))

        // Category pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allCats.take(4).forEach { cat ->
                val isSel = selectedCategory == cat
                StatusChip(
                    label = cat,
                    isSelected = isSel,
                    selectedColor = if (isSel) SoftGold else TextMuted,
                    onClick = { selectedCategory = cat }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Duas list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { dua ->
                DuaItemCard(
                    dua = dua,
                    onShare = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "« ${dua.arabicText} » [${dua.source}] — تطبيق «نُور»")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "مشاركة الدعاء"))
                    }
                )
            }
        }
    }
}

@Composable
fun DuaItemCard(
    dua: DuaItem,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
        borderColor = GlassBorder.copy(alpha = 0.2f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dua.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
                GlassTag(text = dua.category, accentColor = EmeraldPrimary, textColor = EmeraldLight)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dua.arabicText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = IvoryWhite
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = dua.translation,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المصدر: ${dua.source}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = SoftGold
                )

                IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = IvoryMuted, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun RamadanHubView(
    uiState: NoorUiState,
    modifier: Modifier = Modifier
) {
    var cashAmount by remember { mutableStateOf("") }
    var goldGrams by remember { mutableStateOf("") }
    var calculatedZakat by remember { mutableStateOf(0.0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ramadan_hub_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fasting Hero Card (Imsak & Iftar)
        val fajrTime = uiState.todayPrayerTimes.find { it.type == com.example.data.model.PrayerType.FAJR }?.timeFormatted ?: "04:25"
        val maghribTime = uiState.todayPrayerTimes.find { it.type == com.example.data.model.PrayerType.MAGHRIB }?.timeFormatted ?: "18:25"

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MidnightNavyCard.copy(alpha = 0.85f),
            borderColor = SoftGold.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مواقيت الصيام اليوم 🌙",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                    GlassTag(text = "صيام النوافل / رمضان", accentColor = EmeraldPrimary, textColor = EmeraldLight)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "الإمساك (الفجر)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = fajrTime, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = IvoryWhite)
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(GlassBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "الإفطار (المغرب)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = maghribTime, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
                    }
                }
            }
        }

        // Zakat Calculator
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
            borderColor = EmeraldPrimary.copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = EmeraldLight)
                    Text(
                        text = "حاسبة زكاة المال والذهب 💰",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = cashAmount,
                    onValueChange = {
                        cashAmount = it
                        val cash = it.toDoubleOrNull() ?: 0.0
                        val gold = goldGrams.toDoubleOrNull() ?: 0.0
                        calculatedZakat = (cash + (gold * 280)) * 0.025
                    },
                    label = { Text("المبلغ النقدي الفائض (ريال / درهم / دولار)", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldLight,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = IvoryWhite,
                        unfocusedTextColor = IvoryWhite
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = goldGrams,
                    onValueChange = {
                        goldGrams = it
                        val cash = cashAmount.toDoubleOrNull() ?: 0.0
                        val gold = it.toDoubleOrNull() ?: 0.0
                        calculatedZakat = (cash + (gold * 280)) * 0.025
                    },
                    label = { Text("وزن الذهب المدخر بالجرامات (إن وجد)", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldLight,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = IvoryWhite,
                        unfocusedTextColor = IvoryWhite
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldDark.copy(alpha = 0.4f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "مقدار الزكاة الواجبة (2.5%):", style = MaterialTheme.typography.bodyMedium, color = IvoryWhite)
                    Text(
                        text = String.format("%.2f", calculatedZakat),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldLight
                    )
                }
            }
        }
    }
}

@Composable
fun MosqueModeDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyDark,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Mosque, contentDescription = null, tint = EmeraldLight)
                Text(text = "وضع المسجد الصامت 🕌", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = IvoryWhite)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MidnightNavySurface)
                        .border(2.dp, EmeraldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.VolumeOff, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(38.dp))
                }

                Text(
                    text = "وضع الهدوء والسكينة نشط الآن",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )

                Text(
                    text = "تم كتم كافة التنبيهات والأصوات تلقائيًا للخشوع في المسجد وتجنب إزعاج المصلين.",
                    style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center),
                    color = TextMuted
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
            ) {
                Text(text = "إلغاء وضع المسجد", color = EmeraldLight)
            }
        }
    )
}

@Composable
fun WidgetsPreviewDialog(
    uiState: NoorUiState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MidnightNavyCard,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "معاينة ويدجت الشاشة الرئيسية 📱", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "نماذج الويدجت التفاعلية التي يمكنك إضافتها لشاشة هاتفك:", style = MaterialTheme.typography.bodySmall, color = TextMuted)

                // Widget 1: Next Prayer
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavyDark,
                    borderColor = SoftGold.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "الصلاة القادمة", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "${uiState.nextPrayer?.type?.arabicName ?: "العصر"} • ${uiState.nextPrayer?.timeFormatted ?: "15:30"}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                            Text(text = "متبقي: ${uiState.countdownText}", style = MaterialTheme.typography.bodySmall, color = EmeraldLight)
                        }
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = SoftGold, modifier = Modifier.size(32.dp))
                    }
                }

                // Widget 2: Daily Dhikr
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavyDark,
                    borderColor = EmeraldPrimary.copy(alpha = 0.5f)
                ) {
                    Column {
                        Text(text = "ذكر اليوم 🤍", style = MaterialTheme.typography.labelSmall, color = EmeraldLight)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.randomOpeningDhikr.ifEmpty { "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ" },
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = IvoryWhite
                        )
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

@Composable
fun VolumeControlSlider(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val volumePercent = (volume * 100).toInt()
    val volumeIcon = when {
        volume <= 0.05f -> Icons.Default.VolumeMute
        volume < 0.5f -> Icons.Default.VolumeDown
        else -> Icons.Default.VolumeUp
    }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MidnightNavySurface.copy(alpha = 0.9f),
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
                        imageVector = volumeIcon,
                        contentDescription = "مستوى الصوت",
                        tint = SoftGoldBright,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "مستوى صوت الأذان والتنبيه",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }

                GlassTag(
                    text = "$volumePercent%",
                    accentColor = SoftGold,
                    textColor = SoftGoldBright
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = volume,
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
                listOf(0.25f to "25%", 0.50f to "50%", 0.75f to "75%", 1.0f to "100%").forEach { (v, label) ->
                    TextButton(
                        onClick = { onVolumeChange(v) },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (kotlin.math.abs(volume - v) < 0.05f) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (kotlin.math.abs(volume - v) < 0.05f) SoftGoldBright else TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MuezzinSelectionItem(
    muezzin: MuezzinVoice,
    isSelected: Boolean,
    isPlaying: Boolean,
    onSelect: () -> Unit,
    onTogglePreview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) MidnightNavySurface.copy(alpha = 0.95f) else MidnightNavyCard.copy(alpha = 0.6f)
            )
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) SoftGoldBright else GlassBorder.copy(alpha = 0.2f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onSelect)
            .padding(12.dp)
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
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = SoftGoldBright,
                        unselectedColor = TextMuted
                    )
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = muezzin.titleArabic,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 14.sp
                            ),
                            color = if (isSelected) SoftGoldBright else IvoryWhite
                        )
                    }

                    Text(
                        text = muezzin.descriptionArabic,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = IvoryMuted
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "📍 ${muezzin.location}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = EmeraldLight
                    )
                }
            }

            // Preview Button
            Button(
                onClick = onTogglePreview,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlaying) EmeraldPrimary else MidnightNavySurface
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "إيقاف" else "استماع",
                    tint = if (isPlaying) IvoryWhite else SoftGoldBright,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isPlaying) "إيقاف" else "استماع",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isPlaying) IvoryWhite else SoftGoldBright
                )
            }
        }
    }
}

@Composable
fun MuezzinSelectionDialog(
    uiState: NoorUiState,
    onSelectMuezzin: (MuezzinVoice) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onToggleMuezzinPreview: (MuezzinVoice) -> Unit,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = SoftGoldBright
                    )
                    Text(
                        text = "صوت الأذان والمؤذن 🕌",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    VolumeControlSlider(
                        volume = uiState.azanVolume,
                        onVolumeChange = onVolumeChange
                    )
                }

                item {
                    Text(
                        text = "اختر صوت المؤذن المفضل للأذان والتنبيهات:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SoftGold
                    )
                }

                items(MuezzinVoice.values()) { muezzin ->
                    val isSelected = uiState.selectedMuezzin == muezzin
                    val isPlaying = uiState.isAzanAudioPlaying && uiState.playingMuezzin == muezzin

                    MuezzinSelectionItem(
                        muezzin = muezzin,
                        isSelected = isSelected,
                        isPlaying = isPlaying,
                        onSelect = { onSelectMuezzin(muezzin) },
                        onTogglePreview = { onToggleMuezzinPreview(muezzin) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
            ) {
                Text(text = "حفظ واختيار", color = EmeraldLight)
            }
        }
    )
}

@Composable
fun SettingsSheetDialog(
    uiState: NoorUiState,
    onMethodChange: (CalculationMethod) -> Unit,
    onJuristicChange: (JuristicMethod) -> Unit,
    onPreAlertChange: (Int) -> Unit,
    onSelectMuezzin: (MuezzinVoice) -> Unit = {},
    onVolumeChange: (Float) -> Unit = {},
    onToggleMuezzinPreview: (MuezzinVoice) -> Unit = {},
    onPlayToneTest: () -> Unit,
    onToggleAutoPhoneTime: (Boolean) -> Unit = {},
    onSyncWithPhoneNow: () -> Unit = {},
    onToggle24HourFormat: (Boolean) -> Unit = {},
    onSetPrayerManualOffset: (PrayerType, Int) -> Unit = { _, _ -> },
    onResetPrayerManualOffsets: () -> Unit = {},
    onAdjustManualTimeMinutes: (Int) -> Unit = {},
    onSetManualTimeOffset: (Int) -> Unit = {},
    onSetSpecificCustomTime: (Int, Int) -> Unit = { _, _ -> },
    onSetHijriDateAdjustment: (Int) -> Unit = {},
    onSelectCustomDate: (Calendar) -> Unit = {},
    onSetSpecificCustomDate: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onResetDateToToday: () -> Unit = {},
    onResetAllTimeAndDateSettings: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var showManualOffsets by remember { mutableStateOf(false) }

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
                    text = "إعدادات الأذان والمواقيت والوقت ⚙️",
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
                    .height(480.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 0: Full Time & Date Settings & Adjustments (تعديل الوقت والتاريخ)
                item {
                    TimeDateSettingsCard(
                        uiState = uiState,
                        onToggleAutoPhoneTime = onToggleAutoPhoneTime,
                        onSyncWithPhoneNow = onSyncWithPhoneNow,
                        onToggle24HourFormat = onToggle24HourFormat,
                        onAdjustManualTimeMinutes = onAdjustManualTimeMinutes,
                        onSetManualTimeOffset = onSetManualTimeOffset,
                        onSetSpecificCustomTime = onSetSpecificCustomTime,
                        onSetHijriDateAdjustment = onSetHijriDateAdjustment,
                        onSelectCustomDate = onSelectCustomDate,
                        onSetSpecificCustomDate = onSetSpecificCustomDate,
                        onResetDateToToday = onResetDateToToday,
                        onResetAllTimeAndDateSettings = onResetAllTimeAndDateSettings
                    )
                }

                // Section 0.5: Local Mosque Fine-Tuning (± دقيقة لكل صلاة)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MidnightNavySurface.copy(alpha = 0.95f))
                            .border(1.dp, SoftGold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ضبط مواقيت المسجد المحلي (± دقائق)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                            TextButton(onClick = onResetPrayerManualOffsets, contentPadding = PaddingValues(0.dp)) {
                                Text("تصفير الإزاحات", fontSize = 11.sp, color = IvoryMuted)
                            }
                        }

                        listOf(
                            PrayerType.FAJR,
                            PrayerType.DHUHR,
                            PrayerType.ASR,
                            PrayerType.MAGHRIB,
                            PrayerType.ISHA
                        ).forEach { p ->
                            val off = uiState.prayerManualOffsets[p] ?: 0
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(p.arabicName, fontSize = 12.sp, color = IvoryWhite)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { onSetPrayerManualOffset(p, off - 1) }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.RemoveCircleOutline, null, tint = SoftGold, modifier = Modifier.size(18.dp))
                                    }
                                    Text(
                                        text = if (off > 0) "+$off د" else "$off د",
                                        fontSize = 11.sp,
                                        color = if (off != 0) EmeraldLight else IvoryMuted,
                                        modifier = Modifier.width(36.dp),
                                        textAlign = TextAlign.Center
                                    )
                                    IconButton(onClick = { onSetPrayerManualOffset(p, off + 1) }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.AddCircleOutline, null, tint = SoftGoldBright, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 1: Volume Control
                item {
                    VolumeControlSlider(
                        volume = uiState.azanVolume,
                        onVolumeChange = onVolumeChange
                    )
                }

                // Section 2: Muezzin Voice selection
                item {
                    Text(
                        text = "صوت المؤذن المعتمد للأذان",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MuezzinVoice.values().forEach { muezzin ->
                            val isSelected = uiState.selectedMuezzin == muezzin
                            val isPlaying = uiState.isAzanAudioPlaying && uiState.playingMuezzin == muezzin

                            MuezzinSelectionItem(
                                muezzin = muezzin,
                                isSelected = isSelected,
                                isPlaying = isPlaying,
                                onSelect = { onSelectMuezzin(muezzin) },
                                onTogglePreview = { onToggleMuezzinPreview(muezzin) }
                            )
                        }
                    }
                }

                // Section 3: Astronomical calculation method
                item {
                    Text(
                        text = "طريقة حساب المواقيت الفلكية",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CalculationMethod.values().forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (uiState.calculationMethod == method) EmeraldDark.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable { onMethodChange(method) }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = method.arabicName, style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                            RadioButton(
                                selected = uiState.calculationMethod == method,
                                onClick = { onMethodChange(method) },
                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldLight)
                            )
                        }
                    }
                }

                // Section 4: Juristic Method
                item {
                    Text(
                        text = "المذهب الفقهي لحساب وقت العصر",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusChip(
                            label = "الجمهور (الشافعي، المالكي، الحنبلي)",
                            isSelected = uiState.juristicMethod == JuristicMethod.STANDARD,
                            selectedColor = EmeraldPrimary,
                            onClick = { onJuristicChange(JuristicMethod.STANDARD) }
                        )
                        StatusChip(
                            label = "الحنفي",
                            isSelected = uiState.juristicMethod == JuristicMethod.HANAFI,
                            selectedColor = EmeraldPrimary,
                            onClick = { onJuristicChange(JuristicMethod.HANAFI) }
                        )
                    }
                }

                // Section 5: Pre-Prayer Alert
                item {
                    Text(
                        text = "التنبيه المسبق قبل موعد الصلاة",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 5, 10, 15).forEach { min ->
                            StatusChip(
                                label = "$min دقائق",
                                isSelected = uiState.prePrayerAlertMinutes == min,
                                selectedColor = SoftGold,
                                onClick = { onPreAlertChange(min) }
                            )
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
                Text(text = "حفظ وإغلاق", color = EmeraldLight)
            }
        }
    )
}

