package com.akibaroom.domain.order.application

import java.util.UUID

data class RefundOrderCommand(
    val orderId: UUID,
    val memberId: UUID,
)
