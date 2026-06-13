package com.akibaroom.domain.money.domain

import java.util.UUID

interface MoneyAccountRepository {
    fun save(moneyAccount: MoneyAccount): MoneyAccount

    fun findByMemberId(memberId: UUID): MoneyAccount?

    fun findByMemberIdForUpdate(memberId: UUID): MoneyAccount?
}
