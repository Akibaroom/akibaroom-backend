package com.akibaroom.domain.stock.domain

import com.akibaroom.shared.common.exception.ErrorCode

enum class StockErrorCode(
    override val httpStatus: Int,
    override val message: String,
) : ErrorCode {
    STOCK_001(409, "재고가 부족합니다."),
    STOCK_002(404, "상품 정보를 찾을 수 없습니다."),
    STOCK_003(400, "수량은 1개 이상이어야 합니다."),
    ;

    override val code: String get() = name
}
