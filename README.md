<p align="center">
  <img src="art/bloom_app_icon.png" width="128" height="128" alt="Bloom App Icon" />
</p>

<h1 align="center">Bloom — PCOS Daily Companion</h1>

<p align="center">
  <b>A modern, high-fidelity Android wellness companion developed with Kotlin & Jetpack Compose</b><br>
  <i>Accurately crafted to match Figma Frame 26080030 &amp; Node 26-7089</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.2.10-purple.svg" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-green.svg" alt="Compose" />
  <img src="https://img.shields.io/badge/Architecture-MVVM_%2B_UDF-blue.svg" alt="MVVM" />
  <img src="https://img.shields.io/badge/DataStore-1.1.1-orange.svg" alt="DataStore" />
  <img src="https://img.shields.io/badge/Google_Mobile_Ads-23.6.0-red.svg" alt="AdMob" />
  <img src="https://img.shields.io/badge/Min_SDK-24-informational.svg" alt="Min SDK 24" />
</p>

---

## 🌸 1. Complete Figma Design Clone Overview
**Bloom** is a daily wellness companion tailored for individuals navigating Polycystic Ovary Syndrome (PCOS). The UI recreates the exact Figma screens (including node `26-7089` and the complete design system) with 100% precision, complete **Light Mode** and **Dark Mode**, and reactive theme switching.

### Primary Screens & Components Completed:
1. **Figma 4-Step Onboarding Flow**:
   - **Step 1 (Welcome to Bloom)**: Signature 4-petal Bloom flower hero graphic, subtitle, and *"Get started"* CTA.
   - **Step 2 (A little about you)**: Name text field (defaulting to *"Sofia"*), 4 PCOS journey stage radio cards (*Diagnosed*, *Suspected*, *Exploring*, *Not sure yet*).
   - **Step 3 (What would you like to track daily?)**: 7 toggle rows with switches:
     - *Meals · 3 balanced meals*
     - *Movement · 30 minutes*
     - *Water · 8 glasses*
     - *Relaxation · 15 minutes*
     - *Sleep · 8 hours*
     - *Cycle · period dates*
     - *Symptom check-in · bloating, skin, mood, sleep quality*
   - **Step 4 (When should we remind you?)**:
     - *Pick a time of day*: Morning (7:30 AM), Afternoon (1:00 PM), Evening (8:00 PM)
     - *Or set a custom time*: Time input (08:00 AM)
     - *"Enter Bloom"* CTA and *"I'll set this up later"* skip action.
2. **Figma Home Dashboard (Node 26-7089)**:
   - Personalized header (*"GOOD MORNING, Sofia"* in italic display serif + date + profile avatar button).
   - **Today's Goals Card**:
     - Title (*"Today's Goals"*), subtitle (*"Let's get blooming"* / *"You're in full bloom!"*), progress count (*"0 of 5 complete"*), and linear progress indicator.
     - **Signature Interactive Bloom Flower Graphic**: 4-petal canvas graphic where petals animate and fill in based on daily habit completion, with the *"Start here"* italic callout displayed when progress is 0.
   - **PCOS FAQ Carousel Card**: Rotating educational questions with category in Honey Gold, question in bold, answer preview, carousel indicator dots, and interactive full-detail dialog.
   - **Quick Action Cards**:
     - *Cycle*: Status badge (*"No cycle logged yet · Log your last period date"*), opens Month Calendar logger.
     - *Symptom check-in*: Status badge (*"Bloating, skin, mood, sleep quality"*), opens Symptoms logger.
   - **Goal Progress Section**: 5 interactive rows (*Meals*, *Movement*, *Water*, *Relaxation*, *Sleep*) with status progress and interactive modal bottom sheets.
   - **Today's Tip Card**: *"LOG LAST NIGHT'S SLEEP"* with *"Remind me later"* and *"Mark as done"* actions.
   - **Live Notification Preview Card**: Interactive lockscreen preview (*"Bloom · now · Meditation: 5:00 remaining"*) with *"Begin"* / *"Pause"* and *"Reset"* timer controls.
3. **Figma Insights Tab**:
   - 7-day completion bar chart (*M T W T F S S*), Week/Month toggle pill, daily summary, and symptom patterns micro-charts (Bloating, Skin, Mood, Sleep).
4. **Figma Learn Tab**:
   - 4 categories (*Diagnosis & basics*, *Symptoms & body*, *Managing it day to day*, *Looking ahead*) with 16 educational topics and medical overview popups.
5. **Figma Settings Tab**:
   - Profile card, Appearance & Theme selector (System Default, Light, Dark), *"What you're tracking"* switches, daily targets, and Demo Reset.
6. **Figma Modal Bottom Sheets**:
   - **Log Cycle Sheet**: Interactive July 2026 month calendar with start date picker, next period prediction, and legend.
   - **Today's Symptoms Sheet**: 4 categories with 4-tier segmented pill buttons.
   - **Log Meals Sheet**: 3 meal rows (*Breakfast*, *Lunch*, *Dinner*) with 4 selection options.
   - **Habit Steppers**: Steppers for Movement (goal 30 min), Water (goal 8 glasses), Relaxation (goal 15 min), and Sleep (goal 8 hrs) with PCOS motivational advice.
   - **Edit Profile Sheet**: Name and journey stage editor with live DataStore sync.
7. **Complete Light & Dark Mode System**:
   - Centralized design tokens ([Color.kt](file:///d:/Vishal/AndroidStudioProject/New%20Project/Bloom/app/src/main/java/com/newage/bloom/ui/theme/Color.kt), [Theme.kt](file:///d:/Vishal/AndroidStudioProject/New%20Project/Bloom/app/src/main/java/com/newage/bloom/ui/theme/Theme.kt)) adapting across all screens.
8. **Attractive Floating Pill Bottom Navigation Bar**:
   - Elevated pill dock with ambient Rose drop shadow, spring-animated icon scale (`1.12x`), outlined-to-filled icon transitions, active capsule highlight, and expanding indicator pill.
9. **Google Mobile Ads Integration**:
   - App Open Ad with first-launch gatekeeper and Adaptive Banner Ad with fallback handling and ad analytics telemetry.

---

## 🛠️ 2. Technology Stack & Key Libraries
- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose with Material 3 (BOM `2024.10.01`)
- **Architecture**: Model-View-ViewModel (MVVM) + Unidirectional Data Flow (UDF)
- **Local Persistence**: AndroidX Preferences DataStore (`1.1.1`)
- **Monetization**: Google Mobile Ads SDK (`play-services-ads:23.6.0`)
- **Lifecycle & Process**: AndroidX Lifecycle Runtime & Process (`2.8.7`)
- **Build System**: Gradle 9.4.1 / Android Gradle Plugin 9.2.1
- **Minimum SDK**: Android 7.0 (API Level 24)
- **Target / Compile SDK**: Android 16 (API Level 36)

---

## 🎨 3. Color Tokens & Theme Architecture

| Token Name | Light Mode Hex | Dark Mode Hex | Usage |
| :--- | :--- | :--- | :--- |
| `background` | `#FAF6F2` | `#191316` | Main screen background |
| `surface` | `#FFFFFF` | `#241C20` | Elevated cards and bottom sheets |
| `surfaceVariant` | `#F3EDE8` | `#2E2429` | Chip containers and text inputs |
| `border` | `#E8DCD5` | `#3A2E34` | Subtle card borders |
| `primary` | `#FF528C` | `#FF85AB` | Bloom Rose brand CTA, active tabs |
| `secondary` | `#5F8A72` | `#88B39B` | Sage Green success and habit badges |
| `tertiary` | `#E09A2F` | `#F5B24E` | Honey Gold FAQ category and core accents |
| `textPrimary` | `#2D2328` | `#F5EBEF` | Headings and primary copy |
| `textSecondary`| `#786C72` | `#B8A9B0` | Subtitles, labels, and helper text |

---

## 💾 4. DataStore Persistence Keys & State Handling
All user selections, profile attributes, and interactive habit logs are persisted asynchronously in Preferences DataStore (`bloom_user_preferences`) to prevent unexpected state reset:
- `KEY_IS_ONBOARDING_COMPLETED` (Boolean): Determines routing between Onboarding and Home Dashboard.
- `KEY_USER_NAME` (String): Captured during onboarding (default: *"Sofia"*).
- `KEY_PCOS_STAGE` (String): Journey stage (*Diagnosed*, *Suspected*, etc.).
- `KEY_FOCUS_AREA` (String): Selected focus area (*Track my cycle*, *Eat better*, etc.).
- `KEY_THEME_MODE` (String): Theme preference (*"SYSTEM"*, *"LIGHT"*, *"DARK"*).
- `KEY_REMINDER_TIME` (String): Reminder preference (*"Morning (7:30 AM)"*).
- `KEY_SELECTED_GOALS` (Set<String>): Set of enabled tracking goal keys.
- `KEY_CYCLE_LOGGED` (Boolean): Quick action status for cycle logging.
- `KEY_CYCLE_DAY` (Int): Selected cycle day (default: 30).
- `KEY_SYMPTOM_STATUS` (String): Persisted symptom summary string.
- `KEY_WATER_COUNT`, `KEY_MEALS_COUNT`, `KEY_MOVEMENT_MINUTES`, `KEY_RELAXATION_MINUTES`, `KEY_SLEEP_HOURS`: Daily interactive habit counters.
- `KEY_SLEEP_TIP_DISMISSED` (Boolean): Status of sleep tip dismissal.

---

## 📢 5. Google Mobile Ads Integration & Telemetry

### Official Google Test Ad Unit IDs:
- **Sample AdMob App ID**: `ca-app-pub-3940256099942544~3347511713` (configured in `AndroidManifest.xml`)
- **App Open Ad**: `ca-app-pub-3940256099942544/9257395921`
- **Adaptive Banner Ad**: `ca-app-pub-3940256099942544/9214589741`

### App Open Ad Architecture:
1. **First-Time User Protection**:
   - `AppOpenAdManager` checks `isOnboardingCompleted()` before showing ads. First-time users are **never interrupted** during their onboarding experience.
2. **Activity Lifecycle Safeguards**:
   - Checks `activity.isFinishing` and `activity.isDestroyed` before presenting full-screen content to prevent window token exceptions.
3. **Foreground Transitions**:
   - Implements `ProcessLifecycleOwner` to listen for foreground transitions and display pre-cached ads on returning app opens.
4. **4-Hour Expiration & Automatic Reload**:
   - Ad timestamp is compared against a 4-hour threshold (`FOUR_HOURS_MILLIS`). Expired ads are discarded and re-requested. Upon dismissal or failure, `AppOpenAdManager` automatically triggers `loadAd()` to prime the next ad.

### Adaptive Banner Ad Architecture:
1. **Dynamic Orientation & Sizing**:
   - Computed via `AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)`.
2. **Placement & Separation**:
   - Anchored above the bottom navigation bar with rounded container styling that seamlessly blends with the active Light or Dark theme.
3. **Failure Recovery**:
   - If an ad fails to load (such as in offline testing or emulator with network disabled), an unobtrusive test placeholder maintains stable UI spacing without layout jumps.

### Ad Analytics & Telemetry:
Both ad formats emit structured logcat events with `[Ad Analytics]` tags to monitor ad health and engagement:
- `[Ad Analytics] App Open Ad impression recorded.`
- `[Ad Analytics] App Open Ad user click recorded.`
- `[Ad Analytics] Banner ad impression recorded.`
- `[Ad Analytics] Banner ad user click recorded.`
- `[Ad Analytics] Banner ad opened full screen overlay.`
- Load errors with error code and error message for real-time diagnostics.

---

## 🎯 6. Interview Architecture Discussion & Key Decisions

During technical discussions and interviews, here is how each architectural choice is explained:

### 1. Why Jetpack Compose + MVVM?
- **Declarative UI**: Replaces imperative View hierarchies with reactive state rendering. When DataStore emits a new state, Compose automatically updates only the affected nodes.
- **Unidirectional Data Flow (UDF)**: Events flow upwards (User clicks -> ViewModel functions), and state flows downwards (StateFlow -> Compose UI). This eliminates race conditions and ensures a predictable UI.

### 2. Why Preferences DataStore over SharedPreferences or Room?
- **Thread Safety & Async**: Preferences DataStore runs on Kotlin Coroutines and Dispatchers.IO, preventing ANRs caused by synchronous disk writes in SharedPreferences.
- **Lightweight Key-Value Store**: For onboarding flags, habit counters, and user preferences, DataStore offers zero-boilerplate persistence without the schema overhead of an SQLite Room database.

### 3. How are First-Launch Ad Interruptions Prevented?
- Google AdMob policy strictly discourages interrupting a user's initial app setup. Bloom provides an `isOnboardingCompleted` gatekeeper lambda injected into `AppOpenAdManager`. If `isOnboardingCompleted == false`, ad display is immediately suppressed and navigation continues without blocking.

### 4. How does the Floating Dock Bottom Navigation Bar Work?
- Built as an elevated floating pill container with a custom shape, subtle border, and ambient shadow. Active items expand an animated capsule background and apply a `1.12x` spring scale to the icon, providing visual polish without external third-party navigation libraries.

---

## 🚀 7. Build, Test, & Run Commands

### 1. Build the Debug APK
```powershell
.\gradlew.bat assembleDebug
```
*Build Status*: `BUILD SUCCESSFUL`

### 2. Run Local Unit Tests
```powershell
.\gradlew.bat testDebugUnitTest
```
*Test Status*: `BUILD SUCCESSFUL` (All 4 unit tests passing)

### 3. Install to Connected Device / Emulator
```powershell
& "D:\Vishal\AndroidSDK\platform-tools\adb.exe" install -r "app\build\outputs\apk\debug\app-debug.apk"
```

### 4. Launch Application
```powershell
& "D:\Vishal\AndroidSDK\platform-tools\adb.exe" shell am start -n "com.newage.bloom/com.newage.bloom.MainActivity"
```

### 5. Debug APK Details
- **Path**: `D:\Vishal\AndroidStudioProject\New Project\Bloom\app\build\outputs\apk\debug\app-debug.apk`
- **Package**: `com.newage.bloom`
- **Size**: ~22.0 MB

---

## 📋 8. Verification & Testing Status

| Verification Area | Status | Method / Notes |
| :--- | :--- | :--- |
| **Gradle Compilation** | **VERIFIED** | Clean compile on Kotlin 2.2.10 + AGP 9.2.1 |
| **Unit Test Suite** | **VERIFIED** | `ExampleUnitTest` (4 tests: onboarding defaults, partial progress, full progress, timer formatting) passed |
| **Debug APK Generation** | **VERIFIED** | `app-debug.apk` generated (22,001,016 bytes, timestamp matching latest changes) |
| **DataStore Persistence** | **VERIFIED** | Single source of truth via `OnboardingPreferences`, verified in code & tests |
| **AdMob Configuration** | **VERIFIED** | Official Google Test IDs configured in `AndroidManifest.xml` and ad managers |
| **Security & Secrets** | **VERIFIED** | Zero API keys, passwords, or tokens found; `.gitignore` configured |
| **Physical Device Testing** | **PENDING** | To be executed on connected device prior to live interview demo |
| **Live Ad Serving** | **PENDING** | Real-world ad fill depends on network connectivity and Google AdMob test server availability |

---

## 🔒 9. Pre-GitHub Steps for the Developer

Before uploading this project to GitHub, follow these simple steps:

1. **Verify `.gitignore`**:
   The root `.gitignore` has been updated to ignore `local.properties`, `.idea/`, `.kotlin/`, `build/`, and `*.apk`.
2. **Initialize Git (Run in PowerShell)**:
   ```powershell
   git init
   git add .
   git commit -m "Initial commit: Bloom PCOS daily companion Android app"
   ```
3. **Push to your GitHub repository**:
   ```powershell
   git branch -M main
   git remote add origin https://github.com/<your-username>/bloom-android.git
   git push -u origin main
   ```
