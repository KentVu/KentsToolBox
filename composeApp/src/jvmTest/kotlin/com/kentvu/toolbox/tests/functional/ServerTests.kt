package com.kentvu.toolbox.tests.functional

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.pathString

class ServerTests {
  private lateinit var serverProcess: Process

  //@Before
  fun runServer() {
    serverProcess = runGradleApp()
    serverProcess.waitFor(30, TimeUnit.SECONDS)
  }

  //@After
  fun stopServer(): Unit = runBlocking {
    serverProcess.destroy()
  }

  @Test
  fun startServer() = runTest {
    runServer()
    backgroundScope.launch {
      serverProcess.inputReader().useLines {
        it.forEach { line ->
          println(line)
        }
      }
    }
    val client = HttpClient(CIO)
    val response = client.get("http://127.0.0.1:8080/")
    print(response)
    println(" body: "+response.bodyAsText())
    stopServer()
    client.close()
  }

  fun runGradleAppWaiting(): Process = runGradleWaiting("run")
  fun runGradleApp(): Process = runGradle(":server:run")

  fun runGradleWaiting(vararg args: String): Process {
    val process = runGradle(*args)
    process.waitFor()
    return process
  }

  fun runGradle(vararg args: String): Process {
    //val gradlewPath = System.getProperty("gradlew") ?: error("System property 'gradlew' should point to Gradle Wrapper file")
    val rootPrjDir = Path(System.getProperty("user.dir")).parent
    val gradlewPath = Path(rootPrjDir.pathString, "gradlew" + if (System.getProperty("os.name").contains("Windows")) ".bat" else "").pathString
    val processArgs = listOf(gradlewPath/*, "-Dorg.gradle.logging.level=quiet", "--quiet"*/) + args

    return ProcessBuilder(processArgs)
      .directory(rootPrjDir.toFile())
      //.inheritIO()
      .apply {
        environment()["JAVA_HOME"] = System.getProperty("java.home")
      }
      .start()
  }
  fun InputStream.readString(): String = readAllBytes().toString(Charsets.UTF_8)
}