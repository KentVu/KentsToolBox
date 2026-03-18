import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  //alias(libs.plugins.androidMultiplatformLibrary)
  alias(libs.plugins.ktor)
}

kotlin {

  /*androidLibrary {
    namespace = "com.kentvu.toolbox.shared.model"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }

    androidResources {
      enable = true
    }
  }*/

  jvm()

  /*js {
    browser()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }*/

  sourceSets {
    commonMain.dependencies {
      implementation(projects.shared)
      implementation(projects.shared.model)
      implementation(projects.database)
      implementation(libs.ktor.serialization.json)
      implementation(libs.ktor.server.contentNegotiation)
      implementation(libs.logback)
      implementation(libs.ktor.serverCore)
      //implementation("io.ktor:ktor-http-jvm:3.3.3")
      implementation(libs.ktor.serverNetty)
    }
    jvmMain.dependencies {
    }
    jvmTest.dependencies {
      implementation(libs.ktor.serverTestHost)
      implementation(libs.kotlin.testJunit)
    }
  }
}
