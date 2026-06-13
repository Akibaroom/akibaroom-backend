package com.akibaroom.order

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class PlaceOrderRequest(
    @field:NotNull val memberId: UUID?,
    @field:NotNull val goodsId: UUID?,
    @field:NotNull @field:Positive val quantity: Long?,
)
