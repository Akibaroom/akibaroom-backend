package com.akibaroom.infrastructure.jpa.money

import com.akibaroom.domain.money.domain.MoneyAccount
import com.akibaroom.infrastructure.jpa.BaseJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "money_account")
class MoneyAccountJpaEntity(
    id: UUID,
    val memberId: UUID,
    var balance: Long,
    createdAt: Instant,
    @LastModifiedDate var updatedAt: Instant,
) : BaseJpaEntity(id, createdAt) {
    fun toDomain() =
        MoneyAccount(
            id = id,
            memberId = memberId,
            balance = balance,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun from(moneyAccount: MoneyAccount) =
            MoneyAccountJpaEntity(
                id = moneyAccount.id,
                memberId = moneyAccount.memberId,
                balance = moneyAccount.balance,
                createdAt = moneyAccount.createdAt,
                updatedAt = moneyAccount.updatedAt,
            )
    }
}
