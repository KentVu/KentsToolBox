package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item

class Backend {
    enum class Action {
        NewItem
    }

    fun post(action: Action, item: Item):Response {
        return Response(emptyList())
    }

}
