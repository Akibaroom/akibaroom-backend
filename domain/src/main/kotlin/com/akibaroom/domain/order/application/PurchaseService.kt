package com.akibaroom.domain.order.application

import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val lockPort: LockPort,
    private val idempotencyPort: IdempotencyPort,
    private val purchaseProcessor: PurchaseProcessor,
) : PlaceOrderUseCase {
    override fun placeOrder(placeOrderCommand: PlaceOrderCommand): PlaceOrderResult {
        idempotencyPort.find(placeOrderCommand.idempotencyKey)?.let { return it }

        val result =
            lockPort.withLock(placeOrderCommand.goodsId, placeOrderCommand.memberId) {
                purchaseProcessor.execute(placeOrderCommand)
            }

        idempotencyPort.save(placeOrderCommand.idempotencyKey, result)
        return result
    }
}
