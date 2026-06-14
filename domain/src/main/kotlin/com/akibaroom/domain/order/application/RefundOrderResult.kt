package com.akibaroom.domain.order.application

import java.util.UUID

data class RefundOrderResult(
    val orderId: UUID,
    val refundAmount: Long,
    val balanceAfter: Long,
)
