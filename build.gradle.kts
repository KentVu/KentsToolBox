plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
    val hilt_version = "2.59.2"
    //id("dagger.hilt.android.plugin") apply false
    alias(libs.plugins.hilt.android) apply false
    val kotlin_version = libs.versions.kotlin.get()
    val ksp_version = "$kotlin_version-1.0.23"
    //id("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$ksp_version") apply false
    //id("com.google.devtools.ksp") version "2.3.6" apply false
    alias(libs.plugins.ksp) apply false
}