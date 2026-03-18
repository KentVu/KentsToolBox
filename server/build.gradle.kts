plugins {
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.ktor)
  application
}

group = "com.kentvu.toolbox.server"
version = "1.0.0"
application {
  mainClass.set("com.kentvu.toolbox.server.ApplicationKt")

  //project.ext["development"] = true
  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment",)
}

dependencies {
  implementation(projects.shared)
  implementation(projects.shared.model)
  implementation(projects.database)
  implementation(libs.ktor.serialization.json)
  implementation(libs.ktor.server.contentNegotiation)
  implementation(libs.logback)
  implementation(libs.ktor.serverCore)
  //implementation("io.ktor:ktor-http-jvm:3.3.3")
  implementation(libs.ktor.serverNetty)
  testImplementation(libs.ktor.serverTestHost)
  testImplementation(libs.kotlin.testJunit)
}
