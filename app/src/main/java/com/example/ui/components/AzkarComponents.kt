package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.model.DuaItem
import com.example.ui.theme.*

@Composable
fun AzkarCategoryTile(
    category: DhikrCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .testTag("azkar_cat_${category.name.lowercase()}"),
        backgroundColor = if (isSelected) MidnightNavySurface else MidnightNavyCard.copy(alpha = 0.75f),
        borderColor = if (isSelected) EmeraldLight else GlassBorder.copy(alpha = 0.2f),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) EmeraldDark else MidnightNavyLight.copy(alpha = 0.5f))
                    .border(1.dp, if (isSelected) EmeraldLight else GlassBorder.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (category) {
                        DhikrCategory.MORNING -> Icons.Default.WbSunny
                        DhikrCategory.EVENING -> Icons.Default.NightsStay
                        DhikrCategory.AFTER_PRAYER -> Icons.Default.VolunteerActivism
                        DhikrCategory.SLEEP -> Icons.Default.Bedtime
                        DhikrCategory.WAKEUP -> Icons.Default.Brightness7
                        DhikrCategory.HOME -> Icons.Default.Home
                        DhikrCategory.MOSQUE -> Icons.Default.Mosque
                        DhikrCategory.TRAVEL -> Icons.Default.FlightTakeoff
                        DhikrCategory.ISTIKHARA -> Icons.Default.Psychology
                        DhikrCategory.DISTRESS -> Icons.Default.Healing
                        DhikrCategory.SUSTENANCE -> Icons.Default.Diamond
                        DhikrCategory.MISCELLANEOUS -> Icons.Default.AutoAwesome
                    },
                    contentDescription = null,
                    tint = if (isSelected) EmeraldLight else SoftGoldBright,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.arabicName,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (isSelected) EmeraldLight else IvoryWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InteractiveAzkarReader(
    dhikrList: List<DhikrItem>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableStateOf(0) }
    var currentCount by remember { mutableStateOf(0) }

    val currentDhikr = dhikrList.getOrNull(currentIndex) ?: return
    val context = LocalContext.current

    val progress = ((currentCount.toFloat() / currentDhikr.targetCount)).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
            }

            GlassTag(
                text = "${currentIndex + 1} من ${dhikrList.size}",
                accentColor = SoftGold,
                textColor = SoftGoldBright
            )

            IconButton(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "« ${currentDhikr.arabicText} » — تطبيق «نُور»")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "مشاركة الذكر"))
                }
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = IvoryWhite)
            }
        }

        // Central Reading Area (Large Tap Zone)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MidnightNavyCard)
                .border(1.5.dp, if (progress >= 1f) EmeraldLight else GlassBorder, RoundedCornerShape(24.dp))
                .clickable {
                    if (currentCount + 1 >= currentDhikr.targetCount) {
                        if (currentIndex < dhikrList.size - 1) {
                            currentIndex++
                            currentCount = 0
                        } else {
                            currentCount = currentDhikr.targetCount
                        }
                    } else {
                        currentCount++
                    }
                }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currentDhikr.arabicText,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 20.sp,
                        lineHeight = 34.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    color = IvoryWhite
                )

                if (currentDhikr.virtue != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "فضل هذا الذكر: ${currentDhikr.virtue}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        ),
                        color = SoftGoldBright
                    )
                }

                if (currentDhikr.reference.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "المصدر: ${currentDhikr.reference}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "اضغط على البطاقة للتكرار",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDim
                )
            }
        }

        // Bottom Tap Counter & Stepper
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "التكرار المطلوب: ${currentDhikr.targetCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IvoryMuted
                )
                Text(
                    text = "$currentCount / ${currentDhikr.targetCount}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (progress >= 1f) EmeraldLight else SoftGoldBright
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = EmeraldLight,
                trackColor = MidnightNavySurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            currentCount = 0
                        }
                    },
                    enabled = currentIndex > 0
                ) {
                    Text(text = "السابق", color = if (currentIndex > 0) SoftGold else TextDim)
                }

                TextButton(
                    onClick = {
                        if (currentIndex < dhikrList.size - 1) {
                            currentIndex++
                            currentCount = 0
                        }
                    },
                    enabled = currentIndex < dhikrList.size - 1
                ) {
                    Text(text = "التالي", color = if (currentIndex < dhikrList.size - 1) EmeraldLight else TextDim)
                }
            }
        }
    }
}

@Composable
fun FuturisticDigitalTasbih(
    count: Int,
    target: Int,
    selectedDhikrName: String,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onSelectDhikr: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isScalePressed by remember { mutableStateOf(false) }
    var showDhikrPicker by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isScalePressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 600f),
        label = "tasbihScale"
    )

    val progress = (count.toFloat() / target.coerceAtLeast(1)).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("digital_tasbih_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dhikr Selector Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
            onClick = { showDhikrPicker = true }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "الذكر الحالي", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = selectedDhikrName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlassTag(text = "الهدف: $target", accentColor = SoftGold, textColor = SoftGoldBright)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = SoftGold)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Giant Center Counter Ring
        Box(
            modifier = Modifier
                .size(240.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            EmeraldDark.copy(alpha = 0.6f),
                            MidnightNavySurface,
                            MidnightNavyDark
                        )
                    )
                )
                .border(2.5.dp, if (progress >= 1f) EmeraldLight else SoftGold.copy(alpha = 0.5f), CircleShape)
                .clickable {
                    isScalePressed = true
                    onIncrement()
                    isScalePressed = false
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = IvoryWhite
                )

                Text(
                    text = "من $target",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftGoldBright
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "اضغط للتسبيح 🤍",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldLight
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reset and Target buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "تصفير", tint = TextMuted, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "تصفير العداد", color = TextMuted)
            }

            Button(
                onClick = { onSelectDhikr(selectedDhikrName, if (target == 33) 100 else if (target == 100) 1000 else 33) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MidnightNavySurface)
            ) {
                Text(text = "تغيير الهدف: $target", color = SoftGoldBright)
            }
        }
    }

    // Dhikr Picker Dialog
    if (showDhikrPicker) {
        val predefined = listOf(
            Pair("سُبْحَانَ اللَّهِ", 33),
            Pair("الْحَمْدُ لِلَّهِ", 33),
            Pair("اللَّهُ أَكْبَرُ", 33),
            Pair("لاَ إِلَهَ إِلاَّ اللَّهُ", 100),
            Pair("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ", 100),
            Pair("أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", 100),
            Pair("اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", 100),
            Pair("لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ", 100)
        )

        AlertDialog(
            onDismissRequest = { showDhikrPicker = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(text = "اختر صيغة الذكر", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(predefined) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedDhikrName == item.first) EmeraldDark else MidnightNavySurface)
                                .clickable {
                                    onSelectDhikr(item.first, item.second)
                                    showDhikrPicker = false
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.first, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = IvoryWhite)
                            GlassTag(text = "${item.second} مرة", accentColor = SoftGold, textColor = SoftGoldBright)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDhikrPicker = false }) {
                    Text(text = "إغلاق", color = SoftGold)
                }
            }
        )
    }
}
