package com.kentvu.toolbox.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.kentvu.toolbox.Model
import com.kentvu.toolbox.models.Item
import kotlinx.coroutines.launch

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
    LaunchedEffect(Unit) {
      model.get(state.path)
    }
    when (state.path) {
      "/" -> Home {
        model.post("/", Item(it))
      }

      // 7.5. Getting Back to a Working State as Quickly as Possible
      "/lists/the-only-list-in-the-world/" -> List(
        Modifier,
        listId = state.path.removePrefix("/lists/").removeSuffix("/"),
        state.data
      ) {
        model.post("/", Item(it))
      }

      else -> error("Unknown path: ${state.path}")
    }

  }
}

@Composable
fun NewItemField(
  onSubmit: suspend (String) -> Unit
) {
  val scope = rememberCoroutineScope()
  val textFieldState: TextFieldState = rememberTextFieldState()
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
}

