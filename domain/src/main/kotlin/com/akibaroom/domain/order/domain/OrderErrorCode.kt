package com.akibaroom.domain.order.domain

import com.akibaroom.shared.common.exception.ErrorCode

enum class OrderErrorCode(
    override val httpStatus: Int,
    override val message: String,
) : ErrorCode {
    ORDER_001(429, "요청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해 주세요."),
    ;

    override val code: String get() = name
}
