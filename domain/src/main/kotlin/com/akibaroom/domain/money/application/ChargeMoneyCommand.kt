package com.akibaroom.domain.money.application

import java.util.UUID

data class ChargeMoneyCommand(
    val memberId: UUID,
    val amount: Long,
)
