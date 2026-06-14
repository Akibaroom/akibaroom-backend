package com.akibaroom.infrastructure.redis.order

import com.akibaroom.domain.order.application.LockPort
import com.akibaroom.domain.order.application.PlaceOrderResult
import com.akibaroom.domain.order.domain.OrderErrorCode
import com.akibaroom.shared.common.exception.BusinessException
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
class PurchaseLockFacade(
    private val redissonClient: RedissonClient,
) : LockPort {
    override fun withLock(
        goodsId: UUID,
        memberId: UUID,
        action: () -> PlaceOrderResult,
    ): PlaceOrderResult {
        val goodsLock = redissonClient.getLock("lock:goods:$goodsId")
        val accountLock = redissonClient.getLock("lock:account:$memberId")
        val multiLock = redissonClient.getMultiLock(goodsLock, accountLock)

        if (!multiLock.tryLock(3, 5, TimeUnit.SECONDS)) {
            throw BusinessException(OrderErrorCode.ORDER_001)
        }
        try {
            return action()
        } finally {
            multiLock.unlock()
        }
    }
}
