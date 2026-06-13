package com.akibaroom.infrastructure.jpa.order

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PurchaseOrderJpaRepository : JpaRepository<PurchaseOrderJpaEntity, UUID>
