package com.akibaroom.infrastructure.jpa.ledger

import com.akibaroom.domain.ledger.domain.LedgerType
import com.akibaroom.domain.ledger.domain.MoneyLedger
import com.akibaroom.infrastructure.jpa.BaseJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "money_ledger")
class MoneyLedgerJpaEntity(
    id: UUID,
    val accountId: UUID,
    @Enumerated(EnumType.STRING) val type: LedgerType,
    val amount: Long,
    val balanceAfter: Long,
    val orderId: UUID?,
    createdAt: Instant,
) : BaseJpaEntity(id, createdAt) {
    fun toDomain() =
        MoneyLedger(
            id = id,
            accountId = accountId,
            type = type,
            amount = amount,
            balanceAfter = balanceAfter,
            orderId = orderId,
            createdAt = createdAt,
        )

    companion object {
        fun from(moneyLedger: MoneyLedger) =
            MoneyLedgerJpaEntity(
                id = moneyLedger.id,
                accountId = moneyLedger.accountId,
                type = moneyLedger.type,
                amount = moneyLedger.amount,
                balanceAfter = moneyLedger.balanceAfter,
                orderId = moneyLedger.orderId,
                createdAt = moneyLedger.createdAt,
            )
    }
}
