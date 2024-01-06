package com.lawyersbuddy.app.data
import java.io.IOException
import java.net.SocketTimeoutException
class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
class TimeOutException(message: String) : SocketTimeoutException(message)