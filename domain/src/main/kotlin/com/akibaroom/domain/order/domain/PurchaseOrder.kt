package com.akibaroom.domain.order.domain

import com.akibaroom.shared.common.exception.BusinessException
import java.time.Instant
import java.util.UUID

class PurchaseOrder(
    val id: UUID,
    val memberId: UUID,
    val goodsId: UUID,
    val quantity: Long,
    val amount: Long,
    var status: OrderStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun cancel() {
        if (status != OrderStatus.CONFIRMED) throw BusinessException(OrderErrorCode.ORDER_003)
        status = OrderStatus.CANCELLED
    }
}
