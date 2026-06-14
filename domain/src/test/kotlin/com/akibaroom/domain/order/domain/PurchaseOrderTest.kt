package com.akibaroom.domain.order.domain

import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.id.IdGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class PurchaseOrderTest {
    private fun purchaseOrder(): PurchaseOrder =
        PurchaseOrder(
            id = IdGenerator.next(),
            memberId = IdGenerator.next(),
            goodsId = IdGenerator.next(),
            quantity = 10,
            amount = 15000L,
            status = OrderStatus.CONFIRMED,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

    @Test
    fun `취소하면 상태가 CANCELLED가 된다`() {
        // given
        val purchaseOrder = purchaseOrder()

        // when
        purchaseOrder.cancel()

        // then
        assertEquals(OrderStatus.CANCELLED, purchaseOrder.status)
    }

    @Test
    fun `이미 취소된 주문을 취소하면 ORDER_003 예외가 발생한다`() {
        // given
        val purchaseOrder = purchaseOrder()
        purchaseOrder.cancel()

        // when
        val exception =
            assertThrows<BusinessException> {
                purchaseOrder.cancel()
            }

        // then
        assertEquals(OrderErrorCode.ORDER_003, exception.errorCode)
    }
}
