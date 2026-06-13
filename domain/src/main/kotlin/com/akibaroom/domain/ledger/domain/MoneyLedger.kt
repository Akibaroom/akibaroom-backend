package com.akibaroom.domain.ledger.domain

import java.time.Instant
import java.util.UUID

class MoneyLedger(
    val id: UUID,
    val accountId: UUID,
    val type: LedgerType,
    val amount: Long,
    val balanceAfter: Long,
    val orderId: UUID? = null,
    val createdAt: Instant,
)
