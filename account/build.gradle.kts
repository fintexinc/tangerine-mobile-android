plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jacoco)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.tangerine.account"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    sourceSets {
        getByName("main") {
            java.setSrcDirs(listOf("src/main/kotlin", "src/main/java"))
        }
        getByName("test") {
            java.setSrcDirs(listOf("src/test/kotlin", "src/test/java"))
        }
        getByName("androidTest") {
            java.setSrcDirs(listOf("src/androidTest/kotlin", "src/androidTest/java"))
        }
    }
}

dependencies {

    // core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // DI
    implementation(libs.android.hilt)
    implementation(libs.androidx.ui.geometry)
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
    implementation(libs.kotlinx.collections.immutable)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // core
    implementation(project(":core"))

    // charts
    implementation(project(":charts"))

    // Preview
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)

    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("app.cash.turbine:turbine:1.2.1")

}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

afterEvaluate {
    tasks.register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*"
        )

        val debugTree = fileTree("${buildDir}/intermediates/javac/debug") {
            exclude(fileFilter)
        }

        val kotlinTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }

        classDirectories.setFrom(files(debugTree, kotlinTree))
        sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
        executionData.setFrom(files("${buildDir}/jacoco/testDebugUnitTest.exec"))
    }
}
//    ./gradlew clean
//    ./gradlew :account:testDebugUnitTest
//    ./gradlew :account:jacocoTestReport
//      open account/build/reports/jacoco/jacocoTestReport/html/index.html
