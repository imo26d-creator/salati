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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationMethod
import com.example.data.model.JuristicMethod
import com.example.data.model.MuezzinVoice
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState

@Composable
fun MoreScreen(
    uiState: NoorUiState,
    onToggleMosqueMode: () -> Unit,
    onToggleTravelMode: () -> Unit,
    onShowWidgetsPreview: () -> Unit,
    onMethodChange: (CalculationMethod) -> Unit,
    onJuristicChange: (JuristicMethod) -> Unit,
    onPreAlertChange: (Int) -> Unit,
    onSelectMuezzin: (MuezzinVoice) -> Unit = {},
    onVolumeChange: (Float) -> Unit = {},
    onToggleMuezzinPreview: (MuezzinVoice) -> Unit = {},
    onPlayToneTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRamadanSheet by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showMuezzinSheet by remember { mutableStateOf(false) }
    var showTravelDialog by remember { mutableStateOf(false) }
    var showCalendarEventsDialog by remember { mutableStateOf(false) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("more_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "المزيد والخدمات الإسلامية 🌙",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                    Text(
                        text = "المناسبات الهجرية، وضع المسجد والسفر، وحاسبة الزكاة",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            // Feature Sections Grid
            item {
                Text(
                    text = "الأوضاع الذكية والمميزات",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftGoldBright
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MoreFeatureCard(
                        title = "ركن الصيام والزكاة (رمضان) 🌙",
                        subtitle = "مواقيت الإمساك والإفطار، وحاسبة زكاة المال والذهب",
                        icon = Icons.Default.NightsStay,
                        accentColor = SoftGold,
                        onClick = { showRamadanSheet = true }
                    )

                    MoreFeatureCard(
                        title = if (uiState.isMosqueMode) "وضع المسجد نشط (صامت) 🕌" else "وضع المسجد الصامت 🕌",
                        subtitle = "كتم كافة الأصوات تلقائيًا للخشوع أثناء الصلاة",
                        icon = Icons.Default.Mosque,
                        accentColor = EmeraldLight,
                        onClick = onToggleMosqueMode
                    )

                    MoreFeatureCard(
                        title = "أحكام ورخصة السفر ✈️",
                        subtitle = "جمع وقصر الصلوات، واتجاه القبلة أثناء الترحال",
                        icon = Icons.Default.FlightTakeoff,
                        accentColor = SoftGoldBright,
                        onClick = { showTravelDialog = true }
                    )

                    MoreFeatureCard(
                        title = "المناسبات والأيام البيض 📅",
                        subtitle = "الأيام البيض، عاشوراء، يوم عرفة، والمواسم الفاضلة",
                        icon = Icons.Default.Event,
                        accentColor = EmeraldPrimary,
                        onClick = { showCalendarEventsDialog = true }
                    )

                    MoreFeatureCard(
                        title = "صوت الأذان واختيار المؤذن 🕌",
                        subtitle = "التحكم بمستوى الصوت (${(uiState.azanVolume * 100).toInt()}%) واختيار المؤذن المفضل",
                        icon = Icons.Default.Campaign,
                        accentColor = SoftGoldBright,
                        onClick = { showMuezzinSheet = true }
                    )

                    MoreFeatureCard(
                        title = "ويدجت الشاشة الرئيسية والقفل 📱",
                        subtitle = "إضافة مواقيت الصلاة والورد اليومي لشاشة هاتفك",
                        icon = Icons.Default.Widgets,
                        accentColor = SoftGold,
                        onClick = onShowWidgetsPreview
                    )

                    MoreFeatureCard(
                        title = "إعدادات المواقيت والحسابات الفلكية ⚙️",
                        subtitle = "أم القرى، رابطة العالم الإسلامي، المذهب الفقهي، والأذان",
                        icon = Icons.Default.Settings,
                        accentColor = IvoryMuted,
                        onClick = { showSettingsSheet = true }
                    )
                }
            }
        }
    }

    // Ramadan Dialog
    if (showRamadanSheet) {
        AlertDialog(
            onDismissRequest = { showRamadanSheet = false },
            containerColor = MidnightNavyCard,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "ركن الصيام والزكاة 🌙", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
                    IconButton(onClick = { showRamadanSheet = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = IvoryWhite)
                    }
                }
            },
            text = {
                RamadanHubView(uiState = uiState)
            },
            confirmButton = {
                TextButton(onClick = { showRamadanSheet = false }) {
                    Text(text = "إغلاق", color = SoftGold)
                }
            }
        )
    }

    // Travel Dialog
    if (showTravelDialog) {
        AlertDialog(
            onDismissRequest = { showTravelDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.FlightTakeoff, contentDescription = null, tint = SoftGold)
                    Text(text = "أحكام صلاة المسافر ✈️", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = IvoryWhite)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "• مسافة القصر: حوالي 80 كم فأكثر.", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                    Text(text = "• الصلوات الرباعية (الظهر، العصر، العشاء) تُقصر إلى ركعتين.", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                    Text(text = "• يجوز الجمع بين الظهر والعصر (تقديمًا أو تأخيرًا)، والمغرب والعشاء.", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                    Text(text = "• لا قصر في صلاتي الصبح والمغرب.", style = MaterialTheme.typography.bodySmall, color = IvoryMuted)
                }
            },
            confirmButton = {
                TextButton(onClick = { showTravelDialog = false }) {
                    Text(text = "تقبل الله سفركم", color = EmeraldLight)
                }
            }
        )
    }

    // Islamic Calendar Events Dialog
    if (showCalendarEventsDialog) {
        AlertDialog(
            onDismissRequest = { showCalendarEventsDialog = false },
            containerColor = MidnightNavyCard,
            title = {
                Text(text = "المناسبات والأيام الفاضلة 📅", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SoftGoldBright)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "• الأيام البيض (13، 14، 15 من كل شهر هجري): صيامها كصيام الدهر.", style = MaterialTheme.typography.bodySmall, color = EmeraldLight)
                    Text(text = "• صيام يومي الإثنين والخميس: تُعرض فيهما الأعمال على الله تعالى.", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                    Text(text = "• شهر رمضان المبارك: شهر القرآن والعتق من النيران.", style = MaterialTheme.typography.bodySmall, color = SoftGoldBright)
                    Text(text = "• يوم عرفة (9 ذو الحجة): يكفر ذنوب سنة ماضية وسنة باقية.", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                    Text(text = "• يوم عاشوراء (10 محرم): يكفر السنة الماضية.", style = MaterialTheme.typography.bodySmall, color = IvoryWhite)
                }
            },
            confirmButton = {
                TextButton(onClick = { showCalendarEventsDialog = false }) {
                    Text(text = "إغلاق", color = SoftGold)
                }
            }
        )
    }

    // Muezzin & Sound Settings Dialog
    if (showMuezzinSheet) {
        MuezzinSelectionDialog(
            uiState = uiState,
            onSelectMuezzin = onSelectMuezzin,
            onVolumeChange = onVolumeChange,
            onToggleMuezzinPreview = onToggleMuezzinPreview,
            onDismiss = { showMuezzinSheet = false }
        )
    }

    // Full Settings Dialog
    if (showSettingsSheet) {
        SettingsSheetDialog(
            uiState = uiState,
            onMethodChange = onMethodChange,
            onJuristicChange = onJuristicChange,
            onPreAlertChange = onPreAlertChange,
            onSelectMuezzin = onSelectMuezzin,
            onVolumeChange = onVolumeChange,
            onToggleMuezzinPreview = onToggleMuezzinPreview,
            onPlayToneTest = onPlayToneTest,
            onDismiss = { showSettingsSheet = false }
        )
    }
}

@Composable
fun MoreFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MidnightNavyCard.copy(alpha = 0.8f),
        borderColor = GlassBorder.copy(alpha = 0.15f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = IvoryWhite
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = SoftGold,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
