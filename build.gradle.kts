group = "dev.zwander"
version = "1.0-SNAPSHOT"

val groupName by extra(group)
val versionName by extra(version)

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.20.1")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}
