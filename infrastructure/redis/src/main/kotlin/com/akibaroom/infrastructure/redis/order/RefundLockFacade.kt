package com.akibaroom.infrastructure.redis.order

import com.akibaroom.domain.order.application.RefundLockPort
import com.akibaroom.domain.order.application.RefundOrderResult
import com.akibaroom.domain.order.domain.OrderErrorCode
import com.akibaroom.shared.common.exception.BusinessException
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
class RefundLockFacade(
    private val redissonClient: RedissonClient,
) : RefundLockPort {
    override fun withLock(
        orderId: UUID,
        action: () -> RefundOrderResult,
    ): RefundOrderResult {
        val orderLock = redissonClient.getLock("lock:order:$orderId")

        if (!orderLock.tryLock(3, 5, TimeUnit.SECONDS)) {
            throw BusinessException(OrderErrorCode.ORDER_001)
        }
        try {
            return action()
        } finally {
            orderLock.unlock()
        }
    }
}
