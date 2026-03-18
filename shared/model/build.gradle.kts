import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidMultiplatformLibrary)
  // Optional, provides the @Serialize annotation for autogeneration of Serializers.
  alias(libs.plugins.jetbrains.kotlin.serialization)
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
    commonMain.dependencies {
      api(projects.shared)
      implementation(libs.kotlinx.coroutines)
      implementation(libs.kotlinx.serialization.core)
    }
    jvmMain {
      dependencies {
      }
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutinesTest)
    }
  }
}

