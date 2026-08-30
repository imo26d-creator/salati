package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorViewModel
import com.google.android.gms.location.LocationServices
import java.util.Locale

enum class NoorNavTab(
    val route: String,
    val titleArabic: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "الرئيسية", Icons.Default.Home, Icons.Outlined.Home),
    PRAYERS("prayers", "المواقيت", Icons.Default.AccessTimeFilled, Icons.Outlined.AccessTime),
    QIBLA("qibla", "القبلة", Icons.Default.Explore, Icons.Outlined.Explore),
    QURAN("quran", "المصحف", Icons.Default.MenuBook, Icons.Outlined.MenuBook),
    AZKAR("azkar", "الأذكار", Icons.Default.Fingerprint, Icons.Outlined.Fingerprint),
    MORE("more", "المزيد", Icons.Default.GridView, Icons.Outlined.GridView)
}

class MainActivity : ComponentActivity() {

    private val viewModel: NoorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NoorTheme {
                val uiState by viewModel.uiState.collectAsState()
                val currentAzimuth by viewModel.compassManager.azimuth.collectAsState()
                var currentTab by remember { mutableStateOf(NoorNavTab.HOME) }
                var showWidgetsDialog by remember { mutableStateOf(false) }
                var showSettingsDialog by remember { mutableStateOf(false) }
                var showMuezzinDialog by remember { mutableStateOf(false) }
                var showMosqueModeDialog by remember { mutableStateOf(false) }

                val context = LocalContext.current

                // Request location permissions on startup to automatically calibrate prayer times & Qibla
                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

                    if (fineGranted || coarseGranted) {
                        try {
                            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                            fusedClient.lastLocation.addOnSuccessListener { location: Location? ->
                                if (location != null) {
                                    val geocoder = Geocoder(context, Locale("ar"))
                                    try {
                                        @Suppress("DEPRECATION")
                                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                        val city = addresses?.firstOrNull()?.locality
                                            ?: addresses?.firstOrNull()?.adminArea
                                            ?: "موقعي الحالي"
                                        val country = addresses?.firstOrNull()?.countryName ?: ""
                                        val fullCity = if (country.isNotEmpty()) "$city، $country" else city
                                        viewModel.setCustomLocation(fullCity, location.latitude, location.longitude)
                                    } catch (_: Exception) {
                                        viewModel.setCustomLocation("موقعي الحالي", location.latitude, location.longitude)
                                    }
                                }
                            }
                        } catch (_: SecurityException) {}
                    }
                }

                LaunchedEffect(Unit) {
                    val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    if (!hasFine) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MidnightNavyDark,
                    bottomBar = {
                        NoorBottomNavigationBar(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentTab,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(200))
                            },
                            label = "screenTransition"
                        ) { tab ->
                            when (tab) {
                                NoorNavTab.HOME -> HomeScreen(
                                    uiState = uiState,
                                    onNavigateToQibla = { currentTab = NoorNavTab.QIBLA },
                                    onNavigateToTasbih = { currentTab = NoorNavTab.AZKAR },
                                    onNavigateToQuran = { currentTab = NoorNavTab.QURAN },
                                    onNavigateToPrayers = { currentTab = NoorNavTab.PRAYERS },
                                    onToggleMosqueMode = {
                                        viewModel.toggleMosqueMode()
                                        if (uiState.isMosqueMode) showMosqueModeDialog = true
                                    },
                                    onShowWidgetsPreview = { showWidgetsDialog = true },
                                    onChecklistToggle = { viewModel.toggleChecklist(it) },
                                    onIncrementDhikr = { viewModel.incrementTasbih() },
                                    onOpenSettings = { showSettingsDialog = true }
                                )

                                NoorNavTab.PRAYERS -> PrayersScreen(
                                    uiState = uiState,
                                    onSelectDate = { viewModel.setSelectedDate(it) },
                                    onLogStatus = { type, status -> viewModel.logPrayerStatus(type, status) },
                                    onToggleAzan = { viewModel.togglePerPrayerAzan(it) },
                                    onOpenSettings = { showSettingsDialog = true },
                                    onOpenMuezzinSelection = { showMuezzinDialog = true }
                                )

                                NoorNavTab.QIBLA -> QiblaScreen(
                                    uiState = uiState,
                                    currentAzimuth = currentAzimuth,
                                    onToggleAR = { viewModel.toggleARMode() },
                                    onRecalibrate = { viewModel.compassManager.triggerHapticFeedback() }
                                )

                                NoorNavTab.QURAN -> QuranScreen(
                                    uiState = uiState,
                                    onAddPage = { viewModel.updateQuranProgress(uiState.quranPagesRead + 1) },
                                    onBookmarkAyah = { surahNum, ayahNum, surahName, ayahText ->
                                        viewModel.addBookmark(surahNum, ayahNum, surahName, ayahText)
                                    }
                                )

                                NoorNavTab.AZKAR -> AzkarScreen(
                                    uiState = uiState,
                                    onIncrementTasbih = { viewModel.incrementTasbih() },
                                    onResetTasbih = { viewModel.resetTasbih() },
                                    onSelectTasbihDhikr = { name, target -> viewModel.selectDhikr(name, target) }
                                )

                                NoorNavTab.MORE -> MoreScreen(
                                    uiState = uiState,
                                    onToggleMosqueMode = {
                                        viewModel.toggleMosqueMode()
                                        if (!uiState.isMosqueMode) showMosqueModeDialog = true
                                    },
                                    onToggleTravelMode = { viewModel.toggleTravelMode() },
                                    onShowWidgetsPreview = { showWidgetsDialog = true },
                                    onMethodChange = { viewModel.setCalculationMethod(it) },
                                    onJuristicChange = { viewModel.setJuristicMethod(it) },
                                    onPreAlertChange = { viewModel.setPrePrayerAlertMinutes(it) },
                                    onSelectMuezzin = { viewModel.setSelectedMuezzin(it) },
                                    onVolumeChange = { viewModel.setAzanVolume(it) },
                                    onToggleMuezzinPreview = { viewModel.toggleMuezzinPreview(it) },
                                    onPlayToneTest = { viewModel.playPreviewChime(false) }
                                )
                            }
                        }
                    }
                }

                // Global Dialogs
                if (showWidgetsDialog) {
                    WidgetsPreviewDialog(uiState = uiState, onDismiss = { showWidgetsDialog = false })
                }

                if (showSettingsDialog) {
                    SettingsSheetDialog(
                        uiState = uiState,
                        onMethodChange = { viewModel.setCalculationMethod(it) },
                        onJuristicChange = { viewModel.setJuristicMethod(it) },
                        onPreAlertChange = { viewModel.setPrePrayerAlertMinutes(it) },
                        onSelectMuezzin = { viewModel.setSelectedMuezzin(it) },
                        onVolumeChange = { viewModel.setAzanVolume(it) },
                        onToggleMuezzinPreview = { viewModel.toggleMuezzinPreview(it) },
                        onPlayToneTest = { viewModel.playPreviewChime(false) },
                        onDismiss = { showSettingsDialog = false }
                    )
                }

                if (showMuezzinDialog) {
                    MuezzinSelectionDialog(
                        uiState = uiState,
                        onSelectMuezzin = { viewModel.setSelectedMuezzin(it) },
                        onVolumeChange = { viewModel.setAzanVolume(it) },
                        onToggleMuezzinPreview = { viewModel.toggleMuezzinPreview(it) },
                        onDismiss = { showMuezzinDialog = false }
                    )
                }

                if (showMosqueModeDialog) {
                    MosqueModeDialog(onDismiss = { showMosqueModeDialog = false })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.compassManager.startListening()
    }

    override fun onPause() {
        super.onPause()
        viewModel.compassManager.stopListening()
    }
}

@Composable
fun NoorBottomNavigationBar(
    currentTab: NoorNavTab,
    onTabSelected: (NoorNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(26.dp), spotColor = Color.Black)
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MidnightNavyCard.copy(alpha = 0.95f),
                        MidnightNavyDark.copy(alpha = 0.98f)
                    )
                )
            )
            .border(1.dp, GlassBorder.copy(alpha = 0.25f), RoundedCornerShape(26.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NoorNavTab.values().forEach { tab ->
                val isSelected = currentTab == tab

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) EmeraldDark.copy(alpha = 0.4f) else Color.Transparent)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.titleArabic,
                            tint = if (isSelected) EmeraldLight else TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.titleArabic,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) EmeraldLight else TextMuted
                        )
                    }
                }
            }
        }
    }
}
