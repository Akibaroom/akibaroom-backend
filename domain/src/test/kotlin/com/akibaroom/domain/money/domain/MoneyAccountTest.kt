package com.akibaroom.domain.money.domain

import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.id.IdGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class MoneyAccountTest {
    private fun moneyAccount(balance: Long): MoneyAccount =
        MoneyAccount(
            id = IdGenerator.next(),
            memberId = IdGenerator.next(),
            balance = balance,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

    @Test
    fun `충전하면 잔액이 금액만큼 증가한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        account.charge(1000L)

        // then
        assertEquals(2000L, account.balance)
    }

    @Test
    fun `출금하면 잔액이 금액만큼 감소한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        account.withdraw(700L)

        // then
        assertEquals(300L, account.balance)
    }

    @Test
    fun `잔액과 같은 금액을 출금하면 잔액이 0이 되고 예외가 발생하지 않는다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        account.withdraw(1000L)

        // then
        assertEquals(0L, account.balance)
    }

    @Test
    fun `잔액보다 큰 금액을 출금하면 MONEY_001 예외가 발생한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        val exception =
            assertThrows<BusinessException> {
                account.withdraw(2000L)
            }

        // then
        assertEquals(MoneyErrorCode.MONEY_001, exception.errorCode)
    }

    @Test
    fun `0원을 충전하면 MONEY_002 예외가 발생한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        val exception =
            assertThrows<BusinessException> {
                account.charge(0L)
            }

        // then
        assertEquals(MoneyErrorCode.MONEY_002, exception.errorCode)
    }

    @Test
    fun `음수를 충전하면 MONEY_002 예외가 발생한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        val exception =
            assertThrows<BusinessException> {
                account.charge(-1L)
            }

        // then
        assertEquals(MoneyErrorCode.MONEY_002, exception.errorCode)
    }

    @Test
    fun `0원을 출금하면 MONEY_002 예외가 발생한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        val exception =
            assertThrows<BusinessException> {
                account.withdraw(0L)
            }

        // then
        assertEquals(MoneyErrorCode.MONEY_002, exception.errorCode)
    }

    @Test
    fun `음수를 출금하면 MONEY_002 예외가 발생한다`() {
        // given
        val account = moneyAccount(1000L)

        // when
        val exception =
            assertThrows<BusinessException> {
                account.withdraw(-1L)
            }

        // then
        assertEquals(MoneyErrorCode.MONEY_002, exception.errorCode)
    }
}
