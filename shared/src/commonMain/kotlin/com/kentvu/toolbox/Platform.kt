package com.kentvu.toolbox

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform