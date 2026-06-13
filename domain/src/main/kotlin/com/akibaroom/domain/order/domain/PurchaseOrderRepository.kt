package com.akibaroom.domain.order.domain

interface PurchaseOrderRepository {
    fun save(purchaseOrder: PurchaseOrder): PurchaseOrder
}
