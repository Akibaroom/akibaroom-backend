package com.akibaroom.domain.order.application

interface IdempotencyPort {
    fun find(key: String): PlaceOrderResult?

    fun save(
        key: String,
        result: PlaceOrderResult,
    )
}
