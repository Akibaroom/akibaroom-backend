package com.akibaroom.infrastructure.jpa.money

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MoneyAccountJpaRepository : JpaRepository<MoneyAccountJpaEntity, UUID> {
    fun findByMemberId(memberId: UUID): MoneyAccountJpaEntity?
}
