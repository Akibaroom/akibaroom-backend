package com.akibaroom.domain.stock.domain

import com.akibaroom.shared.common.exception.BusinessException
import java.time.Instant
import java.util.UUID

class GoodsStock(
    val id: UUID,
    val name: String,
    val price: Long,
    val totalQuantity: Long,
    var remainingQuantity: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun decrease(quantity: Long) {
        if (quantity <= 0) throw BusinessException(StockErrorCode.STOCK_003)
        if (remainingQuantity < quantity) throw BusinessException(StockErrorCode.STOCK_001)
        remainingQuantity -= quantity
    }

    fun increase(quantity: Long) {
        if (quantity <= 0) throw BusinessException(StockErrorCode.STOCK_003)
        remainingQuantity += quantity
    }
}
