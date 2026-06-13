package com.akibaroom.domain.money.domain

import com.akibaroom.shared.common.exception.BusinessException
import java.time.Instant
import java.util.UUID

class MoneyAccount(
    val id: UUID,
    val memberId: UUID,
    var balance: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun charge(amount: Long) {
        if (amount <= 0) throw BusinessException(MoneyErrorCode.MONEY_002)
        balance += amount
    }

    fun withdraw(amount: Long) {
        if (amount <= 0) throw BusinessException(MoneyErrorCode.MONEY_002)
        if (balance < amount) throw BusinessException(MoneyErrorCode.MONEY_001)
        balance -= amount
    }
}
