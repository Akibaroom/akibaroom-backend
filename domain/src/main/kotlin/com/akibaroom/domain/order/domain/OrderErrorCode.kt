package com.akibaroom.domain.order.domain

import com.akibaroom.shared.common.exception.ErrorCode

enum class OrderErrorCode(
    override val httpStatus: Int,
    override val message: String,
) : ErrorCode {
    ORDER_001(429, "요청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해 주세요."),
    ORDER_002(404, "주문을 찾을 수 없습니다."),
    ORDER_003(409, "이미 취소된 주문입니다."),
    ORDER_004(403, "해당 주문에 대한 권한이 없습니다."),
    ;

    override val code: String get() = name
}
