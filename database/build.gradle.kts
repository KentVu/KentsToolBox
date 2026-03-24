import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidMultiplatformLibrary)
  alias(libs.plugins.ksp)
  alias(libs.plugins.androidx.room)
}

kotlin {
  androidLibrary {
    namespace = "com.kentvu.toolbox.data"
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
      implementation(projects.shared)
      implementation(libs.kotlinx.coroutines)
      implementation(libs.androidx.room.common)
    }
    // wait for Room to support web targets https://issuetracker.google.com/issues/336758416
    val nonWebMain by creating {
      dependsOn(commonMain.get())
      dependencies {
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.sqlite.bundled)
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

dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspJvm", libs.androidx.room.compiler)
  //add("kspIosSimulatorArm64", libs.androidx.room.compiler)
  //add("kspIosX64", libs.androidx.room.compiler)
  //add("kspIosArm64", libs.androidx.room.compiler)
  // Add any other platform target you use in your project, for example kspDesktop
}

room {
  schemaDirectory("$projectDir/schemas")
}
