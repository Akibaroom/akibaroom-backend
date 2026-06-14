package com.akibaroom.domain.order.application

import com.akibaroom.domain.ledger.domain.LedgerType
import com.akibaroom.domain.ledger.domain.MoneyLedger
import com.akibaroom.domain.ledger.domain.MoneyLedgerRepository
import com.akibaroom.domain.money.domain.MoneyAccountRepository
import com.akibaroom.domain.money.domain.MoneyErrorCode
import com.akibaroom.domain.order.domain.OrderErrorCode
import com.akibaroom.domain.order.domain.PurchaseOrderRepository
import com.akibaroom.domain.stock.domain.GoodsStockRepository
import com.akibaroom.domain.stock.domain.StockErrorCode
import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.id.IdGenerator
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
class RefundProcessor(
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val goodsStockRepository: GoodsStockRepository,
    private val moneyAccountRepository: MoneyAccountRepository,
    private val moneyLedgerRepository: MoneyLedgerRepository,
) {
    @Transactional
    fun execute(refundOrderCommand: RefundOrderCommand): RefundOrderResult {
        val orderId = refundOrderCommand.orderId
        val memberId = refundOrderCommand.memberId

        val purchaseOrder =
            purchaseOrderRepository.findById(orderId)
                ?: throw BusinessException(OrderErrorCode.ORDER_002)
        if (memberId != purchaseOrder.memberId) throw BusinessException(OrderErrorCode.ORDER_004)
        purchaseOrder.cancel()

        val goodsStock =
            goodsStockRepository.findById(purchaseOrder.goodsId)
                ?: throw BusinessException(StockErrorCode.STOCK_002)
        goodsStock.increase(purchaseOrder.quantity)
        goodsStockRepository.save(goodsStock)

        var moneyAccount =
            moneyAccountRepository.findByMemberId(memberId)
                ?: throw BusinessException(MoneyErrorCode.MONEY_003)
        val amount = purchaseOrder.amount
        moneyAccount.charge(amount)
        moneyAccount = moneyAccountRepository.save(moneyAccount)

        purchaseOrderRepository.save(purchaseOrder)

        moneyLedgerRepository.save(
            MoneyLedger(
                id = IdGenerator.next(),
                accountId = moneyAccount.id,
                type = LedgerType.REFUND,
                amount = amount,
                balanceAfter = moneyAccount.balance,
                orderId = purchaseOrder.id,
                createdAt = Instant.now(),
            ),
        )

        return RefundOrderResult(
            orderId,
            amount,
            moneyAccount.balance,
        )
    }
}
