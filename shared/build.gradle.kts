plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.kotlin.serialization)
    id("com.android.library")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = "11" }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "ThaparBitesShop Shared Module"
        homepage = "https://github.com/yourrepo"
        version = "1.0"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "shared"
            isStatic = true
        }
        // Firebase iOS pods
        pod("FirebaseAuth")      { version = "~> 11.0" }
        pod("FirebaseFirestore") { version = "~> 11.0" }
    }

    sourceSets {
        commonMain.dependencies {
            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
            // ViewModel (KMP)
            implementation(libs.lifecycle.viewmodel)
            // Koin core
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
            // Firebase (Android)
            implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
            implementation("com.google.firebase:firebase-auth")
            implementation("com.google.firebase:firebase-firestore")
        }
        // iosMain has no extra deps — uses CocoaPods above
    }
}

android {
    namespace = "com.ccs.thaparbitesshop.shared"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}