package com.akibaroom.domain.stock.domain

import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.id.IdGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class GoodsStockTest {
    private fun goodsStock(remainingQuantity: Long): GoodsStock =
        GoodsStock(
            id = IdGenerator.next(),
            name = "테스트 굿즈",
            price = 10000L,
            totalQuantity = 100L,
            remainingQuantity = remainingQuantity,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

    @Test
    fun `차감하면 남은 수량이 금액만큼 감소한다`() {
        // given
        val stock = goodsStock(100L)

        // when
        stock.decrease(10L)

        // then
        assertEquals(90L, stock.remainingQuantity)
    }

    @Test
    fun `남은 수량과 같은 수량을 차감하면 0이 되고 예외가 발생하지 않는다`() {
        // given
        val stock = goodsStock(100L)

        // when
        stock.decrease(100L)

        // then
        assertEquals(0L, stock.remainingQuantity)
    }

    @Test
    fun `남은 수량보다 많이 차감하면 STOCK_001 예외가 발생한다`() {
        // given
        val stock = goodsStock(100L)

        // when
        val exception =
            assertThrows<BusinessException> {
                stock.decrease(101L)
            }

        // then
        assertEquals(StockErrorCode.STOCK_001, exception.errorCode)
    }

    @Test
    fun `0개를 차감하면 STOCK_003 예외가 발생한다`() {
        // given
        val stock = goodsStock(100L)

        // when
        val exception =
            assertThrows<BusinessException> {
                stock.decrease(0L)
            }

        // then
        assertEquals(StockErrorCode.STOCK_003, exception.errorCode)
    }

    @Test
    fun `음수를 차감하면 STOCK_003 예외가 발생한다`() {
        // given
        val stock = goodsStock(100L)

        // when
        val exception =
            assertThrows<BusinessException> {
                stock.decrease(-1L)
            }

        // then
        assertEquals(StockErrorCode.STOCK_003, exception.errorCode)
    }
}
