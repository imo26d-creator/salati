package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassIconButton
import com.example.ui.components.QiblaCompassView
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState

@Composable
fun QiblaScreen(
    uiState: NoorUiState,
    currentAzimuth: Float,
    onToggleAR: () -> Unit,
    onRecalibrate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalibrationHelp by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("qibla_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            text = "بوصلة القبلة الدقيقة 🕋",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        Text(
                            text = "معايرة بحساسات الهاتف وحسابات كروية",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassIconButton(
                            icon = Icons.Default.Info,
                            contentDescription = "المعايرة",
                            onClick = { showCalibrationHelp = true },
                            tint = SoftGold
                        )
                    }
                }
            }

            // Compass View
            item {
                QiblaCompassView(
                    currentAzimuth = currentAzimuth,
                    targetQiblaAngle = uiState.qiblaBearing,
                    distanceToMakkahKm = uiState.distanceToMakkahKm,
                    cityName = uiState.cityName,
                    isARMode = uiState.isARModeActive,
                    onToggleAR = onToggleAR
                )
            }

            // Guidance & Best accuracy card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MidnightNavyCard.copy(alpha = 0.75f),
                    borderColor = GlassBorder.copy(alpha = 0.2f)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "💡 نصائح للحصول على أعلى دقة للبوصلة:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SoftGoldBright
                        )
                        Text(
                            text = "• ضع الهاتف بشكل أفقي مستوٍ بعيدًا عن الأجسام المعدنية أو المغناطيسية.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = IvoryMuted
                        )
                        Text(
                            text = "• إذا شعرت بعدم الدقة، حرّك الهاتف في الهواء على شكل الرقم 8 (∞) للمعايرة التلقائية.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = IvoryMuted
                        )
                        Text(
                            text = "• سيهتز هاتفك بلطف وتتوهج الدائرة بالأخضر بمجرد محاذاة اتجاه الكعبة المشرفة.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = EmeraldLight
                        )
                    }
                }
            }
        }
    }

    if (showCalibrationHelp) {
        AlertDialog(
            onDismissRequest = { showCalibrationHelp = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(text = "معايرة بوصلة القبلة 🧭", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
            },
            text = {
                Text(
                    text = "يعتمد تحديد القبلة على الحسابات المثلثية الكروية الدقيقة لخطوط الطول والعرض مع مكة المكرمة. لتحسين الاستجابة، تأكد من تشغيل خدمات الموقع وتدوير الهاتف بحركة رقم 8 لتنشيط الحساس المغناطيسي.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IvoryWhite
                )
            },
            confirmButton = {
                TextButton(onClick = { showCalibrationHelp = false }) {
                    Text(text = "فهمت", color = EmeraldLight)
                }
            }
        )
    }
}
