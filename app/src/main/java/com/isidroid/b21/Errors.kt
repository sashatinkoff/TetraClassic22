package com.isidroid.b21

abstract class IThrowable(message: String, var error: String? = null) : Throwable(message)

class SendMessageException(msg: String) : Throwable(msg)