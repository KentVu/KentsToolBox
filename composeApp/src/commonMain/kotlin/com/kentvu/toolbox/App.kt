package com.kentvu.toolbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.delete
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.kentvu.toolbox.models.Item
import org.jetbrains.compose.resources.painterResource

import kentstoolbox.composeapp.generated.resources.Res
import kentstoolbox.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch

/*class PreviewBackend : Backend {
    override val model: StateFlow<Model> = MutableStateFlow(Model(listOf(Item("Buy peacock feathers"))))

    override fun post(
        action: Backend.Action,
        item: Item
    ) {
    }

}*/

@Composable
@Preview
fun AppPreview() {
    App(Model.Preview())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(model: Model) {
    MaterialTheme {
        val state by model.state.collectAsState()
        Scaffold(
            Modifier.safeContentPadding(),
            topBar = {
                TopAppBar(title = { Row {
                    Text("To-Do", Modifier.testTag("header"))
                    Text(" - ")
                    Text(state.path.removePrefix("/lists/").removeSuffix("/"), Modifier.testTag("id_list_id"))
                } })
            },
        ) { paddingValues ->
            LaunchedEffect(Unit) {
                model.get("/")
            }
            when (state.path) {
                "/" -> Home(Modifier.padding(paddingValues), state.data) {
                    model.post(state.path, Item(it))
                }

                // 7.5. Getting Back to a Working State as Quickly as Possible
                "/lists/the-only-list-in-the-world/" -> Home(Modifier.padding(paddingValues), state.data) {
                    model.post(state.path, Item(it))
                }

                else -> error("Unknown path: ${state.path}")
            }

        }
    }
}

@Composable
private fun Home(
    modifier: Modifier = Modifier,
    data: List<Item>,
    onSubmit: suspend (text: String) -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .semantics { contentDescription = "Home screen" }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val scope = rememberCoroutineScope()
        val textFieldState = rememberTextFieldState()
        TextField(
            textFieldState,
            Modifier.testTag("id_new_item"),
            placeholder = { Text("Enter a to-do item", Modifier.testTag("Placeholder")) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            onKeyboardAction = { performDefault ->
                scope.launch {
                    onSubmit("${textFieldState.text}")
                    performDefault()
                    textFieldState.edit { delete(0, length) }
                }
            },
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Column(Modifier.testTag("id_list_table")) {
            data.forEachIndexed { index, item ->
                Text("${index + 1}: ${item.text}")
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