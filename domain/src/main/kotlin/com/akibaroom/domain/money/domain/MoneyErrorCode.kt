package com.akibaroom.domain.money.domain

import com.akibaroom.shared.common.exception.ErrorCode

enum class MoneyErrorCode(
    override val httpStatus: Int,
    override val message: String,
) : ErrorCode {
    MONEY_001(409, "잔액이 부족합니다."),
    MONEY_002(400, "금액은 1원 이상이어야 합니다."),
    MONEY_003(404, "머니 계좌를 찾을 수 없습니다."),
    ;

    override val code: String get() = name
}
