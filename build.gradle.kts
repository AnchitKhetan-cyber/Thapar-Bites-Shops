plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    kotlin("multiplatform") version "2.0.21" apply false
    kotlin("native.cocoapods") version "2.0.21" apply false
    kotlin("plugin.serialization") version "2.0.21" apply false

    // KSP replaces kapt (works with KMP)
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false

    id("com.google.gms.google-services") version "4.4.4" apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.lint) apply false
    // Hilt removed ❌
}