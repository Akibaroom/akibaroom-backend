package com.akibaroom.domain.order.application

interface PlaceOrderUseCase {
    fun placeOrder(placeOrderCommand: PlaceOrderCommand): PlaceOrderResult
}
