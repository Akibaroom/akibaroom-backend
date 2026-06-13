package com.akibaroom.infrastructure.jpa.stock

import com.akibaroom.domain.stock.domain.GoodsStock
import com.akibaroom.domain.stock.domain.GoodsStockRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class GoodsStockPersistenceAdapter(
    private val goodsStockJpaRepository: GoodsStockJpaRepository,
) : GoodsStockRepository {
    override fun save(goodsStock: GoodsStock): GoodsStock =
        goodsStockJpaRepository.save(GoodsStockJpaEntity.from(goodsStock)).toDomain()

    override fun findById(id: UUID): GoodsStock? = goodsStockJpaRepository.findByIdOrNull(id)?.toDomain()

    @Deprecated("비관적 락 전용. Redis 분산락으로 대체됨.")
    override fun findByIdForUpdate(id: UUID): GoodsStock? = goodsStockJpaRepository.findByIdForUpdate(id)?.toDomain()
}
