package com.akibaroom.infrastructure.redis.order

import com.akibaroom.domain.order.application.IdempotencyPort
import com.akibaroom.domain.order.application.PlaceOrderResult
import com.akibaroom.infrastructure.redis.idempotency.IdempotencyStore
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class IdempotencyOrderFacade(
    private val idempotencyStore: IdempotencyStore,
    private val objectMapper: ObjectMapper,
) : IdempotencyPort {
    override fun find(key: String): PlaceOrderResult? {
        val redisKey = "idempotency:order:$key"
        return idempotencyStore.find(redisKey)?.let {
            objectMapper.readValue(it, PlaceOrderResult::class.java)
        }
    }

    override fun save(
        key: String,
        result: PlaceOrderResult,
    ) {
        val redisKey = "idempotency:order:$key"
        idempotencyStore.save(redisKey, objectMapper.writeValueAsString(result), Duration.ofMinutes(5))
    }
}
