package com.newage.bloom.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newage.bloom.ui.components.BloomFlowerLogo
import com.newage.bloom.ui.components.BloomPrimaryButton
import com.newage.bloom.ui.components.OnboardingHeader
import com.newage.bloom.ui.theme.BloomDisplayFont
import com.newage.bloom.ui.theme.BloomThemeTokens

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val colors = BloomThemeTokens.colors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            OnboardingHeader(
                currentStep = state.currentStep,
                totalSteps = 4,
                onBackClick = if (state.currentStep > 1) {
                    { viewModel.previousStep() }
                } else null
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = state.currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                        } else {
                            slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                        }
                    },
                    label = "OnboardingStepTransition"
                ) { step ->
                    when (step) {
                        1 -> OnboardingStep1Welcome(
                            onContinue = { viewModel.nextStep() }
                        )
                        2 -> OnboardingStep2AboutYou(
                            userName = state.userName,
                            onNameChange = { viewModel.updateName(it) },
                            selectedStage = state.pcosStage,
                            onSelectStage = { viewModel.selectPcosStage(it) },
                            onContinue = { viewModel.nextStep() }
                        )
                        3 -> OnboardingStep3DailyGoals(
                            selectedGoals = state.selectedGoals,
                            onToggleGoal = { viewModel.toggleGoal(it) },
                            onContinue = { viewModel.nextStep() }
                        )
                        4 -> OnboardingStep4Reminders(
                            selectedReminder = state.selectedReminder,
                            customTime = state.customTime,
                            onSelectReminder = { period, text -> viewModel.selectReminder(period, text) },
                            onCustomTimeChange = { viewModel.updateCustomTime(it) },
                            onEnterBloom = { viewModel.completeOnboarding(onFinishOnboarding) },
                            onSkip = { viewModel.completeOnboarding(onFinishOnboarding) }
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// STEP 1: WELCOME TO BLOOM (Exact Figma Screen)
// =========================================================================
@Composable
fun OnboardingStep1Welcome(
    onContinue: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Hero Flower Logo
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(colors.primaryContainer.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            BloomFlowerLogo(
                size = 96.dp,
                petalColor = colors.primary,
                centerColor = colors.tertiary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to Bloom",
            fontFamily = BloomDisplayFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            color = colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Your daily companion for PCOS — tracking what matters, understanding your body, and building habits that stick.",
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.weight(1.3f))

        BloomPrimaryButton(
            text = "Get started",
            onClick = onContinue,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

// =========================================================================
// STEP 2: A LITTLE ABOUT YOU (Exact Figma Screen)
// =========================================================================
@Composable
fun OnboardingStep2AboutYou(
    userName: String,
    onNameChange: (String) -> Unit,
    selectedStage: String,
    onSelectStage: (String) -> Unit,
    onContinue: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = "A little about you",
            fontFamily = BloomDisplayFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Let's personalize your daily check-in",
            fontSize = 14.sp,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Name Section
        Text(
            text = "What should we call you?",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.surface)
                .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = userName,
                onValueChange = onNameChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // PCOS Stage Section
        Text(
            text = "Where are you on your journey?",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        val stages = listOf(
            "Diagnosed" to "I have a confirmed PCOS diagnosis",
            "Suspected" to "A doctor mentioned it, not confirmed",
            "Exploring" to "I think I might have symptoms",
            "Not sure yet" to "I just want to learn more"
        )

        stages.forEach { (title, subtitle) ->
            val isSelected = selectedStage == title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) colors.primaryContainer else colors.surface)
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) colors.primary else colors.border,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelectStage(title) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectStage(title) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colors.primary,
                            unselectedColor = colors.border
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
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
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        BloomPrimaryButton(
            text = "Continue",
            onClick = onContinue,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

// =========================================================================
// STEP 3: WHAT WOULD YOU LIKE TO TRACK DAILY? (Exact Figma Screen)
// =========================================================================
@Composable
fun OnboardingStep3DailyGoals(
    selectedGoals: Set<String>,
    onToggleGoal: (String) -> Unit,
    onContinue: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // Star / Flower icon at top
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "What would you like to track daily?",
            fontFamily = BloomDisplayFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Pick the goals that matter to you — only these will show up on your dashboard. Partial progress still counts, and your flower fills in as you go.",
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        val goals = listOf(
            GoalConfig("meals", "Meals · 3 balanced meals", "Steadier blood sugar helps keep PCOS symptoms in check", Icons.Default.Restaurant, colors.secondary),
            GoalConfig("movement", "Movement · 30 minutes", "Supports insulin sensitivity, a key lever in PCOS", Icons.AutoMirrored.Filled.DirectionsRun, colors.secondary),
            GoalConfig("water", "Water · 8 glasses", "Helps with energy and bloating", Icons.Default.Opacity, colors.primary),
            GoalConfig("relaxation", "Relaxation · 15 minutes", "Lower stress means lower cortisol, which can worsen symptoms", Icons.Default.SelfImprovement, colors.tertiary),
            GoalConfig("sleep", "Sleep · 8 hours", "Consistent sleep helps balance hormones over time", Icons.Default.Bedtime, colors.primary),
            GoalConfig("cycle", "Cycle · period dates", "Spot your personal pattern and predict your next period", Icons.Default.DateRange, colors.secondary),
            GoalConfig("symptoms", "Symptom check-in · bloating, skin, mood, sleep quality", "Track how you feel day to day to notice patterns over time", Icons.Default.Favorite, colors.primary)
        )

        goals.forEach { goal ->
            val isEnabled = selectedGoals.contains(goal.key)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.surface)
                    .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(goal.tint.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = goal.icon,
                                contentDescription = null,
                                tint = goal.tint,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = goal.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = goal.subtitle,
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = colors.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { onToggleGoal(goal.key) },
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

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Targets and what you track can be adjusted any time in Settings.",
            fontSize = 11.sp,
            color = colors.textMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        BloomPrimaryButton(
            text = "Continue",
            onClick = onContinue,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

private data class GoalConfig(
    val key: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val tint: Color
)

// =========================================================================
// STEP 4: WHEN SHOULD WE REMIND YOU? (Exact Figma Screen)
// =========================================================================
@Composable
fun OnboardingStep4Reminders(
    selectedReminder: String,
    customTime: String,
    onSelectReminder: (String, String) -> Unit,
    onCustomTimeChange: (String) -> Unit,
    onEnterBloom: () -> Unit,
    onSkip: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // Clock icon at top
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "When should we remind you?",
            fontFamily = BloomDisplayFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "We'll send one daily nudge to log how you're doing.",
            fontSize = 13.sp,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Pick a time of day",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        val reminderOptions = listOf(
            Triple("Morning", "7:30 AM — start the day on track", Icons.Default.WbSunny),
            Triple("Afternoon", "1:00 PM — a midday check-in", Icons.Default.WbTwilight),
            Triple("Evening", "8:00 PM — wind down and reflect", Icons.Default.Bedtime)
        )

        reminderOptions.forEach { (period, desc, icon) ->
            val isSelected = selectedReminder == period
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) colors.primaryContainer else colors.surface)
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) colors.primary else colors.border,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelectReminder(period, "$period (${desc.substringBefore(" —")})") }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) colors.primary.copy(alpha = 0.2f) else colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) colors.primary else colors.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = period,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = colors.textSecondary
                        )
                    }

                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectReminder(period, "$period (${desc.substringBefore(" —")})") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colors.primary,
                            unselectedColor = colors.border
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Or set a custom time",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.surface)
                .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = customTime,
                    onValueChange = onCustomTimeChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        BloomPrimaryButton(
            text = "Enter Bloom",
            onClick = onEnterBloom
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "I'll set this up later",
                fontSize = 13.sp,
                color = colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
