package com.akibaroom.infrastructure.redis.order

import com.akibaroom.domain.order.application.PlaceOrderCommand
import com.akibaroom.domain.order.application.PlaceOrderResult
import com.akibaroom.domain.order.application.PlaceOrderUseCase
import com.akibaroom.domain.order.application.PurchaseService
import com.akibaroom.domain.order.domain.OrderErrorCode
import com.akibaroom.shared.common.exception.BusinessException
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PurchaseLockFacade(
    private val redissonClient: RedissonClient,
    private val purchaseService: PurchaseService
) : PlaceOrderUseCase {
    override fun placeOrder(placeOrderCommand: PlaceOrderCommand): PlaceOrderResult {
        val goodsLock = redissonClient.getLock("lock:goods:${placeOrderCommand.goodsId}")
        val accountLock = redissonClient.getLock("lock:account:${placeOrderCommand.memberId}")
        val multiLock = redissonClient.getMultiLock(goodsLock, accountLock)

        if (!multiLock.tryLock(3, 5, TimeUnit.SECONDS)) {
            throw BusinessException(OrderErrorCode.ORDER_001)
        }
        try {
            return purchaseService.placeOrder(placeOrderCommand)
        } finally {
            multiLock.unlock()
        }
    }
}
