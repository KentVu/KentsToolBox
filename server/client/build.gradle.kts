import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
  androidLibrary {
    namespace = "com.kentvu.toolbox.shared.model"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }

    androidResources {
      enable = true
    }
  }

  val enableIos: Boolean by rootProject.extra
  if (enableIos) {
    iosArm64()
    iosSimulatorArm64()
  }

  jvm()

  js {
    browser()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  sourceSets {
    // Workaround Java-only module server can't see ktor-http deps in AndroidStudio
    // Set to true to enable
    val workaroundServerModule = false
    commonMain.dependencies {
      api(projects.shared)
      api(projects.shared.model)
      implementation(libs.kotlinx.coroutines)
      if (!workaroundServerModule) {
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.contentNegotiation)
        implementation(libs.ktor.serialization.json)
      }
    }
    jvmMain.dependencies {
      if (workaroundServerModule) {
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.contentNegotiation)
        implementation(libs.ktor.serialization.json)
      }
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutinesTest)
    }
  }
}

