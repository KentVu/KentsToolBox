package com.kentvu.toolbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import com.kentvu.toolbox.models.Item
import kotlinx.coroutines.launch

//The template for an individual list will reuse quite a lot of the stuff we currently have
// in home.html, so we can start by just copying that:
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List(
  modifier: Modifier = Modifier,
  listId: String,
  data: List<Item>,
  onSubmit: suspend (text: String) -> Unit,
) {
  Scaffold(
    Modifier.safeContentPadding(),
    topBar = {
      TopAppBar(title = { Row {
        Text("To-Do", Modifier.testTag("header"))
        Text(" - ")
        Text(listId, Modifier.testTag("id_list_id"))
      } })
    },
  ) { paddingValues ->
    Column(
      modifier.padding(paddingValues).testTag("list.html")
        .background(MaterialTheme.colorScheme.primaryContainer)
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      NewItemField(onSubmit)
      Column(Modifier.testTag("id_list_table")) {
        data.forEachIndexed { index, item ->
          Text("${index + 1}: ${item.text}")
        }
      }
    }
  }
}