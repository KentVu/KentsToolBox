package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item

interface Backend {
    enum class Action {
        NewItem
    }

    fun post(action: Action, item: Item): Response

    class Default : Backend {

        override fun post(action: Action, item: Item): Response {
            return Response(emptyList())
        }
    }
}
