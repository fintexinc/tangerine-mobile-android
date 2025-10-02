plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.fintexinc.tangerine"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fintexinc.tangerine"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // DI
    implementation(libs.android.hilt)
    ksp(libs.android.hilt.compiler)

    // compose
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.constraint.layout)
    implementation(libs.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.threetenabp)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // features
    implementation(project(":dashboard"))
    implementation(project(":account"))
    implementation(project(":documents"))
    implementation(project(":core"))
    implementation(project(":transaction_details"))
}