package com.junction.savebears.remote.exception

import java.lang.Exception

/**
 * 응답 데이터가 비어있을 때의 Throwable
 */
class EmptyBodyError(message: String): Exception(message)