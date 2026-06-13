package com.akibaroom.domain.ledger.domain

interface MoneyLedgerRepository {
    fun save(moneyLedger: MoneyLedger): MoneyLedger
}
