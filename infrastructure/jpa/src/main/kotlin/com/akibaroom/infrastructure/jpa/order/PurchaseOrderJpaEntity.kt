package com.akibaroom.infrastructure.jpa.order

import com.akibaroom.domain.order.domain.OrderStatus
import com.akibaroom.domain.order.domain.PurchaseOrder
import com.akibaroom.infrastructure.jpa.BaseJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "purchase_order")
class PurchaseOrderJpaEntity(
    id: UUID,
    val memberId: UUID,
    val goodsId: UUID,
    val quantity: Long,
    val amount: Long,
    @Enumerated(EnumType.STRING) val status: OrderStatus,
    createdAt: Instant,
    @LastModifiedDate var updatedAt: Instant,
) : BaseJpaEntity(id, createdAt) {
    fun toDomain() =
        PurchaseOrder(
            id = id,
            memberId = memberId,
            goodsId = goodsId,
            quantity = quantity,
            amount = amount,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun from(purchaseOrder: PurchaseOrder) =
            PurchaseOrderJpaEntity(
                id = purchaseOrder.id,
                memberId = purchaseOrder.memberId,
                goodsId = purchaseOrder.goodsId,
                quantity = purchaseOrder.quantity,
                amount = purchaseOrder.amount,
                status = purchaseOrder.status,
                createdAt = purchaseOrder.createdAt,
                updatedAt = purchaseOrder.updatedAt,
            )
    }
}
