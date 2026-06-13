package com.akibaroom.domain.stock.domain

import java.util.UUID

interface GoodsStockRepository {
    fun save(goodsStock: GoodsStock): GoodsStock

    fun findById(id: UUID): GoodsStock?

    fun findByIdForUpdate(id: UUID): GoodsStock?
}
