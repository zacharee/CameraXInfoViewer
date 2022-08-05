plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    id("dev.icerock.mobile.multiplatform-resources")
}

group = rootProject.extra["groupName"].toString()
version = rootProject.extra["versionName"].toString()

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.5.1")
}

android {
    compileSdk = (32)
    defaultConfig {
        applicationId = "dev.zwander.cameraxinfoviewer"
        minSdk = (24)
        targetSdk = (32)
        versionCode = 1
        versionName = rootProject.extra["versionName"].toString()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "dev.zwander.cameraxinfoviewer.resources.android" // required
}
