package com.kentvu.toolbox.models

class FallBackToLocalDataSourceException(val data: List<Item>?, cause: Exception): Exception(cause) {}
