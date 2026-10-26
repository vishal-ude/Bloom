package com.newage.bloom.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bloom_user_preferences")

class OnboardingPreferences(private val context: Context) {

    companion object {
        val KEY_IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_PCOS_STAGE = stringPreferencesKey("pcos_stage")
        val KEY_FOCUS_AREA = stringPreferencesKey("focus_area")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode") // "SYSTEM", "LIGHT", "DARK"
        val KEY_REMINDER_TIME = stringPreferencesKey("reminder_time")
        val KEY_SELECTED_GOALS = stringSetPreferencesKey("selected_goals")

        // Interactive Tracking Progress
        val KEY_WATER_COUNT = intPreferencesKey("water_glasses_count")
        val KEY_MEALS_COUNT = intPreferencesKey("meals_count")
        val KEY_MOVEMENT_MINUTES = intPreferencesKey("movement_minutes")
        val KEY_RELAXATION_MINUTES = intPreferencesKey("relaxation_minutes")
        val KEY_SLEEP_HOURS = intPreferencesKey("sleep_hours")
        val KEY_SLEEP_TIP_DISMISSED = booleanPreferencesKey("sleep_tip_dismissed")
        val KEY_CYCLE_LOGGED = booleanPreferencesKey("cycle_logged_today")
        val KEY_CYCLE_DAY = intPreferencesKey("cycle_day")
        val KEY_SYMPTOM_STATUS = stringPreferencesKey("symptom_status")
    }

    val isOnboardingCompletedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_IS_ONBOARDING_COMPLETED] ?: false
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_NAME]?.ifBlank { "Sofia" } ?: "Sofia"
    }

    val pcosStageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_PCOS_STAGE] ?: "Not sure yet"
    }

    val focusAreaFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_FOCUS_AREA] ?: "Track my cycle"
    }

    val themeModeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_THEME_MODE] ?: "SYSTEM"
    }

    val reminderTimeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_REMINDER_TIME] ?: "Morning (7:30 AM)"
    }

    val selectedGoalsFlow: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[KEY_SELECTED_GOALS] ?: setOf("meals", "movement", "water", "relaxation", "sleep", "cycle", "symptoms")
    }

    val waterCountFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_WATER_COUNT] ?: 1
    }

    val mealsCountFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_MEALS_COUNT] ?: 2
    }

    val movementMinutesFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_MOVEMENT_MINUTES] ?: 20
    }

    val relaxationMinutesFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_RELAXATION_MINUTES] ?: 10
    }

    val sleepHoursFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_SLEEP_HOURS] ?: 7
    }

    val sleepTipDismissedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_SLEEP_TIP_DISMISSED] ?: false
    }

    val cycleLoggedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_CYCLE_LOGGED] ?: false
    }

    val cycleDayFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_CYCLE_DAY] ?: 30
    }

    val symptomStatusFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_SYMPTOM_STATUS] ?: "Bloating, skin, mood, sleep quality"
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun saveUserProfile(name: String, stage: String, focus: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_NAME] = name
            preferences[KEY_PCOS_STAGE] = stage
            preferences[KEY_FOCUS_AREA] = focus
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode
        }
    }

    suspend fun saveSelectedGoals(goals: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_GOALS] = goals
        }
    }

    suspend fun saveReminderTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_REMINDER_TIME] = time
        }
    }

    suspend fun updateWaterCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_WATER_COUNT] = count
        }
    }

    suspend fun updateMealsCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MEALS_COUNT] = count
        }
    }

    suspend fun updateMovementMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MOVEMENT_MINUTES] = minutes
        }
    }

    suspend fun updateRelaxationMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_RELAXATION_MINUTES] = minutes
        }
    }

    suspend fun updateSleepHours(hours: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SLEEP_HOURS] = hours
        }
    }

    suspend fun setSleepTipDismissed(dismissed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SLEEP_TIP_DISMISSED] = dismissed
        }
    }

    suspend fun setCycleLogged(logged: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CYCLE_LOGGED] = logged
        }
    }

    suspend fun saveCycleLog(day: Int, logged: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CYCLE_DAY] = day
            preferences[KEY_CYCLE_LOGGED] = logged
        }
    }

    suspend fun saveSymptomStatus(status: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SYMPTOM_STATUS] = status
        }
    }

    suspend fun resetOnboardingForTesting() {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_ONBOARDING_COMPLETED] = false
            preferences[KEY_USER_NAME] = "Sofia"
            preferences[KEY_PCOS_STAGE] = "Diagnosed"
            preferences[KEY_FOCUS_AREA] = "Track my cycle"
            preferences[KEY_WATER_COUNT] = 1
            preferences[KEY_MEALS_COUNT] = 2
            preferences[KEY_MOVEMENT_MINUTES] = 20
            preferences[KEY_RELAXATION_MINUTES] = 10
            preferences[KEY_SLEEP_HOURS] = 7
            preferences[KEY_SLEEP_TIP_DISMISSED] = false
            preferences[KEY_CYCLE_LOGGED] = false
            preferences[KEY_CYCLE_DAY] = 30
            preferences[KEY_SYMPTOM_STATUS] = "Bloating, skin, mood, sleep quality"
        }
    }
}
