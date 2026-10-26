package com.newage.bloom.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newage.bloom.data.OnboardingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val currentStep: Int = 1,
    val userName: String = "Sofia",
    val pcosStage: String = "Diagnosed",
    val selectedGoals: Set<String> = setOf("meals", "movement", "water", "relaxation", "sleep", "cycle", "symptoms"),
    val selectedReminder: String = "Morning", // "Morning", "Afternoon", "Evening", "Custom"
    val reminderTimeText: String = "Morning (7:30 AM)",
    val customTime: String = "08:00 AM",
    val isCompleted: Boolean = false
)

class OnboardingViewModel(
    private val preferences: OnboardingPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextStep() {
        if (_uiState.value.currentStep < 4) {
            _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep + 1)
        }
    }

    fun previousStep() {
        if (_uiState.value.currentStep > 1) {
            _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep - 1)
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(userName = name)
    }

    fun selectPcosStage(stage: String) {
        _uiState.value = _uiState.value.copy(pcosStage = stage)
    }

    fun toggleGoal(goalKey: String) {
        val currentGoals = _uiState.value.selectedGoals.toMutableSet()
        if (currentGoals.contains(goalKey)) {
            currentGoals.remove(goalKey)
        } else {
            currentGoals.add(goalKey)
        }
        _uiState.value = _uiState.value.copy(selectedGoals = currentGoals)
    }

    fun selectReminder(period: String, formattedText: String) {
        _uiState.value = _uiState.value.copy(
            selectedReminder = period,
            reminderTimeText = formattedText
        )
    }

    fun updateCustomTime(time: String) {
        _uiState.value = _uiState.value.copy(
            customTime = time,
            selectedReminder = "Custom",
            reminderTimeText = "Custom ($time)"
        )
    }

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val effectiveName = state.userName.trim().ifEmpty { "Sofia" }
            preferences.saveUserProfile(effectiveName, state.pcosStage, "Track my cycle")
            preferences.saveSelectedGoals(state.selectedGoals)
            preferences.saveReminderTime(state.reminderTimeText)
            preferences.setOnboardingCompleted(true)
            _uiState.value = state.copy(isCompleted = true)
            onSuccess()
        }
    }
}
