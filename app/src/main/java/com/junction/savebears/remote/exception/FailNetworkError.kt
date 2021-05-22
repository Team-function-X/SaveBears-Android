package com.junction.savebears.remote.exception

import java.lang.Exception

/**
 * 예기치 못한 네트워크 에러가 발생했을 때의 Throwable
 */
class FailNetworkError(message: String): Exception(message)