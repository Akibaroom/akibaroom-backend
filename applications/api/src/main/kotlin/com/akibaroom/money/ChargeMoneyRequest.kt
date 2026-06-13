package com.akibaroom.money

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class ChargeMoneyRequest(
    @field:NotNull val memberId: UUID?,
    @field:NotNull @field:Positive val amount: Long?,
)
