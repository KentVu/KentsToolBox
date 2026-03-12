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
    namespace = "com.kentvu.toolbox.uimodel"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }

    androidResources {
      enable = true
    }
  }

  iosArm64()
  iosSimulatorArm64()

  jvm()

  js {
    browser()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain.dependencies {
      api(projects.shared)
      implementation(libs.kotlinx.coroutines)
      //implementation(libs.androidx.navigation3.runtime)
    }
    // wait for Room to support web targets https://issuetracker.google.com/issues/336758416
    val nonWebMain by creating {
      dependsOn(commonMain.get())
      dependencies {
        implementation(projects.database)
      }
    }
    androidMain.get().dependsOn(nonWebMain)
    jvmMain.get().dependsOn(nonWebMain)
    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutinesTest)
    }
  }
}

