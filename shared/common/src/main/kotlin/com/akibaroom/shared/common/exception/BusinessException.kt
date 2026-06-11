package com.akibaroom.shared.common.exception

open class BusinessException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
