import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  //alias(libs.plugins.hilt.android)
  // Kotlin multiplatform Variant API does not support addKspConfigurations() yet. https://github.com/google/ksp/issues/2476
  //alias(libs.plugins.ksp)
}

kotlin {
  target {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }
  dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.uiToolingPreview)
    // Kotlin multiplatform Variant API does not support addKspConfigurations() yet
    //kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android)
    androidTestImplementation(libs.cucumber.android)
    androidTestImplementation(libs.cucumber.android.hilt)
  }
}

android {
  namespace = "com.kentvu.toolbox"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.kentvu.toolbox"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
    // To run Cucumber
    //testInstrumentationRunner = "com.kentvu.toolbox.tests.KentAndroidJUnitRunner"
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
