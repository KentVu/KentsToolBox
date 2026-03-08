package com.kentvu.toolbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import org.jetbrains.compose.resources.painterResource

import kentstoolbox.composeapp.generated.resources.Res
import kentstoolbox.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewBackend : Backend {
    override val model: StateFlow<Model> = MutableStateFlow(Model(listOf(Item("Buy peacock feathers"))))

    override fun post(
        action: Backend.Action,
        item: Item
    ): Response {
        return Response(emptyList())
    }

}

@Composable
@Preview
fun App() {
    App(PreviewBackend())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(backend: Backend) {
    MaterialTheme {
        Scaffold(
            Modifier.safeContentPadding(),
            topBar = {
                TopAppBar(title = { Text("To-Do", Modifier.testTag("header")) })
            },
        ) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    rememberTextFieldState(),
                    Modifier.testTag("id_new_item"),
                    placeholder = { Text("Enter a to-do item", Modifier.testTag("Placeholder")) },
                )
                val model by backend.model.collectAsState()
                Column(Modifier.testTag("id_list_table")) {
                    model.items.forEachIndexed { index, item ->
                        Text("${index + 1}: ${item.item_text}")
                    }
                }
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    }
}