package com.kentvu.toolbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
  modifier: Modifier = Modifier,
  onSubmit: suspend (text: String) -> Unit,
) {
  Scaffold(
    Modifier.safeContentPadding(),
    topBar = {
      TopAppBar(title = { Text("To-Do", Modifier.testTag("header")) })
    },
  ) { paddingValues ->
    Column(
      modifier = modifier.padding(paddingValues).testTag("home.html")
        .semantics { contentDescription = "Home screen" }
        .background(MaterialTheme.colorScheme.primaryContainer)
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      NewItemField(onSubmit)
    }
  }
}
