package com.akibaroom.domain.order.application

import org.springframework.stereotype.Service

@Service
class RefundService(
    private val refundLockPort: RefundLockPort,
    private val refundProcessor: RefundProcessor,
) : RefundOrderUseCase {
    override fun refundOrder(refundOrderCommand: RefundOrderCommand): RefundOrderResult =
        refundLockPort.withLock(refundOrderCommand.orderId) {
            refundProcessor.execute(refundOrderCommand)
        }
}
