package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fingerprint
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
import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.repository.AzkarDataRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NoorUiState

import com.example.data.repository.PresetDhikr
import com.example.data.repository.TasbihCategory
import com.example.data.repository.TasbihDataRepository

@Composable
fun AzkarScreen(
    uiState: NoorUiState,
    onIncrementTasbih: () -> Unit,
    onDecrementTasbih: () -> Unit = {},
    onResetTasbih: () -> Unit,
    onResetTasbihLaps: () -> Unit = {},
    onSelectPresetDhikr: (PresetDhikr, Int?) -> Unit = { _, _ -> },
    onSetTasbihTarget: (Int) -> Unit = {},
    onToggleTasbihHaptic: () -> Unit = {},
    onToggleTasbihSound: () -> Unit = {},
    onToggleTasbihFullScreenTap: () -> Unit = {},
    onAddCustomDhikr: (String, Int, String) -> Unit = { _, _, _ -> },
    onDeleteCustomDhikr: (String) -> Unit = {},
    onDismissTasbihCelebration: () -> Unit = {},
    onSelectTasbihDhikr: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: الأذكار, 1: السبحة الذكية, 2: الأدعية
    var activeCategoryReading by remember { mutableStateOf<DhikrCategory?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("azkar_screen")
    ) {
        if (activeCategoryReading != null) {
            val list = AzkarDataRepository.getAzkarByCategory(activeCategoryReading!!)
            InteractiveAzkarReader(
                dhikrList = list,
                onClose = { activeCategoryReading = null }
            )
        } else if (selectedTab == 1) {
            // Dedicated full Digital Tasbih Screen Experience with back to tabs
            Column(modifier = Modifier.fillMaxSize()) {
                // Tab Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MidnightNavySurface)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TabPill(
                        label = "أذكار الحصن",
                        isSelected = false,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    TabPill(
                        label = "السبحة الذكية 📿",
                        isSelected = true,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    TabPill(
                        label = "موسوعة الأدعية",
                        isSelected = false,
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }

                DigitalTasbihScreen(
                    uiState = uiState,
                    onBack = null,
                    onIncrement = onIncrementTasbih,
                    onDecrement = onDecrementTasbih,
                    onReset = onResetTasbih,
                    onResetLaps = onResetTasbihLaps,
                    onSelectPresetDhikr = onSelectPresetDhikr,
                    onSetTarget = onSetTasbihTarget,
                    onToggleHaptic = onToggleTasbihHaptic,
                    onToggleSound = onToggleTasbihSound,
                    onToggleFullScreenTap = onToggleTasbihFullScreenTap,
                    onAddCustomDhikr = onAddCustomDhikr,
                    onDeleteCustomDhikr = onDeleteCustomDhikr,
                    onDismissCelebration = onDismissTasbihCelebration
                )
            }
        } else {
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
                            text = "الأذكار والسبحة 📿",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = IvoryWhite
                        )
                        Text(
                            text = "حصن المسلم، التسبيح الذكي، والأدعية المأثورة",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }
                }

                // Primary Tab Switcher (الأذكار / السبحة الذكية / الأدعية)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MidnightNavySurface)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TabPill(
                            label = "أذكار الحصن",
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        TabPill(
                            label = "السبحة الذكية 📿",
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.weight(1f)
                        )
                        TabPill(
                            label = "موسوعة الأدعية",
                            isSelected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                when (selectedTab) {
                    0 -> {
                        // Azkar Categories Grid
                        item {
                            Text(
                                text = "أقسام الأذكار اليومية",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SoftGoldBright
                            )
                        }

                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                val cats = DhikrCategory.values().toList()
                                for (i in cats.indices step 2) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        AzkarCategoryTile(
                                            category = cats[i],
                                            isSelected = false,
                                            onClick = { activeCategoryReading = cats[i] },
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (i + 1 < cats.size) {
                                            AzkarCategoryTile(
                                                category = cats[i + 1],
                                                isSelected = false,
                                                onClick = { activeCategoryReading = cats[i + 1] },
                                                modifier = Modifier.weight(1f)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Duas Library
                        item {
                            DuasLibrarySection()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) EmeraldDark else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) EmeraldLight else IvoryMuted
        )
    }
}
