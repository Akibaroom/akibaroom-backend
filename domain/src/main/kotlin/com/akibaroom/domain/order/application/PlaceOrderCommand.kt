package com.akibaroom.domain.order.application

import java.util.UUID

data class PlaceOrderCommand(
    val memberId: UUID,
    val goodsId: UUID,
    val quantity: Long,
    val idempotencyKey: String,
)
