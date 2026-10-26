package com.newage.bloom.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newage.bloom.data.OnboardingPreferences
import com.newage.bloom.data.PcosFaqItem
import com.newage.bloom.ui.sheets.ActiveSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HomeDashboardUiState(
    val userName: String = "Sofia",
    val currentDateFormatted: String = "",
    val pcosStage: String = "Not set",
    val focusArea: String = "Track my cycle",
    val themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
    val cycleLogged: Boolean = false,
    val mealsLogged: Boolean = false,
    val lifestyleLogged: Boolean = false,
    val cycleStatus: String = "No cycle logged yet · Log your last period date",
    val selectedCycleDay: Int = 30,
    val symptomStatus: String = "Bloating, skin, mood, sleep quality",
    val symptomBloating: String = "None",
    val symptomSkin: String = "Clear",
    val symptomMood: String = "Great",
    val symptomSleep: String = "Great",
    val mealBreakfast: String = "",
    val mealLunch: String = "",
    val mealDinner: String = "",
    val waterCount: Int = 0,
    val mealsCount: Int = 0,
    val movementMinutes: Int = 0,
    val relaxationMinutes: Int = 0,
    val sleepHours: Int = 0,
    val sleepTipDismissed: Boolean = false,
    val activeTab: Int = 0, // 0 = Dashboard, 1 = Insights, 2 = Learn, 3 = Settings
    val selectedFaqIndex: Int = 0,
    val selectedFaqDetail: PcosFaqItem? = null,
    val activeSheet: ActiveSheet? = null,
    // Live Notification Preview State (Figma Frame)
    val notificationFilter: String = "Meditation",
    val meditationRemainingSeconds: Int = 300,
    val isMeditationRunning: Boolean = false
) {
    // 5 daily habit goals matching Figma Home Dashboard
    val completedGoalsCount: Int
        get() {
            var count = 0
            if (mealsCount >= 3) count++
            if (movementMinutes >= 30) count++
            if (waterCount >= 8) count++
            if (relaxationMinutes >= 15) count++
            if (sleepHours >= 8) count++
            return count
        }

    val totalGoalsCount: Int = 5

    val completionProgress: Float
        get() = completedGoalsCount.toFloat() / totalGoalsCount.toFloat()

    val formattedMeditationTime: String
        get() {
            val minutes = meditationRemainingSeconds / 60
            val seconds = meditationRemainingSeconds % 60
            return "%d:%02d".format(minutes, seconds)
        }
}

class HomeViewModel(
    private val preferences: OnboardingPreferences
) : ViewModel() {

    private val _activeTab = MutableStateFlow(0)
    private val _selectedCycleDay = MutableStateFlow(30)
    private val _mealsLogged = MutableStateFlow(false)
    private val _lifestyleLogged = MutableStateFlow(false)
    private val _symptomBloating = MutableStateFlow("None")
    private val _symptomSkin = MutableStateFlow("Clear")
    private val _symptomMood = MutableStateFlow("Great")
    private val _symptomSleep = MutableStateFlow("Great")
    private val _mealBreakfast = MutableStateFlow("")
    private val _mealLunch = MutableStateFlow("")
    private val _mealDinner = MutableStateFlow("")
    private val _selectedFaqIndex = MutableStateFlow(0)
    private val _selectedFaqDetail = MutableStateFlow<PcosFaqItem?>(null)
    private val _activeSheet = MutableStateFlow<ActiveSheet?>(null)

    // Live Notification Preview states
    private val _notificationFilter = MutableStateFlow("Meditation")
    private val _meditationRemainingSeconds = MutableStateFlow(300)
    private val _isMeditationRunning = MutableStateFlow(false)
    private var timerJob: Job? = null

    val faqs = listOf(
        PcosFaqItem(
            id = "faq_1",
            category = "PCOS FAQ",
            question = "What exactly is PCOS?",
            answer = "PCOS stands for polycystic ovary syndrome, a complex hormonal condition that impacts how ovaries function, insulin resistance, and menstrual regularity."
        ),
        PcosFaqItem(
            id = "faq_2",
            category = "Managing PCOS",
            question = "Why does blood sugar matter?",
            answer = "High insulin levels can stimulate ovaries to produce more androgens. Eating balanced meals with protein and fiber helps stabilize glucose and hormone levels."
        ),
        PcosFaqItem(
            id = "faq_3",
            category = "Daily Habits",
            question = "How does movement help?",
            answer = "Regular moderate movement increases insulin sensitivity by muscles, lowers chronic inflammation, and supports cortisol regulation."
        ),
        PcosFaqItem(
            id = "faq_4",
            category = "Hormones & Sleep",
            question = "Why is consistent sleep crucial?",
            answer = "Sleep deprivation spikes cortisol and disrupts melatonin and leptin, making PCOS cravings and insulin resistance more difficult to manage."
        )
    )

    private fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return sdf.format(Date())
    }

    val uiState: StateFlow<HomeDashboardUiState> = combine(
        combine(
            preferences.userNameFlow,
            preferences.pcosStageFlow,
            preferences.focusAreaFlow,
            preferences.themeModeFlow,
            preferences.cycleLoggedFlow
        ) { name, stage, focus, theme, cycleLogged ->
            ProfileThemeState(name, stage, focus, theme, cycleLogged)
        },
        combine(
            _mealsLogged,
            _lifestyleLogged,
            preferences.cycleDayFlow,
            _activeTab
        ) { mealsLogged, lifestyleLogged, cycleDay, activeTab ->
            ActionState(mealsLogged, lifestyleLogged, cycleDay, activeTab)
        },
        combine(
            preferences.waterCountFlow,
            preferences.mealsCountFlow,
            preferences.movementMinutesFlow,
            preferences.relaxationMinutesFlow,
            preferences.sleepHoursFlow
        ) { water, meals, movement, relax, sleep ->
            GoalsState(water, meals, movement, relax, sleep)
        },
        combine(
            preferences.sleepTipDismissedFlow,
            _selectedFaqIndex,
            _selectedFaqDetail,
            _activeSheet
        ) { tipDismissed, faqIdx, faqDetail, activeSheet ->
            MetaState(tipDismissed, faqIdx, faqDetail, activeSheet)
        },
        combine(
            preferences.symptomStatusFlow,
            _symptomBloating,
            _symptomSkin,
            _symptomMood,
            _symptomSleep
        ) { status, bloating, skin, mood, sleep ->
            SymptomState(status, bloating, skin, mood, sleep)
        }
    ) { profileTheme, action, goals, meta, symptom ->
        val computedCycleStatus = if (profileTheme.cycleLogged) {
            "Day ${action.cycleDay} · Period logged"
        } else {
            "No cycle logged yet · Log your last period date"
        }

        HomeDashboardUiState(
            userName = profileTheme.name,
            pcosStage = profileTheme.stage,
            focusArea = profileTheme.focus,
            themeMode = profileTheme.theme,
            cycleLogged = profileTheme.cycleLogged,
            mealsLogged = action.mealsLogged,
            lifestyleLogged = action.lifestyleLogged,
            cycleStatus = computedCycleStatus,
            selectedCycleDay = action.cycleDay,
            activeTab = action.activeTab,
            waterCount = goals.water,
            mealsCount = goals.meals,
            movementMinutes = goals.movement,
            relaxationMinutes = goals.relax,
            sleepHours = goals.sleep,
            sleepTipDismissed = meta.tipDismissed,
            selectedFaqIndex = meta.faqIdx,
            selectedFaqDetail = meta.faqDetail,
            activeSheet = meta.activeSheet,
            symptomStatus = symptom.status,
            symptomBloating = symptom.bloating,
            symptomSkin = symptom.skin,
            symptomMood = symptom.mood,
            symptomSleep = symptom.sleep,
            mealBreakfast = _mealBreakfast.value,
            mealLunch = _mealLunch.value,
            mealDinner = _mealDinner.value,
            currentDateFormatted = getFormattedDate(),
            notificationFilter = _notificationFilter.value,
            meditationRemainingSeconds = _meditationRemainingSeconds.value,
            isMeditationRunning = _isMeditationRunning.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeDashboardUiState(currentDateFormatted = getFormattedDate())
    )

    fun selectTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferences.setThemeMode(mode)
        }
    }

    fun openSheet(sheet: ActiveSheet) {
        _activeSheet.value = sheet
    }

    fun closeSheet() {
        _activeSheet.value = null
    }

    fun setMealsCount(count: Int) {
        viewModelScope.launch {
            preferences.updateMealsCount(count.coerceIn(0, 3))
        }
    }

    fun setMovementMinutes(minutes: Int) {
        viewModelScope.launch {
            preferences.updateMovementMinutes(minutes.coerceAtLeast(0))
        }
    }

    fun setWaterCount(count: Int) {
        viewModelScope.launch {
            preferences.updateWaterCount(count.coerceAtLeast(0))
        }
    }

    fun setRelaxationMinutes(minutes: Int) {
        viewModelScope.launch {
            preferences.updateRelaxationMinutes(minutes.coerceAtLeast(0))
        }
    }

    fun setSleepHours(hours: Int) {
        viewModelScope.launch {
            preferences.updateSleepHours(hours.coerceAtLeast(0))
        }
    }

    fun selectCycleDay(day: Int) {
        _selectedCycleDay.value = day
    }

    fun saveCyclePeriod() {
        viewModelScope.launch {
            preferences.saveCycleLog(day = _selectedCycleDay.value, logged = true)
        }
        closeSheet()
    }

    fun selectSymptomBloating(value: String) { _symptomBloating.value = value }
    fun selectSymptomSkin(value: String) { _symptomSkin.value = value }
    fun selectSymptomMood(value: String) { _symptomMood.value = value }
    fun selectSymptomSleep(value: String) { _symptomSleep.value = value }

    fun saveSymptoms() {
        val statusText = "${_symptomBloating.value} bloating · ${_symptomMood.value} mood"
        viewModelScope.launch {
            preferences.saveSymptomStatus(statusText)
        }
        closeSheet()
    }

    fun selectMealBreakfast(value: String) {
        _mealBreakfast.value = value
        updateMealsCountFromSelections()
    }

    fun selectMealLunch(value: String) {
        _mealLunch.value = value
        updateMealsCountFromSelections()
    }

    fun selectMealDinner(value: String) {
        _mealDinner.value = value
        updateMealsCountFromSelections()
    }

    private fun updateMealsCountFromSelections() {
        var count = 0
        if (_mealBreakfast.value.isNotEmpty() && _mealBreakfast.value != "Skipped") count++
        if (_mealLunch.value.isNotEmpty() && _mealLunch.value != "Skipped") count++
        if (_mealDinner.value.isNotEmpty() && _mealDinner.value != "Skipped") count++
        setMealsCount(count)
        _mealsLogged.value = count > 0
    }

    fun saveMeals() {
        _mealsLogged.value = true
        closeSheet()
    }

    fun saveProfile(name: String, stage: String, focus: String = uiState.value.focusArea) {
        viewModelScope.launch {
            preferences.saveUserProfile(name, stage, focus)
            closeSheet()
        }
    }

    fun dismissSleepTip() {
        viewModelScope.launch {
            preferences.setSleepTipDismissed(true)
        }
    }

    fun markSleepDoneFromTip() {
        viewModelScope.launch {
            preferences.updateSleepHours(8)
            preferences.setSleepTipDismissed(true)
            _lifestyleLogged.value = true
        }
    }

    fun nextFaq() {
        val next = (_selectedFaqIndex.value + 1) % faqs.size
        _selectedFaqIndex.value = next
    }

    fun showFaqDetail(faq: PcosFaqItem) {
        _selectedFaqDetail.value = faq
    }

    fun dismissFaqDetail() {
        _selectedFaqDetail.value = null
    }

    // Live Notification Preview Controls
    fun selectNotificationFilter(filter: String) {
        _notificationFilter.value = filter
    }

    fun startMeditation() {
        if (_isMeditationRunning.value) {
            timerJob?.cancel()
            _isMeditationRunning.value = false
            return
        }

        _isMeditationRunning.value = true
        timerJob = viewModelScope.launch {
            while (_meditationRemainingSeconds.value > 0 && _isMeditationRunning.value) {
                delay(1000)
                _meditationRemainingSeconds.value = _meditationRemainingSeconds.value - 1
            }
            _isMeditationRunning.value = false
        }
    }

    fun resetMeditation() {
        timerJob?.cancel()
        _isMeditationRunning.value = false
        _meditationRemainingSeconds.value = 300
    }

    fun resetDemoOnboarding(onResetComplete: () -> Unit) {
        viewModelScope.launch {
            preferences.resetOnboardingForTesting()
            _selectedCycleDay.value = 30
            _mealsLogged.value = false
            _lifestyleLogged.value = false
            _mealBreakfast.value = ""
            _mealLunch.value = ""
            _mealDinner.value = ""
            onResetComplete()
        }
    }
}

private data class ProfileThemeState(
    val name: String,
    val stage: String,
    val focus: String,
    val theme: String,
    val cycleLogged: Boolean
)

private data class ActionState(
    val mealsLogged: Boolean,
    val lifestyleLogged: Boolean,
    val cycleDay: Int,
    val activeTab: Int
)

private data class GoalsState(
    val water: Int,
    val meals: Int,
    val movement: Int,
    val relax: Int,
    val sleep: Int
)

private data class MetaState(
    val tipDismissed: Boolean,
    val faqIdx: Int,
    val faqDetail: PcosFaqItem?,
    val activeSheet: ActiveSheet?
)

private data class SymptomState(
    val status: String,
    val bloating: String,
    val skin: String,
    val mood: String,
    val sleep: String
)
