package com.akibaroom.infrastructure.jpa.order

import com.akibaroom.domain.order.domain.PurchaseOrder
import com.akibaroom.domain.order.domain.PurchaseOrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PurchaseOrderPersistenceAdapter(
    private val purchaseOrderJpaRepository: PurchaseOrderJpaRepository,
) : PurchaseOrderRepository {
    override fun save(purchaseOrder: PurchaseOrder): PurchaseOrder =
        purchaseOrderJpaRepository.save(PurchaseOrderJpaEntity.from(purchaseOrder)).toDomain()

    override fun findById(id: UUID): PurchaseOrder? = purchaseOrderJpaRepository.findByIdOrNull(id)?.toDomain()
}
