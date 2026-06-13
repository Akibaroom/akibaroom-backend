package com.akibaroom.domain.order.domain

import java.time.Instant
import java.util.UUID

class PurchaseOrder(
    val id: UUID,
    val memberId: UUID,
    val goodsId: UUID,
    val quantity: Long,
    val amount: Long,
    val status: OrderStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)
