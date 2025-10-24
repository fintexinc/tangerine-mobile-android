plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.jacoco)
    alias(libs.plugins.kover)
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
        debug {
            enableUnitTestCoverage = true
        }
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

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)

    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("app.cash.turbine:turbine:1.2.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

afterEvaluate {
    tasks.register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")

        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }

        val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "**/*\$*.*", // Inner classes
            "**/di/**", // DI modules
            "**/hilt/**",
            "android/**/*.*"
        )

        val javaTree = fileTree("${layout.buildDirectory.get()}/intermediates/javac/debug/classes") {
            exclude(fileFilter)
        }

        val kotlinTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }

        classDirectories.setFrom(files(javaTree, kotlinTree))

        sourceDirectories.setFrom(files(
            "${projectDir}/src/main/java",
            "${projectDir}/src/main/kotlin"
        ))

        executionData.setFrom(fileTree(layout.buildDirectory.get()) {
            include("**/*.exec", "**/*.ec")
        })

        doLast {
            println("✅ Jacoco report generated for :account module:")
            println("   → file://${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/html/index.html")
        }
    }
}

// ./gradlew testDebugUnitTest jacocoTestReport
// ./gradlew jacocoTestReport
// open app/build/reports/jacoco/jacocoTestReport/html/index.html
// open account/build/reports/tests/testDebugUnitTest/index.html - check tests