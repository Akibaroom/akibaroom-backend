package com.akibaroom.infrastructure.jpa.stock

import com.akibaroom.domain.stock.domain.GoodsStock
import com.akibaroom.infrastructure.jpa.BaseJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "goods_stock")
class GoodsStockJpaEntity(
    id: UUID,
    val name: String,
    val price: Long,
    val totalQuantity: Long,
    var remainingQuantity: Long,
    createdAt: Instant,
    @LastModifiedDate var updatedAt: Instant,
) : BaseJpaEntity(id, createdAt) {
    fun toDomain() =
        GoodsStock(
            id = id,
            name = name,
            price = price,
            totalQuantity = totalQuantity,
            remainingQuantity = remainingQuantity,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun from(goodsStock: GoodsStock) =
            GoodsStockJpaEntity(
                id = goodsStock.id,
                name = goodsStock.name,
                price = goodsStock.price,
                totalQuantity = goodsStock.totalQuantity,
                remainingQuantity = goodsStock.remainingQuantity,
                createdAt = goodsStock.createdAt,
                updatedAt = goodsStock.updatedAt,
            )
    }
}
