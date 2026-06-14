package com.akibaroom.infrastructure.redis.idempotency

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class IdempotencyStore(
    private val redisTemplate: StringRedisTemplate,
) {
    fun find(key: String): String? = redisTemplate.opsForValue().get(key)

    fun save(
        key: String,
        value: String,
        ttl: Duration,
    ) = redisTemplate.opsForValue().set(key, value, ttl)
}
