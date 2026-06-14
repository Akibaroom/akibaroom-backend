package com.akibaroom.domain.order.application

import java.util.UUID

interface RefundLockPort {
    fun withLock(
        orderId: UUID,
        action: () -> RefundOrderResult,
    ): RefundOrderResult
}
