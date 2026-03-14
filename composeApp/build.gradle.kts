import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidLibrary {
        namespace = "com.kentvu.toolbox.library"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        // Opt-in to enable and configure device-side (instrumented) tests
        withDeviceTest {
        }

        androidResources {
            enable = true
        }
    }

    /*takeIf { enableIos }?.*/listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    // Default Kotlin Hierarchy Template Not Applied Correctly
    //The Default Kotlin Hierarchy Template was not applied to 'project ':composeApp'':
    //Explicit .dependsOn() edges were configured for the following source sets:
    //[androidDeviceTest]
    // Apply the default hierarchy again. It'll create, for example, the iosMain source set:
    applyDefaultHierarchyTemplate()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        val commonTest2Android by creating {
            dependsOn(commonTest.get())
        }
        //androidInstrumentedTest.dependencies {}
        getByName("androidDeviceTest") {
            // Make commonTest run on Android also.
            dependsOn(commonTest2Android)
            dependencies {
                implementation(libs.androidx.ui.test.manifest)
                implementation(libs.androidx.ui.test.junit4)
            }
        }
        commonMain.dependencies {
            implementation(projects.uiModel)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.nav3.ui)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ui.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            //implementation(libs.androidx.navigation3.ui)
        }
        jvmTest.dependencies {
            implementation(libs.ui.test.junit4)
            implementation(project.dependencies.platform(libs.junit.bom))
            implementation(project.dependencies.platform(libs.cucumber.bom))
            implementation(libs.cucumber.java)
            implementation(libs.cucumber.junit)
            implementation(libs.cucumber.junit.platform.engine)
            implementation(libs.junit.platform.suite)
            runtimeOnly("org.junit.platform:junit-platform-launcher")
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.kentvu.toolbox.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kentvu.toolbox"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform {
        // When running an individual scenario, assume we only want to run
        // Cucumber
        System.getProperty("cucumber.features")?.let { includeEngines("cucumber") }
    }

    // Pass selected system properties to Cucumber
    System.getProperty("cucumber.features")?.let { systemProperty("cucumber.features", it) }
    System.getProperty("cucumber.filter.tags")?.let { systemProperty("cucumber.filter.tags", it) }
    System.getProperty("cucumber.filter.name")?.let { systemProperty("cucumber.filter.name", it) }
    System.getProperty("cucumber.plugin")?.let { systemProperty("cucumber.plugin", it) }

    // Work around. Gradle does not include enough information to disambiguate
    // between different examples and scenarios.
    systemProperty("cucumber.junit-platform.naming-strategy", "long")

}

tasks.register<Test>("e2eTest") {
    dependsOn
}