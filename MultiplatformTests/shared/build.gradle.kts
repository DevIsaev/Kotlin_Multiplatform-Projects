import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)

    alias(libs.plugins.javafxplugin)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    androidLibrary {
       namespace = "com.example.multiplatformtests.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)

            implementation("androidx.compose.animation:animation-graphics:1.7.6") // грузит AVD в Compose
            implementation("androidx.core:core-splashscreen:1.0.1") // системный SplashScreen API (Android 12+)

            implementation("androidx.media3:media3-exoplayer:1.5.0")
            implementation("androidx.media3:media3-ui:1.5.0") // PlayerView
        }

        val jvmMain by getting {
            dependencies {
                val javafxVersion = "21"
                val osName = System.getProperty("os.name").lowercase()
                val fxPlatform = when {
                    osName.contains("win") -> "win"
                    osName.contains("mac") -> "mac"
                    else -> "linux"
                }

                implementation("org.openjfx:javafx-base:$javafxVersion:$fxPlatform")
                implementation("org.openjfx:javafx-graphics:$javafxVersion:$fxPlatform")
                implementation("org.openjfx:javafx-media:$javafxVersion:$fxPlatform")
                implementation("org.openjfx:javafx-swing:$javafxVersion:$fxPlatform")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.napier)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.voyager.navigator) // если нужна навигация между экранами
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

javafx {
    version = "21"
    modules("javafx.media", "javafx.swing")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}