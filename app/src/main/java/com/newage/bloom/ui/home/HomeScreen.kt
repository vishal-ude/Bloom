package com.newage.bloom.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newage.bloom.ads.AdaptiveBannerAd
import com.newage.bloom.ui.components.BloomFlowerLogo
import com.newage.bloom.ui.sheets.ActiveSheet
import com.newage.bloom.ui.sheets.EditProfileBottomSheet
import com.newage.bloom.ui.sheets.HabitStepperBottomSheet
import com.newage.bloom.ui.sheets.LogCycleBottomSheet
import com.newage.bloom.ui.sheets.LogMealsBottomSheet
import com.newage.bloom.ui.sheets.TodaysSymptomsBottomSheet
import com.newage.bloom.ui.theme.BloomDisplayFont
import com.newage.bloom.ui.theme.BloomThemeTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val colors = BloomThemeTokens.colors

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = colors.background,
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().background(colors.background)) {
                // Adaptive Banner Ad anchored above the bottom navigation bar
                AdaptiveBannerAd(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )

                // Attractive Floating Dock Bottom Navigation Bar
                BloomBottomNavBar(
                    selectedTab = state.activeTab,
                    onSelectTab = { viewModel.selectTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.activeTab) {
                0 -> DashboardTab(
                    state = state,
                    viewModel = viewModel
                )
                1 -> InsightsTab(state = state, viewModel = viewModel)
                2 -> LearnTab(
                    onSelectFaq = { viewModel.showFaqDetail(it) }
                )
                3 -> SettingsTab(
                    state = state,
                    viewModel = viewModel,
                    onResetOnboarding = {
                        viewModel.resetDemoOnboarding(onNavigateToOnboarding)
                    }
                )
            }

            // =========================================================================
            // ACTIVE BOTTOM SHEET ROUTING (Matches Figma Modals with 100% Fidelity)
            // =========================================================================
            when (state.activeSheet) {
                ActiveSheet.LOG_CYCLE -> {
                    LogCycleBottomSheet(
                        sheetState = bottomSheetState,
                        selectedDay = state.selectedCycleDay,
                        onSelectDay = { viewModel.selectCycleDay(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.saveCyclePeriod() }
                    )
                }
                ActiveSheet.TODAYS_SYMPTOMS -> {
                    TodaysSymptomsBottomSheet(
                        sheetState = bottomSheetState,
                        bloating = state.symptomBloating,
                        skin = state.symptomSkin,
                        mood = state.symptomMood,
                        sleep = state.symptomSleep,
                        onSelectBloating = { viewModel.selectSymptomBloating(it) },
                        onSelectSkin = { viewModel.selectSymptomSkin(it) },
                        onSelectMood = { viewModel.selectSymptomMood(it) },
                        onSelectSleep = { viewModel.selectSymptomSleep(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.saveSymptoms() }
                    )
                }
                ActiveSheet.LOG_MEALS -> {
                    LogMealsBottomSheet(
                        sheetState = bottomSheetState,
                        breakfast = state.mealBreakfast,
                        lunch = state.mealLunch,
                        dinner = state.mealDinner,
                        onSelectBreakfast = { viewModel.selectMealBreakfast(it) },
                        onSelectLunch = { viewModel.selectMealLunch(it) },
                        onSelectDinner = { viewModel.selectMealDinner(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.saveMeals() }
                    )
                }
                ActiveSheet.LOG_MOVEMENT -> {
                    HabitStepperBottomSheet(
                        sheetState = bottomSheetState,
                        title = "Log Movement",
                        currentValue = state.movementMinutes,
                        targetValue = 30,
                        unit = "min",
                        stepAmount = 10,
                        motivationalTitle = "Get moving today",
                        motivationalText = "No movement logged yet today. Try a brisk 30-minute walk, a swim, or some light strength training — these are especially good for insulin sensitivity with PCOS.",
                        quickAddText = "+10 min",
                        onValueChange = { viewModel.setMovementMinutes(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.closeSheet() }
                    )
                }
                ActiveSheet.LOG_WATER -> {
                    HabitStepperBottomSheet(
                        sheetState = bottomSheetState,
                        title = "Log Water",
                        currentValue = state.waterCount,
                        targetValue = 8,
                        unit = "glasses",
                        stepAmount = 1,
                        motivationalTitle = "Start hydrating",
                        motivationalText = "No water logged yet today. Try starting with a glass first thing in the morning to support energy and reduce bloating.",
                        quickAddText = "+1 glasses",
                        onValueChange = { viewModel.setWaterCount(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.closeSheet() }
                    )
                }
                ActiveSheet.LOG_RELAXATION -> {
                    HabitStepperBottomSheet(
                        sheetState = bottomSheetState,
                        title = "Log Relaxation",
                        currentValue = state.relaxationMinutes,
                        targetValue = 15,
                        unit = "min",
                        stepAmount = 5,
                        motivationalTitle = "Take a breather",
                        motivationalText = "Lower stress means lower cortisol, which directly helps balance hormones and reduce PCOS flare-ups.",
                        quickAddText = "+5 min",
                        onValueChange = { viewModel.setRelaxationMinutes(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.closeSheet() }
                    )
                }
                ActiveSheet.LOG_SLEEP -> {
                    HabitStepperBottomSheet(
                        sheetState = bottomSheetState,
                        title = "Log Sleep",
                        currentValue = state.sleepHours,
                        targetValue = 8,
                        unit = "hrs",
                        stepAmount = 1,
                        motivationalTitle = "Prioritize rest",
                        motivationalText = "Consistent, close-to-8-hr nights help keep hunger hormones in check and insulin more responsive.",
                        quickAddText = "+1 hr",
                        onValueChange = { viewModel.setSleepHours(it) },
                        onDismiss = { viewModel.closeSheet() },
                        onSave = { viewModel.closeSheet() }
                    )
                }
                ActiveSheet.EDIT_PROFILE -> {
                    EditProfileBottomSheet(
                        sheetState = bottomSheetState,
                        initialName = state.userName,
                        initialStage = state.pcosStage,
                        onDismiss = { viewModel.closeSheet() },
                        onSaveProfile = { name, stage -> viewModel.saveProfile(name, stage) }
                    )
                }
                null -> Unit
            }

            // FAQ Detail Popup Dialog if opened
            state.selectedFaqDetail?.let { faq ->
                AlertDialog(
                    onDismissRequest = { viewModel.dismissFaqDetail() },
                    title = {
                        Text(
                            text = faq.question,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary,
                            fontSize = 18.sp
                        )
                    },
                    text = {
                        Text(
                            text = faq.answer,
                            color = colors.textSecondary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.dismissFaqDetail() }) {
                            Text("Got it", color = colors.primary, fontWeight = FontWeight.SemiBold)
                        }
                    },
                    containerColor = colors.surface,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

// =========================================================================
// TAB 0: DASHBOARD TAB (Matches Exact Figma Screen at Node 26-7089)
// =========================================================================
@Composable
fun DashboardTab(
    state: HomeDashboardUiState,
    viewModel: HomeViewModel
) {
    val colors = BloomThemeTokens.colors

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Greeting & Avatar (Exact Figma Node 26-7089)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "GOOD MORNING, ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textSecondary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = state.userName,
                            fontFamily = BloomDisplayFont,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = colors.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.currentDateFormatted.ifEmpty { "Saturday, June 27" },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }

                // Avatar button (opens Edit Profile)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(colors.primaryContainer)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable { viewModel.openSheet(ActiveSheet.EDIT_PROFILE) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Edit Profile",
                        tint = colors.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // 2. Today's Goals Card with Signature Interactive Flower Logo (Exact Figma Node 26-7089)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.surface)
                    .border(1.dp, colors.border, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Today's Goals",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (state.completedGoalsCount == state.totalGoalsCount) "You're in full bloom!" else "Let's get blooming",
                            fontSize = 13.sp,
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${state.completedGoalsCount} of ${state.totalGoalsCount} complete",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { state.completionProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = colors.primary,
                            trackColor = colors.progressTrack
                        )
                    }

                    // Interactive signature bloom flower graphic
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(colors.primaryContainer.copy(alpha = 0.5f))
                            ) {
                                BloomFlowerLogo(
                                    size = 64.dp,
                                    petalColor = colors.primary,
                                    centerColor = colors.tertiary,
                                    progress = state.completionProgress
                                )
                            }

                            if (state.completedGoalsCount == 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Start here",
                                    fontFamily = BloomDisplayFont,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. PCOS FAQ Carousel Card (Exact Figma Node 26-7089)
        item {
            val currentFaq = viewModel.faqs[state.selectedFaqIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(colors.surface)
                    .border(1.dp, colors.border, RoundedCornerShape(18.dp))
                    .clickable { viewModel.nextFaq() }
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentFaq.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.tertiary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentFaq.question,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentFaq.answer,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = colors.textSecondary,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Carousel indicator dots
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            viewModel.faqs.forEachIndexed { i, _ ->
                                Box(
                                    modifier = Modifier
                                        .size(if (i == state.selectedFaqIndex) 6.dp else 4.dp)
                                        .clip(CircleShape)
                                        .background(if (i == state.selectedFaqIndex) colors.tertiary else colors.progressTrack)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next FAQ",
                        tint = colors.textMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // 4. Quick Action Cards: Cycle & Symptom Check-in (Exact Figma Node 26-7089)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Cycle Card -> Opens LogCycleBottomSheet
                FigmaQuickActionCard(
                    icon = Icons.Default.DateRange,
                    iconTint = colors.secondary,
                    iconBg = colors.secondaryContainer,
                    title = "Cycle",
                    subtitle = state.cycleStatus,
                    onClick = { viewModel.openSheet(ActiveSheet.LOG_CYCLE) }
                )

                // Symptom Check-in Card -> Opens TodaysSymptomsBottomSheet
                FigmaQuickActionCard(
                    icon = Icons.Default.Favorite,
                    iconTint = colors.primary,
                    iconBg = colors.primaryContainer,
                    title = "Symptom check-in",
                    subtitle = state.symptomStatus,
                    onClick = { viewModel.openSheet(ActiveSheet.TODAYS_SYMPTOMS) }
                )
            }
        }

        // 5. Goal Progress Section Header (Exact Figma Node 26-7089)
        item {
            Text(
                text = "Goal Progress",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 6. Interactive Goal Items (Clicking opens respective Figma Bottom Sheet)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GoalItemRow(
                    icon = Icons.Default.Restaurant,
                    tint = colors.secondary,
                    bg = colors.secondaryContainer,
                    title = "Meals",
                    progressText = "${state.mealsCount}/3 meals",
                    isComplete = state.mealsCount >= 3,
                    onClick = { viewModel.openSheet(ActiveSheet.LOG_MEALS) }
                )

                GoalItemRow(
                    icon = Icons.AutoMirrored.Filled.DirectionsRun,
                    tint = colors.secondary,
                    bg = colors.secondaryContainer,
                    title = "Movement",
                    progressText = "${state.movementMinutes}/30 min",
                    isComplete = state.movementMinutes >= 30,
                    onClick = { viewModel.openSheet(ActiveSheet.LOG_MOVEMENT) }
                )

                GoalItemRow(
                    icon = Icons.Default.Opacity,
                    tint = colors.primary,
                    bg = colors.primaryContainer,
                    title = "Water",
                    progressText = "${state.waterCount}/8 glasses",
                    isComplete = state.waterCount >= 8,
                    onClick = { viewModel.openSheet(ActiveSheet.LOG_WATER) }
                )

                GoalItemRow(
                    icon = Icons.Default.SelfImprovement,
                    tint = colors.tertiary,
                    bg = colors.tertiaryContainer,
                    title = "Relaxation",
                    progressText = "${state.relaxationMinutes}/15 min",
                    isComplete = state.relaxationMinutes >= 15,
                    onClick = { viewModel.openSheet(ActiveSheet.LOG_RELAXATION) }
                )

                GoalItemRow(
                    icon = Icons.Default.Bedtime,
                    tint = colors.primary,
                    bg = colors.primaryContainer,
                    title = "Sleep",
                    progressText = "${state.sleepHours}/8 hrs",
                    isComplete = state.sleepHours >= 8,
                    onClick = { viewModel.openSheet(ActiveSheet.LOG_SLEEP) }
                )
            }
        }

        // 7. Today's Tip Card (Exact Figma Node 26-7089)
        item {
            AnimatedVisibility(
                visible = !state.sleepTipDismissed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.tipBackground)
                        .border(1.dp, colors.tertiary.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(colors.tertiary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = colors.tertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "LOG LAST NIGHT'S SLEEP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "You haven't logged sleep yet. Consistent, close-to-8-hr nights can help keep your hormones more balanced — avoiding phones and screens before bed makes it easier to fall asleep.",
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = colors.textSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { viewModel.dismissSleepTip() }) {
                                Text(
                                    text = "Remind me later",
                                    fontSize = 13.sp,
                                    color = colors.textSecondary
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { viewModel.markSleepDoneFromTip() },
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.primary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Mark as done",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 8. Live Notification Preview Card (Figma Frame `Live Notification Preview`)
        item {
            LiveNotificationPreviewCard(
                state = state,
                onFilterSelect = { viewModel.selectNotificationFilter(it) },
                onBeginClick = { viewModel.startMeditation() },
                onResetClick = { viewModel.resetMeditation() }
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// Quick action card for Cycle & Symptoms
@Composable
fun FigmaQuickActionCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val colors = BloomThemeTokens.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = colors.textSecondary
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = colors.textMuted,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// Goal Item Row matching Figma
@Composable
fun GoalItemRow(
    icon: ImageVector,
    tint: Color,
    bg: Color,
    title: String,
    progressText: String,
    isComplete: Boolean,
    onClick: () -> Unit
) {
    val colors = BloomThemeTokens.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = progressText,
                    fontSize = 12.sp,
                    color = if (isComplete) colors.secondary else colors.textSecondary,
                    fontWeight = if (isComplete) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Log $title",
                tint = colors.textMuted,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// Live Notification Preview Card matching Figma Frame `Live Notification Preview`
@Composable
fun LiveNotificationPreviewCard(
    state: HomeDashboardUiState,
    onFilterSelect: (String) -> Unit,
    onBeginClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    Card(
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Live Notification Preview",
                fontFamily = BloomDisplayFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "A preview of how Bloom could show live, real-time updates on the lock screen.",
                fontSize = 12.sp,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Filter pills: Meditation, Daily goal, Water
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Meditation", "Daily goal", "Water").forEach { pill ->
                    val isSel = state.notificationFilter == pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSel) colors.primaryContainer else colors.background)
                            .border(
                                width = if (isSel) 1.5.dp else 1.dp,
                                color = if (isSel) colors.primary else colors.border,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onFilterSelect(pill) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = pill,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) colors.primary else colors.textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated lockscreen container
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF2C2428))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "5:52 AM",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Thu, Jul 30",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Inner Lockscreen Notification Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Bloom",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "now",
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (state.notificationFilter == "Meditation") {
                                        "Meditation: ${state.formattedMeditationTime} remaining"
                                    } else if (state.notificationFilter == "Daily goal") {
                                        "Daily goal: ${state.completedGoalsCount}/5 complete"
                                    } else {
                                        "Water: ${state.waterCount}/8 glasses logged"
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = Color(0xFFFF85AB),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons: Begin / Pause and Reset
            Row(
                modifier = Modifier.fillMaxWidth(0.92f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onBeginClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (state.isMeditationRunning) "Pause" else "Begin",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onResetClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Reset", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Updates live while a meditation session is running — same pattern as an order-tracking or workout notification.",
                fontSize = 11.sp,
                color = colors.textMuted,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                modifier = Modifier.fillMaxWidth(0.95f)
            )
        }
    }
}

// =========================================================================
// TAB 1: INSIGHTS TAB (Exact Figma Frame `Insights`)
// =========================================================================
@Composable
fun InsightsTab(
    state: HomeDashboardUiState,
    viewModel: HomeViewModel
) {
    val colors = BloomThemeTokens.colors
    var selectedTimeframe by remember { mutableStateOf("Week") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Insights",
                fontFamily = BloomDisplayFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = colors.textPrimary
            )
            Text(
                text = "Your week at a glance, and what to try next",
                fontSize = 13.sp,
                color = colors.textSecondary
            )
        }

        // Section 1: "Your score" Card with Week Bar Chart (Exact Figma Insights)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your score",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textSecondary
                        )

                        // Week / Month Pill Selector
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.background)
                                .padding(2.dp)
                        ) {
                            listOf("Week", "Month").forEach { tf ->
                                val isSel = selectedTimeframe == tf
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) colors.surface else Color.Transparent)
                                        .clickable { selectedTimeframe = tf }
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tf,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSel) colors.textPrimary else colors.textMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "60%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Text(
                        text = "week average",
                        fontSize = 12.sp,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 7-day Bar Chart (M T W T F S S)
                    val days = listOf("M", "T", "W", "T", "F", "S", "S")
                    val heights = listOf(0.7f, 0.5f, 0.8f, 0.6f, 0.9f, 0.4f, 0.3f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { idx, day ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height((70 * heights[idx]).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(colors.primary.copy(alpha = 0.35f + heights[idx] * 0.65f))
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tap a day to see its summary",
                        fontSize = 11.sp,
                        color = colors.textMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Section 2: Cycle Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
                modifier = Modifier.clickable { viewModel.openSheet(ActiveSheet.LOG_CYCLE) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(colors.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Cycle", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                        Text(text = state.cycleStatus, fontSize = 12.sp, color = colors.textSecondary)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(12.dp))
                }
            }
        }

        // Section 3: Daily Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Daily summary", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${(state.completionProgress * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (state.completedGoalsCount == 0) "Today · Nothing logged yet today — no rush, tap a goal below whenever you're ready." else "Today · ${state.completedGoalsCount} of ${state.totalGoalsCount} daily goals completed.",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }

        // Section 4: Symptom Patterns Card (Exact Figma Insights)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "Symptom patterns", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)

                    SymptomPatternRow(title = "Bloating", summary = "Moderate or higher on 5 of 7 days", activeCount = 5)
                    SymptomPatternRow(title = "Skin & acne", summary = "Moderate or higher on 2 of 7 days", activeCount = 2)
                    SymptomPatternRow(title = "Mood", summary = "Moderate or higher on 2 of 7 days", activeCount = 2)
                    SymptomPatternRow(title = "Sleep quality", summary = "Moderate or higher on 2 of 7 days", activeCount = 2)
                }
            }
        }
    }
}

@Composable
fun SymptomPatternRow(
    title: String,
    summary: String,
    activeCount: Int
) {
    val colors = BloomThemeTokens.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = summary, fontSize = 11.sp, color = colors.textSecondary)
        }

        // 7 mini vertical bars matching Figma
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            for (i in 1..7) {
                val isActive = i <= activeCount
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isActive) colors.primary else colors.progressTrack)
                )
            }
        }
    }
}

// =========================================================================
// TAB 2: LEARN TAB (Exact Figma Frame `Learn`)
// =========================================================================
@Composable
fun LearnTab(
    onSelectFaq: (com.newage.bloom.data.PcosFaqItem) -> Unit
) {
    val colors = BloomThemeTokens.colors
    val sections = listOf(
        "Diagnosis & basics" to listOf(
            "What exactly is PCOS?",
            "What does PCOS stand for — and do I actually have cysts on my ovaries?",
            "What causes PCOS?",
            "How is PCOS actually diagnosed?",
            "Is PCOS the same for everyone?"
        ),
        "Symptoms & body" to listOf(
            "Why are my periods irregular?",
            "Why does weight feel harder to manage?",
            "What is the deal with insulin resistance?",
            "Is the acne and hair growth from PCOS treatable?"
        ),
        "Managing it day to day" to listOf(
            "Can food really make a difference?",
            "What foods should I eat — and which should I limit?",
            "How can I manage insulin resistance day to day?",
            "Will I need medication forever?",
            "How does stress fit into all this?"
        ),
        "Looking ahead" to listOf(
            "Does PCOS affect fertility?",
            "What happens if PCOS is left unmanaged?"
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Learn",
                fontFamily = BloomDisplayFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = colors.textPrimary
            )
            Text(
                text = "Understand PCOS at your own pace",
                fontSize = 13.sp,
                color = colors.textSecondary
            )
        }

        sections.forEachIndexed { secIdx, (category, questions) ->
            item {
                Text(
                    text = category,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(questions.size) { qIdx ->
                val q = questions[qIdx]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.border, RoundedCornerShape(14.dp))
                        .clickable {
                            onSelectFaq(
                                com.newage.bloom.data.PcosFaqItem(
                                    id = "learn_${secIdx}_$qIdx",
                                    category = category,
                                    question = q,
                                    answer = "Medical Overview for '$q': Polycystic Ovary Syndrome involves endocrine and metabolic pathways. Balanced nutrition with protein and fiber, regular movement, restorative sleep, and stress reduction help lower circulating androgens and restore hormone balance over time."
                                )
                            )
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = q,
                            fontSize = 13.sp,
                            color = colors.textPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = colors.textMuted,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// TAB 3: SETTINGS TAB (Exact Figma Frame `Settings` + Live Theme Switcher)
// =========================================================================
@Composable
fun SettingsTab(
    state: HomeDashboardUiState,
    viewModel: HomeViewModel,
    onResetOnboarding: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Settings",
                fontFamily = BloomDisplayFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = colors.textPrimary
            )
            Text(
                text = "Your profile and daily goal targets",
                fontSize = 13.sp,
                color = colors.textSecondary
            )
        }

        // 1. Profile Card (Exact Figma Settings)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
                modifier = Modifier.clickable { viewModel.openSheet(ActiveSheet.EDIT_PROFILE) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(colors.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = colors.primary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = state.userName, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "PCOS journey · ${state.pcosStage}", fontSize = 12.sp, color = colors.textSecondary)
                    }
                    Text(text = "Edit profile", fontSize = 12.sp, color = colors.primary, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = colors.primary, modifier = Modifier.size(11.dp))
                }
            }
        }

        // 2. Complete Light, Dark & System Theme Selector
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Appearance & Theme",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Select your preferred visual style. Updates instantly across all screens.",
                        fontSize = 12.sp,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val themeOptions = listOf(
                        Triple("SYSTEM", "System Default", Icons.Default.SettingsBrightness),
                        Triple("LIGHT", "Light Mode", Icons.Default.LightMode),
                        Triple("DARK", "Dark Mode", Icons.Default.DarkMode)
                    )

                    themeOptions.forEach { (modeKey, label, icon) ->
                        val isSelected = state.themeMode == modeKey
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) colors.primaryContainer else colors.background)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) colors.primary else colors.border,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { viewModel.setThemeMode(modeKey) }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) colors.primary else colors.textSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = label,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) colors.primary else colors.textPrimary
                                    )
                                }

                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.setThemeMode(modeKey) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = colors.primary,
                                        unselectedColor = colors.border
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. "What you're tracking" Section (Exact Figma Settings)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "What you're tracking", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)

                    val trackingItems = listOf(
                        Triple("Meals", Icons.Default.Restaurant, colors.secondary),
                        Triple("Movement", Icons.AutoMirrored.Filled.DirectionsRun, colors.secondary),
                        Triple("Water", Icons.Default.Opacity, colors.primary),
                        Triple("Relaxation", Icons.Default.SelfImprovement, colors.tertiary),
                        Triple("Sleep", Icons.Default.Bedtime, colors.primary),
                        Triple("Cycle", Icons.Default.DateRange, colors.secondary),
                        Triple("Symptom check-in", Icons.Default.Favorite, colors.primary)
                    )

                    trackingItems.forEach { (title, icon, color) ->
                        var isChecked by remember { mutableStateOf(true) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(28.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(15.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = title, fontSize = 13.sp, color = colors.textPrimary)
                            }
                            Switch(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = colors.surface,
                                    checkedTrackColor = colors.primary,
                                    uncheckedThumbColor = colors.surface,
                                    uncheckedTrackColor = colors.border
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4. "Daily goal targets" Section (Exact Figma Settings)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Daily goal targets", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Meals", fontSize = 13.sp, color = colors.textSecondary)
                        Text(text = "3 meals", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Movement", fontSize = 13.sp, color = colors.textSecondary)
                        Text(text = "30 min", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Water", fontSize = 13.sp, color = colors.textSecondary)
                        Text(text = "8 glasses", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Relaxation", fontSize = 13.sp, color = colors.textSecondary)
                        Text(text = "15 min", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Sleep", fontSize = 13.sp, color = colors.textSecondary)
                        Text(text = "8 hrs", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    }
                }
            }
        }

        // Demo Reset Button
        item {
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedButton(
                onClick = onResetOnboarding,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset Onboarding Flow (Demo Mode)", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// =========================================================================
// ATTRACTIVE FLOATING DOCK BOTTOM NAVIGATION BAR
// =========================================================================
@Composable
fun BloomBottomNavBar(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BloomThemeTokens.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Floating pill container with ambient drop shadow & subtle border
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = colors.primary.copy(alpha = 0.12f),
                    spotColor = colors.primary.copy(alpha = 0.20f)
                )
                .clip(RoundedCornerShape(26.dp))
                .background(colors.surface)
                .border(1.dp, colors.border.copy(alpha = 0.85f), RoundedCornerShape(26.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                BloomNavItemData(0, "Dashboard", Icons.Filled.Home, Icons.Outlined.Home),
                BloomNavItemData(1, "Insights", Icons.Filled.Insights, Icons.Outlined.Insights),
                BloomNavItemData(2, "Learn", Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook),
                BloomNavItemData(3, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
            )

            navItems.forEach { item ->
                BloomNavBarItem(
                    item = item,
                    isSelected = selectedTab == item.index,
                    onClick = { onSelectTab(item.index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private data class BloomNavItemData(
    val index: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
private fun BloomNavBarItem(
    item: BloomNavItemData,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BloomThemeTokens.colors

    // Smooth spring scale animation on selection
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.12f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "nav_scale_${item.index}"
    )

    // Animated color transitions
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.textMuted,
        animationSpec = tween(220),
        label = "nav_icon_color_${item.index}"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.textMuted,
        animationSpec = tween(220),
        label = "nav_text_color_${item.index}"
    )

    // Animated background capsule opacity
    val bgAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(250),
        label = "nav_bg_alpha_${item.index}"
    )

    // Animated indicator pill width underneath
    val indicatorWidth by animateDpAsState(
        targetValue = if (isSelected) 14.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "nav_indicator_width_${item.index}"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.primaryContainer.copy(alpha = bgAlpha * 0.75f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                tint = iconColor,
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                    }
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Signature rose indicator dot/pill
            Box(
                modifier = Modifier
                    .width(indicatorWidth)
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(if (isSelected) colors.primary else Color.Transparent)
            )
        }
    }
}
