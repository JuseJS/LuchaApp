import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.bottom.sheet.navigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
    sourceSets.commonMain.dependencies {
        implementation(kotlin("reflect"))
    }
}

android {
    namespace = "org.iesharia"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.iesharia"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.iesharia.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "LuchaCanaria"
            packageVersion = "1.0.0"

            // Add application metadata
            description = "Aplicación de Lucha Canaria"
            copyright = "© 2025 IES Haria"
            vendor = "IES Haria"

            // Windows configuration
            windows {
                // Usa app_icon.ico en lugar de app_logo.ico después de tu cambio
                iconFile.set(project.file("src/commonMain/composeResources/drawable/app_icon.ico"))
                menuGroup = "IES Haria Apps"
                // Opciones adicionales
                dirChooser = true
                perUserInstall = true
                shortcut = true
                menu = true
            }

            // macOS configuration
            macOS {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/app_icon.icns"))
                bundleID = "org.iesharia.luchacanaria"
            }

            // Linux configuration
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/app_logo.png"))
            }
        }
    }
}