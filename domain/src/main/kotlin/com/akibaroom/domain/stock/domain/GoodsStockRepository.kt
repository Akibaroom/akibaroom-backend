package com.akibaroom.domain.stock.domain

import java.util.UUID

interface GoodsStockRepository {
    fun save(goodsStock: GoodsStock): GoodsStock

    fun findById(id: UUID): GoodsStock?

    @Deprecated("비관적 락 전용. Redis 분산락으로 대체됨.")
    fun findByIdForUpdate(id: UUID): GoodsStock?
}
