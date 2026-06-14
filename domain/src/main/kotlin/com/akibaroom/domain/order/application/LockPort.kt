package com.akibaroom.domain.order.application

import java.util.UUID

interface LockPort {
    fun withLock(
        goodsId: UUID,
        memberId: UUID,
        action: () -> PlaceOrderResult,
    ): PlaceOrderResult
}
