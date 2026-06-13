package com.akibaroom.infrastructure.jpa.stock

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface GoodsStockJpaRepository : JpaRepository<GoodsStockJpaEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT gse FROM GoodsStockJpaEntity gse
        WHERE gse.id = :id
    """,
    )
    fun findByIdForUpdate(
        @Param("id") id: UUID,
    ): GoodsStockJpaEntity?
}
