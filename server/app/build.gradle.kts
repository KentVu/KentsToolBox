plugins {
    application
}

group = "com.kentvu.toolbox.server"
version = "1.0.0"
application {
    mainClass.set("com.kentvu.toolbox.server.ApplicationKt")

    project.ext["development"] = true
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.server)
}