# Women-Fitness-Weight-Lose
 
# WomenHomeWorkout App

## Overview

The WomenHomeWorkout app is designed to offer a variety of fitness routines tailored for women. It includes features for workout tracking, progress monitoring, and more. This README provides a comprehensive overview of the project's dependencies, screenshots, and other relevant information.

## Dependencies

### Core Libraries

- **Kotlin Standard Library**
  - `implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"`
  - Provides essential Kotlin language features and standard library functions.

- **AndroidX Libraries**
  - `androidx.appcompat:appcompat:1.2.0`
    - Adds backward-compatible support for Android components and features.
  - `androidx.core:core-ktx:1.3.2`
    - Provides Kotlin extensions for core Android framework APIs.
  - `androidx.constraintlayout:constraintlayout:2.0.4`
    - Enables complex layouts with a flat view hierarchy.
  - `androidx.legacy:legacy-support-v4:1.0.0`
    - Support for older Android APIs.

- **Material Design Components**
  - `implementation 'com.google.android.material:material:1.2.1'`
    - Implements Material Design components and themes.

- **Flexbox Layout**
  - `implementation 'com.google.android:flexbox:1.0.0'`
    - Allows flexible box layout for arranging UI elements.

### Image and UI Libraries

- **Glide**
  - `implementation 'com.github.bumptech.glide:glide:4.11.0'`
    - Image loading and caching library.

- **Lottie**
  - `implementation "com.airbnb.android:lottie:3.4.0"`
    - Renders animations in JSON format.

- **Horizontal Picker**
  - `implementation 'com.github.jhonnyx2012:horizontal-picker:1.0.6'`
    - Custom view for horizontal pickers (currently commented out in the configuration).

### Firebase Libraries

- **Firebase BOM (Bill of Materials)**
  - `implementation platform('com.google.firebase:firebase-bom:31.2.0')`
    - Ensures compatibility and simplifies version management for Firebase libraries.

- **Firebase SDKs**
  - `implementation 'com.google.firebase:firebase-ads:21.3.0'`
    - For integrating Google AdMob ads.
  - `implementation 'com.google.firebase:firebase-analytics:21.2.0'`
    - Provides analytics and event tracking.
  - `implementation 'com.google.firebase:firebase-messaging:23.1.0'`
    - Manages Firebase Cloud Messaging.
  - `implementation 'com.google.firebase:firebase-perf-ktx:20.3.1'`
    - Performance monitoring.
  - `implementation 'com.google.firebase:firebase-crashlytics-ktx'`
    - Crash reporting and analytics.

### Networking and JSON Handling

- **Retrofit**
  - `implementation 'com.squareup.retrofit2:retrofit:2.6.1'`
  - `implementation 'com.squareup.retrofit2:converter-gson:2.6.1'`
    - Type-safe HTTP client with JSON conversion via Gson.

- **Gson**
  - `implementation 'com.google.code.gson:gson:2.9.0'`
    - Converts Java objects to and from JSON.

### Background Work

- **WorkManager**
  - `implementation 'androidx.work:work-runtime-ktx:2.7.1'`
    - Manages deferrable background tasks.

### In-App Purchase and Ad SDKs

- **AppLovin SDK**
  - `implementation 'com.applovin:applovin-sdk:+'`
    - SDK for in-app advertisements. (Consider specifying a fixed version instead of `+`.)

- **Facebook Audience Network SDK**
  - `implementation 'com.facebook.android:audience-network-sdk:6.+'`
    - SDK for integrating Facebook ads. (Consider specifying a fixed version instead of `6.+`.)

- **Google Play Core**
  - `implementation 'com.google.android.play:core:1.10.3'`
    - Manages in-app updates and reviews.

## Screenshots

Here are some screenshots showcasing the main features of the WomenHomeWorkout app:

### Home Screen

![Home Screen](screenshots/home_screen.png)
*The Home Screen provides an overview of available workout routines and user progress.*

### Workout Routine

![Workout Routine](screenshots/workout_routine.png)
*Detailed view of a selected workout routine.*

### Progress Tracking

![Progress Tracking](screenshots/progress_tracking.png)
*Tracks user progress and workout history.*

### Settings

![Settings](screenshots/settings.png)
*User settings and preferences.*

## Building the Project

1. **Setup**
   - Ensure you have Android Studio and the latest Gradle installed.

2. **Sync Project**
   - Sync the project with Gradle files to resolve dependencies.

3. **Build and Run**
   - Build and run the app on an emulator or a physical device.

## Notes

- For certain libraries like AppLovin SDK and Facebook Audience Network SDK, it's recommended to specify exact versions to ensure stability.
- The `horizontal-picker` dependency is commented out. Uncomment and configure if needed.

## Troubleshooting

- If you encounter issues, ensure all dependencies are correctly added and up-to-date.
- Refer to the official documentation of each library for specific integration guidelines and troubleshooting.

## Contributing

- Contributions are welcome. Please fork the repository and submit a pull request with your changes.

## License

- This project is licensed under the [MIT License](LICENSE).

