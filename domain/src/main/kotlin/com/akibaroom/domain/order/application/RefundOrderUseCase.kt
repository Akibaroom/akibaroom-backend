package com.akibaroom.domain.order.application

interface RefundOrderUseCase {
    fun refundOrder(refundOrderCommand: RefundOrderCommand): RefundOrderResult
}
