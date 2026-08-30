package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun QiblaCompassView(
    currentAzimuth: Float,
    targetQiblaAngle: Float,
    distanceToMakkahKm: Int,
    cityName: String,
    isARMode: Boolean,
    onToggleAR: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Relative angle between current heading and Qibla target
    val relativeAngle = (targetQiblaAngle - currentAzimuth + 360) % 360
    val isAligned = abs(if (relativeAngle > 180) relativeAngle - 360 else relativeAngle) <= 3.0f

    val animatedRotation by animateFloatAsState(
        targetValue = -currentAzimuth,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "compassRotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("qibla_compass_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Alignment Banner
        if (isAligned) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(EmeraldDark)
                    .border(1.5.dp, EmeraldLight, RoundedCornerShape(20.dp))
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "أنت تتجه الآن نحو القبلة مباشرة 🕋",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                }
            }
        } else {
            GlassTag(
                text = "درجة القبلة: ${targetQiblaAngle.roundToInt()}° من الشمال",
                accentColor = SoftGold,
                textColor = SoftGoldBright
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isARMode) {
            // Futuristic AR Camera overlay view
            FuturisticAROverlay(
                relativeAngle = relativeAngle,
                isAligned = isAligned,
                distanceToMakkahKm = distanceToMakkahKm,
                onCloseAR = onToggleAR
            )
        } else {
            // Giant Futuristic Compass Dial
            Box(
                modifier = Modifier
                    .size(290.dp)
                    .shadow(elevation = 20.dp, shape = CircleShape, spotColor = if (isAligned) EmeraldGlow else GoldGlow)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MidnightNavySurface,
                                MidnightNavyCard,
                                MidnightNavyDark
                            )
                        )
                    )
                    .border(
                        width = if (isAligned) 3.dp else 1.5.dp,
                        color = if (isAligned) EmeraldLight else GlassBorder,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Rotating Compass Dial
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedRotation)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2 - 16.dp.toPx()

                    // Draw outer ticks (every 10 degrees)
                    for (i in 0 until 360 step 10) {
                        val angleRad = Math.toRadians(i.toDouble())
                        val isMajor = i % 30 == 0
                        val isCardinal = i % 90 == 0

                        val tickLength = if (isCardinal) 18.dp.toPx() else if (isMajor) 12.dp.toPx() else 6.dp.toPx()
                        val tickColor = if (isCardinal) SoftGoldBright else if (isMajor) IvoryWhite else TextMuted.copy(alpha = 0.5f)
                        val strokeW = if (isCardinal) 3.dp.toPx() else if (isMajor) 2.dp.toPx() else 1.dp.toPx()

                        val startX = center.x + (radius - tickLength) * sin(angleRad).toFloat()
                        val startY = center.y - (radius - tickLength) * cos(angleRad).toFloat()
                        val endX = center.x + radius * sin(angleRad).toFloat()
                        val endY = center.y - radius * cos(angleRad).toFloat()

                        drawLine(
                            color = tickColor,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = strokeW
                        )
                    }

                    // Draw Qibla pointer line and beacon on compass ring
                    val qiblaRad = Math.toRadians(targetQiblaAngle.toDouble())
                    val qiblaX = center.x + (radius - 28.dp.toPx()) * sin(qiblaRad).toFloat()
                    val qiblaY = center.y - (radius - 28.dp.toPx()) * cos(qiblaRad).toFloat()

                    drawCircle(
                        color = if (isAligned) EmeraldLight else SoftGoldBright,
                        radius = 10.dp.toPx(),
                        center = Offset(qiblaX, qiblaY)
                    )
                }

                // Central Fixed Indicator Needle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mosque,
                        contentDescription = "الكعبة المشرفة",
                        tint = if (isAligned) EmeraldLight else SoftGoldBright,
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${currentAzimuth.roundToInt()}°",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                    Text(
                        text = if (isAligned) "وجهتك صحيحة" else "حرّك الهاتف",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = if (isAligned) EmeraldLight else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Cards (Distance & City & AR mode toggle)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassCard(
                modifier = Modifier.weight(1f),
                backgroundColor = MidnightNavyCard.copy(alpha = 0.8f)
            ) {
                Column {
                    Text(text = "المسافة إلى مكة", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$distanceToMakkahKm كم تقريباً",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftGoldBright
                    )
                }
            }

            GlassCard(
                modifier = Modifier.weight(1f),
                backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
                onClick = onToggleAR
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isARMode) Icons.Default.Explore else Icons.Default.ViewInAr,
                        contentDescription = null,
                        tint = EmeraldLight,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = if (isARMode) "الوضع العادي" else "الواقع المعزز",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        Text(
                            text = "كاميرا القبلة",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FuturisticAROverlay(
    relativeAngle: Float,
    isAligned: Boolean,
    distanceToMakkahKm: Int,
    onCloseAR: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF030712))
            .border(2.dp, if (isAligned) EmeraldLight else SoftGold.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Horizon grid lines simulation
        Canvas(modifier = Modifier.fillMaxSize()) {
            val h = size.height
            val w = size.width

            // Horizon line
            drawLine(
                color = SoftGold.copy(alpha = 0.4f),
                start = Offset(0f, h / 2),
                end = Offset(w, h / 2),
                strokeWidth = 2.dp.toPx()
            )

            // Dynamic AR Beacon Offset based on relative angle
            val normalizedOffset = (if (relativeAngle > 180) relativeAngle - 360 else relativeAngle) / 45f
            val beaconX = (w / 2) + normalizedOffset * (w / 2.5f)

            // Draw futuristic targeting brackets and Kaaba beacon
            drawCircle(
                color = if (isAligned) EmeraldLight else SoftGoldBright,
                radius = 24.dp.toPx(),
                center = Offset(beaconX.coerceIn(40f, w - 40f), h / 2),
                style = Stroke(width = 3.dp.toPx())
            )
        }

        // HUD Overlay Text
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassTag(text = "HUD AR 🕋", accentColor = EmeraldPrimary, textColor = EmeraldLight)
                IconButton(onClick = onCloseAR) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isAligned) "الكعبة المشرفة في الأفق المباشر 🕋" else "وجه الكاميرا نحو علامة الهدف",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isAligned) EmeraldLight else SoftGoldBright
                )
                Text(
                    text = "المسافة: $distanceToMakkahKm كم",
                    style = MaterialTheme.typography.bodySmall,
                    color = IvoryMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
