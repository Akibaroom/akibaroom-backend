package com.akibaroom.domain.money.application

import java.util.UUID

data class ChargeMoneyResult(
    val accountId: UUID,
    val balance: Long,
)
