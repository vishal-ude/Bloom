package com.newage.bloom.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newage.bloom.ui.components.BloomPrimaryButton
import com.newage.bloom.ui.theme.BloomDisplayFont
import com.newage.bloom.ui.theme.BloomThemeTokens

enum class ActiveSheet {
    LOG_CYCLE,
    TODAYS_SYMPTOMS,
    LOG_MEALS,
    LOG_MOVEMENT,
    LOG_WATER,
    LOG_RELAXATION,
    LOG_SLEEP,
    EDIT_PROFILE
}

// =========================================================================
// 1. LOG CYCLE BOTTOM SHEET (Frame 26080030)
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LogCycleBottomSheet(
    sheetState: SheetState,
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val colors = BloomThemeTokens.colors
    var isPeriodDay by remember { mutableStateOf(true) }
    val symptomsList = listOf("Bloating", "Cramps", "Acne", "Mood swings", "Fatigue", "Cravings")
    val selectedSymptoms = remember { mutableStateListOf("Bloating", "Cramps") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.border)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Log Cycle",
                    fontFamily = BloomDisplayFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = colors.textPrimary
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Is today a period day?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { isPeriodDay = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPeriodDay) colors.primary else colors.surfaceVariant,
                        contentColor = if (isPeriodDay) Color.White else colors.textPrimary
                    ),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Icon(imageVector = Icons.Default.LocalFlorist, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Yes, log it", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = { isPeriodDay = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (!isPeriodDay) colors.primaryContainer else Color.Transparent,
                        contentColor = if (!isPeriodDay) colors.primary else colors.textPrimary
                    ),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Text(text = "Not today", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Any symptoms today?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                symptomsList.forEach { symptom ->
                    val isSelected = selectedSymptoms.contains(symptom)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) colors.primaryContainer else colors.surfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) colors.primary else colors.border,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                if (isSelected) selectedSymptoms.remove(symptom) else selectedSymptoms.add(symptom)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = symptom,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) colors.primary else colors.textPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            BloomPrimaryButton(
                text = "Save entry",
                onClick = onSave
            )
        }
    }
}

// =========================================================================
// 2. TODAY'S SYMPTOMS BOTTOM SHEET (Frame 26080030)
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodaysSymptomsBottomSheet(
    sheetState: SheetState,
    bloating: String,
    skin: String,
    mood: String,
    sleep: String,
    onSelectBloating: (String) -> Unit,
    onSelectSkin: (String) -> Unit,
    onSelectMood: (String) -> Unit,
    onSelectSleep: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.border)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's symptoms",
                    fontFamily = BloomDisplayFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = colors.textPrimary
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "These are separate from your daily goals — logging them just helps you (and eventually your doctor) spot patterns over time.",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
            )

            SymptomPillRow("Bloating", listOf("None", "Mild", "Moderate", "Severe"), bloating, onSelectBloating)
            Spacer(modifier = Modifier.height(14.dp))

            SymptomPillRow("Skin & acne", listOf("Clear", "Mild", "Moderate", "Flare-up"), skin, onSelectSkin)
            Spacer(modifier = Modifier.height(14.dp))

            SymptomPillRow("Mood", listOf("Great", "Okay", "Low", "Very low"), mood, onSelectMood)
            Spacer(modifier = Modifier.height(14.dp))

            SymptomPillRow("Sleep quality", listOf("Great", "Okay", "Poor", "Very poor"), sleep, onSelectSleep)
            Spacer(modifier = Modifier.height(24.dp))

            BloomPrimaryButton(text = "Done", onClick = onSave)
        }
    }
}

@Composable
fun SymptomPillRow(
    title: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    val colors = BloomThemeTokens.colors
    Column {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEach { option ->
                val isSelected = selectedOption == option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) colors.primaryContainer else colors.background)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) colors.primary else colors.border,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { onSelect(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) colors.primary else colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// =========================================================================
// 3. LOG MEALS BOTTOM SHEET (Frame 26080030)
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMealsBottomSheet(
    sheetState: SheetState,
    breakfast: String,
    lunch: String,
    dinner: String,
    onSelectBreakfast: (String) -> Unit,
    onSelectLunch: (String) -> Unit,
    onSelectDinner: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.border)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Log Meals",
                    fontFamily = BloomDisplayFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = colors.textPrimary
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                }
            }

            Text(
                text = "How would you describe each meal today?",
                fontSize = 13.sp,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
            )

            MealSection(mealName = "Breakfast", selectedType = breakfast, onSelect = onSelectBreakfast)
            Spacer(modifier = Modifier.height(14.dp))

            MealSection(mealName = "Lunch", selectedType = lunch, onSelect = onSelectLunch)
            Spacer(modifier = Modifier.height(14.dp))

            MealSection(mealName = "Dinner", selectedType = dinner, onSelect = onSelectDinner)
            Spacer(modifier = Modifier.height(20.dp))

            var loggedCount = 0
            if (breakfast.isNotEmpty()) loggedCount++
            if (lunch.isNotEmpty()) loggedCount++
            if (dinner.isNotEmpty()) loggedCount++

            Text(
                text = "$loggedCount of 3 meals logged today",
                fontSize = 12.sp,
                color = colors.textMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            BloomPrimaryButton(text = "Done", onClick = onSave)
        }
    }
}

@Composable
fun MealSection(
    mealName: String,
    selectedType: String,
    onSelect: (String) -> Unit
) {
    val colors = BloomThemeTokens.colors
    val options = listOf("Light", "Balanced", "Indulgent", "Skipped")
    Column {
        Text(text = "● $mealName", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.take(2).forEach { opt ->
                MealOptionButton(opt, selectedType == opt, { onSelect(opt) }, Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.drop(2).forEach { opt ->
                MealOptionButton(opt, selectedType == opt, { onSelect(opt) }, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MealOptionButton(
    option: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BloomThemeTokens.colors
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) colors.primaryContainer else colors.background)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) colors.primary else colors.border,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) colors.primary else colors.textPrimary
        )
    }
}

// =========================================================================
// 4. HABIT STEPPER BOTTOM SHEET
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitStepperBottomSheet(
    sheetState: SheetState,
    title: String,
    currentValue: Int,
    targetValue: Int,
    unit: String,
    stepAmount: Int,
    motivationalTitle: String,
    motivationalText: String,
    quickAddText: String,
    onValueChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val colors = BloomThemeTokens.colors

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.border)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = BloomDisplayFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = colors.textPrimary
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stepper Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colors.background)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable { onValueChange((currentValue - stepAmount).coerceAtLeast(0)) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = colors.textPrimary, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$currentValue",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(text = unit, fontSize = 13.sp, color = colors.textSecondary)
                }

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colors.background)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable { onValueChange(currentValue + stepAmount) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = colors.textPrimary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Goal: $targetValue $unit · adjust in Settings",
                fontSize = 12.sp,
                color = colors.textMuted
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Motivation Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.background)
                    .border(1.dp, colors.border, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "● $motivationalTitle", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = motivationalText, fontSize = 12.sp, lineHeight = 16.sp, color = colors.textSecondary)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { onValueChange(currentValue + stepAmount) },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = quickAddText, fontSize = 12.sp, color = colors.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BloomPrimaryButton(text = "Done", onClick = onSave)
        }
    }
}

// =========================================================================
// 5. EDIT PROFILE BOTTOM SHEET
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBottomSheet(
    sheetState: SheetState,
    initialName: String,
    initialStage: String,
    onDismiss: () -> Unit,
    onSaveProfile: (String, String) -> Unit
) {
    val colors = BloomThemeTokens.colors
    var name by remember { mutableStateOf(initialName) }
    var stage by remember { mutableStateOf(initialStage) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.border)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit Profile",
                    fontFamily = BloomDisplayFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = colors.textPrimary
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Your first name", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.background)
                    .border(1.dp, colors.border, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(text = "Journey stage", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            val stages = listOf(
                "Diagnosed" to "I have a confirmed PCOS diagnosis",
                "Suspected" to "A doctor mentioned it, not confirmed",
                "Exploring" to "I think I might have symptoms",
                "Not sure yet" to "I just want to learn more"
            )

            stages.forEach { (title, subtitle) ->
                val isSelected = stage == title
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
                        .clickable { stage = title }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { stage = title },
                            colors = RadioButtonDefaults.colors(selectedColor = colors.primary, unselectedColor = colors.border)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                            Text(text = subtitle, fontSize = 11.sp, color = colors.textSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BloomPrimaryButton(
                text = "Save",
                onClick = { onSaveProfile(name.trim().ifEmpty { "Nabila" }, stage) }
            )
        }
    }
}
