import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidMultiplatformLibrary)
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

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain.dependencies {
      api(projects.shared)
      //api(projects.shared.model)
      implementation(projects.server.client)
      implementation(libs.kotlinx.coroutines)
    }
    // wait for Room to support web targets https://issuetracker.google.com/issues/336758416
    val nonWebMain by creating {
      dependsOn(commonMain.get())
      dependencies {
        implementation(projects.database)
      }
    }
    androidMain.get().dependsOn(nonWebMain)
    jvmMain {
      dependsOn(nonWebMain)
      dependencies {
      }
    }
    jvmTest.dependencies {
      implementation(libs.mockk)
      implementation(libs.kotest.assertions.core)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutinesTest)
    }
  }
}

