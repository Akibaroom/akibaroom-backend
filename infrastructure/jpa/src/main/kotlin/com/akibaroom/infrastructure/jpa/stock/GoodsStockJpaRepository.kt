package com.akibaroom.infrastructure.jpa.stock

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GoodsStockJpaRepository : JpaRepository<GoodsStockJpaEntity, UUID>
