package com.akibaroom.domain.money.domain

import java.util.UUID

interface MoneyAccountRepository {
    fun save(moneyAccount: MoneyAccount): MoneyAccount

    fun findByMemberId(memberId: UUID): MoneyAccount?

    @Deprecated("비관적 락 전용. Redis 분산락으로 대체됨.")
    fun findByMemberIdForUpdate(memberId: UUID): MoneyAccount?
}
