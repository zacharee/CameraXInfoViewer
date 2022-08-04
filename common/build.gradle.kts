import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
    kotlin("plugin.serialization") version "1.7.0"
}

group = rootProject.extra["groupName"].toString()
version = rootProject.extra["versionName"].toString()

@OptIn(ExperimentalComposeLibrary::class)
kotlin {
    android()
    js(IR) {
        useCommonJs()
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.ui)
                api(compose.material3)
                api("dev.icerock.moko:resources:0.20.1")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0-RC")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")
                api("com.soywiz.korlibs.korio:korio:2.7.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.4.2")
                api("androidx.core:core-ktx:1.8.0")
                api("com.caverock:androidsvg-aar:1.4")
                api("androidx.compose.foundation:foundation-layout:1.3.0-alpha02")

                api("com.google.firebase:firebase-firestore-ktx:24.2.1")
                api("com.google.firebase:firebase-auth-ktx:21.0.6")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jsMain by getting {
            dependencies {
                api(compose.web.core)
                api(npm("firebase", "9.9.1"))
//                api(npm("@firebase/app", "0.7.29"))
//                api(npm("@firebase/firestore", "3.4.13"))
//                api(npm("@firebase/auth", "0.20.5"))
//                api(npm("@firebase/app-types", "0.7.0"))
//                api(npm("@firebase/firestore-types", "2.5.0"))
//                api(npm("@firebase/auth-types", "0.11.0"))
            }
        }
    }
}

android {
    compileSdk = (31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = (24)
        targetSdk = (31)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "dev.zwander.cameraxinfoviewer.resources.common" // required
}
