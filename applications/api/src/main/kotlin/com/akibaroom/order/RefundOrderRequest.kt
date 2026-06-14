package com.akibaroom.order

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RefundOrderRequest(
    @field:NotNull val memberId: UUID?,
)
