package com.akibaroom.domain.order.domain

import java.util.UUID

interface PurchaseOrderRepository {
    fun save(purchaseOrder: PurchaseOrder): PurchaseOrder

    fun findById(id: UUID): PurchaseOrder?
}
