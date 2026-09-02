package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.PresetDhikr
import com.example.data.repository.TasbihCategory
import com.example.data.repository.TasbihDataRepository
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassTag
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalTasbihScreen(
    uiState: NoorUiState,
    onBack: (() -> Unit)? = null,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onResetLaps: () -> Unit,
    onSelectPresetDhikr: (PresetDhikr, Int?) -> Unit,
    onSetTarget: (Int) -> Unit,
    onToggleHaptic: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleFullScreenTap: () -> Unit,
    onAddCustomDhikr: (String, Int, String) -> Unit,
    onDeleteCustomDhikr: (String) -> Unit,
    onDismissCelebration: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDhikrListSheet by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var showAddCustomDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf(TasbihCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    // Spring button click animation
    var isTapped by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        targetValue = if (isTapped) 0.93f else 1.0f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 800f),
        label = "buttonScale"
    )

    // Full screen tap handler
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("digital_tasbih_screen")
            .then(
                if (uiState.isTasbihFullScreenTapEnabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        coroutineScope.launch {
                            isTapped = true
                            delay(60)
                            isTapped = false
                        }
                        onIncrement()
                    }
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Bar
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MidnightNavyCard)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "رجوع",
                            tint = IvoryWhite
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MidnightNavyCard.copy(alpha = 0.8f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = null,
                                tint = SoftGoldBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "مجموع اليوم: ${uiState.todayTotalTasbihCount}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = IvoryWhite
                            )
                        }
                    }
                }

                Text(
                    text = "السبحة الإلكترونية 📿",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )

                // Quick Toggle Actions (Sound, Haptic, FullScreen)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onToggleSound,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isTasbihSoundEnabled) EmeraldDark.copy(alpha = 0.5f) else MidnightNavyCard)
                            .border(
                                1.dp,
                                if (uiState.isTasbihSoundEnabled) EmeraldLight.copy(alpha = 0.5f) else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (uiState.isTasbihSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "الصوت",
                            tint = if (uiState.isTasbihSoundEnabled) EmeraldLight else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleHaptic,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isTasbihHapticEnabled) SoftGold.copy(alpha = 0.2f) else MidnightNavyCard)
                            .border(
                                1.dp,
                                if (uiState.isTasbihHapticEnabled) SoftGoldBright.copy(alpha = 0.5f) else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (uiState.isTasbihHapticEnabled) Icons.Default.Vibration else Icons.Default.Smartphone,
                            contentDescription = "الاهتزاز",
                            tint = if (uiState.isTasbihHapticEnabled) SoftGoldBright else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleFullScreenTap,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isTasbihFullScreenTapEnabled) EmeraldDark.copy(alpha = 0.7f) else MidnightNavyCard)
                            .border(
                                1.dp,
                                if (uiState.isTasbihFullScreenTapEnabled) EmeraldLight else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "النقر في كامل الشاشة",
                            tint = if (uiState.isTasbihFullScreenTapEnabled) EmeraldLight else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (uiState.isTasbihFullScreenTapEnabled) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(EmeraldDark.copy(alpha = 0.35f))
                        .border(1.dp, EmeraldLight.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "وضع النقر الحر مفعل: المس أي مكان في الشاشة للتسبيح ✨",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = EmeraldLight
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Active Dhikr Banner Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_dhikr_banner"),
                backgroundColor = MidnightNavyCard.copy(alpha = 0.85f),
                borderColor = SoftGold.copy(alpha = 0.35f),
                onClick = { showDhikrListSheet = true }
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
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SoftGoldBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "الذكر الحالي",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            GlassTag(
                                text = "تغيير الذكر",
                                accentColor = EmeraldPrimary,
                                textColor = EmeraldLight
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = EmeraldLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.selectedDhikrName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 19.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        color = IvoryWhite,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (uiState.selectedPresetDhikr.virtueArabic.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "✨ ${uiState.selectedPresetDhikr.virtueArabic}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = IvoryMuted,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Giant Circular Counter Canvas
            val targetVal = uiState.tasbihTarget
            val currentCount = uiState.tasbihCount
            val progress = if (targetVal > 0) (currentCount.toFloat() / targetVal).coerceIn(0f, 1f) else 0f

            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing),
                label = "progressAnim"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(270.dp)
                        .scale(buttonScale)
                        .testTag("circular_tasbih_counter")
                        .clip(CircleShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            coroutineScope.launch {
                                isTapped = true
                                delay(60)
                                isTapped = false
                            }
                            onIncrement()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Custom Canvas Circular Ring
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 14.dp.toPx()
                        val diameter = size.minDimension - strokeWidth - 8.dp.toPx()
                        val radius = diameter / 2f
                        val centerOffset = Offset(size.width / 2, size.height / 2)

                        // Outer subtle bead ticks (33 points)
                        val totalTicks = 33
                        for (i in 0 until totalTicks) {
                            val angleRad = Math.toRadians((i * (360f / totalTicks) - 90).toDouble())
                            val tickRadius = radius + 14.dp.toPx()
                            val tickX = (centerOffset.x + tickRadius * cos(angleRad)).toFloat()
                            val tickY = (centerOffset.y + tickRadius * sin(angleRad)).toFloat()

                            val isFilled = if (targetVal > 0) (i.toFloat() / totalTicks) <= animatedProgress else false
                            drawCircle(
                                color = if (isFilled) SoftGoldBright.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.12f),
                                radius = if (isFilled) 3.5.dp.toPx() else 2.dp.toPx(),
                                center = Offset(tickX, tickY)
                            )
                        }

                        // Background Track Arc
                        drawCircle(
                            color = MidnightNavySurface.copy(alpha = 0.85f),
                            radius = radius,
                            center = centerOffset
                        )

                        drawCircle(
                            color = Color.White.copy(alpha = 0.06f),
                            radius = radius,
                            center = centerOffset,
                            style = Stroke(width = strokeWidth)
                        )

                        // Animated Sweep Progress Arc
                        if (targetVal > 0 && animatedProgress > 0f) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        EmeraldPrimary,
                                        EmeraldLight,
                                        SoftGoldBright,
                                        EmeraldLight
                                    ),
                                    center = centerOffset
                                ),
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                                size = Size(radius * 2, radius * 2),
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                    }

                    // Inner Central Glow Disc
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        if (progress >= 1f) EmeraldLight.copy(alpha = 0.25f) else EmeraldDark.copy(alpha = 0.5f),
                                        MidnightNavySurface.copy(alpha = 0.9f),
                                        MidnightNavyDark.copy(alpha = 0.95f)
                                    )
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (progress >= 1f) EmeraldLight else SoftGold.copy(alpha = 0.4f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Cycle/Lap indicator
                            if (uiState.tasbihLaps > 0) {
                                Surface(
                                    color = SoftGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "الدورة ${uiState.tasbihLaps}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = SoftGoldBright,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            // Big Count Display
                            Text(
                                text = "$currentCount",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 58.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-1).sp
                                ),
                                color = IvoryWhite
                            )

                            // Goal Subtitle
                            Text(
                                text = if (targetVal > 0) "الهدف: $targetVal" else "تسبيح حر (مفتوح)",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = if (progress >= 1f) EmeraldLight else SoftGoldBright
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "اضغط للتسبيح 🤍",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = EmeraldLight.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Target Selector Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الهدف السريع:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextMuted
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(33, 100, 500, 1000, 0).forEach { targetOption ->
                        val isSelected = targetVal == targetOption
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) EmeraldDark else MidnightNavyCard)
                                .border(
                                    1.dp,
                                    if (isSelected) EmeraldLight else GlassBorder.copy(alpha = 0.2f),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSetTarget(targetOption) }
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (targetOption == 0) "∞ حر" else "$targetOption",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) EmeraldLight else IvoryMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Bar (Reset, Decrement -1, Preset List Drawer)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                OutlinedButton(
                    onClick = { showResetConfirmDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("reset_tasbih_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IvoryWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder.copy(alpha = 0.3f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة ضبط",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تصفير",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextMuted
                    )
                }

                // Decrement Button (-1 undo)
                Button(
                    onClick = onDecrement,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("decrement_tasbih_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MidnightNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold.copy(alpha = 0.3f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "تراجع -1",
                        tint = SoftGoldBright,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تراجع -١",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                }

                // Preset Dhikr List Button
                Button(
                    onClick = { showDhikrListSheet = true },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(46.dp)
                        .testTag("preset_dhikr_list_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatListBulleted,
                        contentDescription = "قائمة الأذكار",
                        tint = EmeraldLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الأذكار الجاهزة",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldLight
                    )
                }
            }
        }
    }

    // Reset Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(
                    text = "تصفير عداد التسبيح؟",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = IvoryWhite
                )
            },
            text = {
                Text(
                    text = "هل تريد تصفير العداد الحالي (${uiState.tasbihCount})، أم تصفير العداد والدورات المكتملة معاً؟",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IvoryMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReset()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
                ) {
                    Text("تصفير العداد الحالي", color = EmeraldLight)
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        onReset()
                        onResetLaps()
                        showResetConfirmDialog = false
                    }) {
                        Text("تصفير الكل مع الدورات", color = SoftGoldBright)
                    }
                    TextButton(onClick = { showResetConfirmDialog = false }) {
                        Text("إلغاء", color = TextMuted)
                    }
                }
            }
        )
    }

    // Goal Achieved Celebration Dialog
    if (uiState.showTasbihGoalCelebration) {
        AlertDialog(
            onDismissRequest = onDismissCelebration,
            containerColor = MidnightNavyCard,
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(EmeraldDark)
                        .border(1.5.dp, EmeraldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldLight,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "ما شاء الله! تقبل الله طاعتكم ✨",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "أتممت هدفك بنجاح (${uiState.tasbihTarget} تسبيحة) من ذكر:\n«${uiState.selectedDhikrName}»",
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        color = IvoryWhite
                    )
                    Text(
                        text = "الدورات المكتملة حتى الآن: ${uiState.tasbihLaps} دورة",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldLight
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismissCelebration()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
                ) {
                    Text("متابعة الدورة التالية (+1)", color = EmeraldLight)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismissCelebration()
                    showDhikrListSheet = true
                }) {
                    Text("اختيار ذكر آخر", color = SoftGoldBright)
                }
            }
        )
    }

    // Preset Dhikrs Bottom Sheet / Library
    if (showDhikrListSheet) {
        val allDhikrs = (TasbihDataRepository.presetDhikrList + uiState.customDhikrList)
        val filteredDhikrs = allDhikrs.filter { item ->
            val matchCat = when (selectedCategoryFilter) {
                TasbihCategory.ALL -> true
                TasbihCategory.CUSTOM -> item.isCustom
                else -> item.category == selectedCategoryFilter
            }
            val matchQuery = if (searchQuery.isBlank()) true else {
                item.arabicText.contains(searchQuery, ignoreCase = true) ||
                        item.translation.contains(searchQuery, ignoreCase = true) ||
                        item.virtueArabic.contains(searchQuery, ignoreCase = true)
            }
            matchCat && matchQuery
        }

        ModalBottomSheet(
            onDismissRequest = { showDhikrListSheet = false },
            containerColor = MidnightNavyDark,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(GlassBorder)
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .padding(horizontal = 16.dp)
            ) {
                // Sheet Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "مكتبة الأذكار والتسابيح 📿",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        Text(
                            text = "اختر من التسابيح المأثورة أو أضف ذكراً مخصصاً",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = { showAddCustomDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftGold.copy(alpha = 0.25f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = SoftGoldBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "إضافة مخصص", color = SoftGoldBright, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    placeholder = { Text("ابحث في التسابيح والأدعية...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "مسح", tint = TextMuted)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MidnightNavyCard,
                        unfocusedContainerColor = MidnightNavyCard,
                        focusedBorderColor = EmeraldLight,
                        unfocusedBorderColor = GlassBorder.copy(alpha = 0.3f),
                        focusedTextColor = IvoryWhite,
                        unfocusedTextColor = IvoryWhite
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Pills Row
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(TasbihCategory.values().toList()) { cat ->
                        val isSelected = selectedCategoryFilter == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) EmeraldDark else MidnightNavySurface)
                                .border(
                                    1.dp,
                                    if (isSelected) EmeraldLight else GlassBorder.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCategoryFilter = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${cat.iconEmoji} ${cat.titleArabic}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) EmeraldLight else IvoryMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dhikrs List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredDhikrs, key = { it.id }) { dhikr ->
                        val isCurrentActive = uiState.selectedPresetDhikr.id == dhikr.id

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = if (isCurrentActive) MidnightNavySurface else MidnightNavyCard.copy(alpha = 0.8f),
                            borderColor = if (isCurrentActive) EmeraldLight else GlassBorder.copy(alpha = 0.2f),
                            onClick = {
                                onSelectPresetDhikr(dhikr, dhikr.defaultTarget)
                                showDhikrListSheet = false
                            }
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
                                        GlassTag(
                                            text = dhikr.category.titleArabic,
                                            accentColor = if (isCurrentActive) EmeraldPrimary else SoftGold,
                                            textColor = if (isCurrentActive) EmeraldLight else SoftGoldBright
                                        )
                                        GlassTag(
                                            text = "${dhikr.defaultTarget} مرة",
                                            accentColor = GlassBorder,
                                            textColor = IvoryMuted
                                        )
                                    }

                                    if (dhikr.isCustom) {
                                        IconButton(
                                            onClick = { onDeleteCustomDhikr(dhikr.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "حذف الذكر المخصص",
                                                tint = Color.Red.copy(alpha = 0.7f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else if (isCurrentActive) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "المحدد حالياً",
                                            tint = EmeraldLight,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = dhikr.arabicText,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 26.sp
                                    ),
                                    color = IvoryWhite
                                )

                                if (dhikr.virtueArabic.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "فضل هذا الذكر: ${dhikr.virtueArabic}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = SoftGoldBright
                                    )
                                }

                                if (dhikr.hadithReference.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "المصدر: ${dhikr.hadithReference}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = TextDim
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Custom Dhikr Dialog
    if (showAddCustomDialog) {
        var customArabicText by remember { mutableStateOf("") }
        var customTargetText by remember { mutableStateOf("33") }
        var customVirtueText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddCustomDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(
                    text = "إضافة ذكر مخصص ✏️",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = customArabicText,
                        onValueChange = { customArabicText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("صيغة الذكر (بالعربية)", color = TextMuted) },
                        placeholder = { Text("مثال: رَبِّ اغْفِرْ لِي وَارْحَمْنِي", color = TextDim) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLight,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = IvoryWhite,
                            unfocusedTextColor = IvoryWhite
                        )
                    )

                    OutlinedTextField(
                        value = customTargetText,
                        onValueChange = { customTargetText = it.filter { char -> char.isDigit() } },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("الهدف المطلوب (مثلاً: 33، 100، 1000)", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLight,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = IvoryWhite,
                            unfocusedTextColor = IvoryWhite
                        )
                    )

                    OutlinedTextField(
                        value = customVirtueText,
                        onValueChange = { customVirtueText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("ملاحظة أو فضل الذكر (اختياري)", color = TextMuted) },
                        placeholder = { Text("مثال: دعاء للسكينة وتفريج الهم", color = TextDim) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLight,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = IvoryWhite,
                            unfocusedTextColor = IvoryWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customArabicText.isNotBlank()) {
                            val target = customTargetText.toIntOrNull() ?: 33
                            onAddCustomDhikr(customArabicText, target, customVirtueText)
                            showAddCustomDialog = false
                            showDhikrListSheet = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                    enabled = customArabicText.isNotBlank()
                ) {
                    Text("حفظ والبدء بالتسبيح", color = EmeraldLight)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCustomDialog = false }) {
                    Text("إلغاء", color = TextMuted)
                }
            }
        )
    }
}
