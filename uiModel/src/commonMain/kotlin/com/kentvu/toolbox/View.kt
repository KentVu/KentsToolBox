package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface View {
  val state: StateFlow<State>

  suspend fun handleNewItemSubmit(text: String)
  suspend fun onInit()

  /** TODO there should be separate Views for Home and List screen. */
  class Default(
    private val httpModel: HttpModel,
    override val state: MutableStateFlow<State> = MutableStateFlow(State())
  ): View {
    // wait for explicit-backing-property to be stable to simplify this.
    //private val _state = MutableStateFlow(State())
    //override val state: StateFlow<State> get() = _state

    override suspend fun onInit() {
      state.value = httpModel.get("/")
    }

    override suspend fun handleNewItemSubmit(text: String) {
      state.value = httpModel.post("/lists/new", Item(text))
    }
  }
}