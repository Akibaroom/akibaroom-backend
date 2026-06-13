package com.akibaroom.domain.order.application

import java.util.UUID

data class PlaceOrderResult(
    val orderId: UUID,
    val amount: Long,
    val balanceAfter: Long,
)
