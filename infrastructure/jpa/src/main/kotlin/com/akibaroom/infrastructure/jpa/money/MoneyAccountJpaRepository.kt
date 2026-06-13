package com.akibaroom.infrastructure.jpa.money

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface MoneyAccountJpaRepository : JpaRepository<MoneyAccountJpaEntity, UUID> {
    fun findByMemberId(memberId: UUID): MoneyAccountJpaEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT mae FROM MoneyAccountJpaEntity mae
        WHERE mae.memberId = :memberId
    """,
    )
    fun findByMemberIdForUpdate(
        @Param("memberId") memberId: UUID,
    ): MoneyAccountJpaEntity?
}
