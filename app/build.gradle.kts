plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.solancegmd.geode"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.solancegmd.geode"
        minSdk = 23
        targetSdk = 28
        versionCode = 1
        versionName = "1.0"
    }
}
