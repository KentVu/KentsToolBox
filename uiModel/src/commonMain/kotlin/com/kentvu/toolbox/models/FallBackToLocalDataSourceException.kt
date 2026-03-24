package com.kentvu.toolbox.models

class FallBackToLocalDataSourceException(val data: Any?, cause: Exception): Exception(cause) {}
