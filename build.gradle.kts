buildscript {

    val hiltVersion by extra("2.42")

    dependencies {
        classpath ("com.google.gms:google-services:4.3.13")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
}

tasks.register<Delete>("clean", Delete::class) {
    delete(rootProject.buildDir)
}