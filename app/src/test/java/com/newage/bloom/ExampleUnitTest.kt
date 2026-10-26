package com.newage.bloom

import com.newage.bloom.ui.home.HomeDashboardUiState
import com.newage.bloom.ui.onboarding.OnboardingUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun onboardingUiState_defaultsMatchFigmaRequirements() {
        val state = OnboardingUiState()
        assertEquals(1, state.currentStep)
        assertEquals("Sofia", state.userName)
        assertEquals("Diagnosed", state.pcosStage)
        assertTrue(state.selectedGoals.contains("meals"))
        assertTrue(state.selectedGoals.contains("cycle"))
        assertFalse(state.isCompleted)
    }

    @Test
    fun homeDashboardUiState_partialHabits_zeroCompletedGoals() {
        val state = HomeDashboardUiState(
            mealsCount = 2,           // target 3
            movementMinutes = 20,     // target 30
            waterCount = 1,           // target 8
            relaxationMinutes = 10,   // target 15
            sleepHours = 7            // target 8
        )
        assertEquals(0, state.completedGoalsCount)
        assertEquals(0.0f, state.completionProgress, 0.001f)
    }

    @Test
    fun homeDashboardUiState_allHabitsMet_fiveCompletedGoals() {
        val state = HomeDashboardUiState(
            mealsCount = 3,
            movementMinutes = 30,
            waterCount = 8,
            relaxationMinutes = 15,
            sleepHours = 8
        )
        assertEquals(5, state.completedGoalsCount)
        assertEquals(1.0f, state.completionProgress, 0.001f)
    }

    @Test
    fun homeDashboardUiState_formattedMeditationTime_formatsCorrectly() {
        val state5Min = HomeDashboardUiState(meditationRemainingSeconds = 300)
        assertEquals("5:00", state5Min.formattedMeditationTime)

        val state65Sec = HomeDashboardUiState(meditationRemainingSeconds = 65)
        assertEquals("1:05", state65Sec.formattedMeditationTime)

        val state0Sec = HomeDashboardUiState(meditationRemainingSeconds = 0)
        assertEquals("0:00", state0Sec.formattedMeditationTime)
    }
}