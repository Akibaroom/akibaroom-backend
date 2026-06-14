package com.akibaroom.domain.order.application

import com.akibaroom.domain.ledger.domain.LedgerType
import com.akibaroom.domain.ledger.domain.MoneyLedger
import com.akibaroom.domain.ledger.domain.MoneyLedgerRepository
import com.akibaroom.domain.member.domain.MemberErrorCode
import com.akibaroom.domain.member.domain.MemberRepository
import com.akibaroom.domain.money.domain.MoneyAccountRepository
import com.akibaroom.domain.money.domain.MoneyErrorCode
import com.akibaroom.domain.order.domain.OrderStatus
import com.akibaroom.domain.order.domain.PurchaseOrder
import com.akibaroom.domain.order.domain.PurchaseOrderRepository
import com.akibaroom.domain.stock.domain.GoodsStockRepository
import com.akibaroom.domain.stock.domain.StockErrorCode
import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.id.IdGenerator
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
class PurchaseProcessor(
    private val memberRepository: MemberRepository,
    private val goodsStockRepository: GoodsStockRepository,
    private val moneyAccountRepository: MoneyAccountRepository,
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val moneyLedgerRepository: MoneyLedgerRepository,
) {
    @Transactional
    fun execute(placeOrderCommand: PlaceOrderCommand): PlaceOrderResult {
        val memberId = placeOrderCommand.memberId
        val goodsId = placeOrderCommand.goodsId
        val quantity = placeOrderCommand.quantity

        if (memberRepository.findById(memberId) == null) throw BusinessException(MemberErrorCode.MEMBER_001)

        val goodsStock = goodsStockRepository.findById(goodsId) ?: throw BusinessException(StockErrorCode.STOCK_002)
        goodsStock.decrease(quantity)
        goodsStockRepository.save(goodsStock)

        var moneyAccount =
            moneyAccountRepository.findByMemberId(memberId)
                ?: throw BusinessException(MoneyErrorCode.MONEY_003)
        val amount = goodsStock.price * quantity
        moneyAccount.withdraw(amount)
        moneyAccount = moneyAccountRepository.save(moneyAccount)

        val purchaseOrder =
            purchaseOrderRepository.save(
                PurchaseOrder(
                    id = IdGenerator.next(),
                    memberId = memberId,
                    goodsId = goodsId,
                    quantity = quantity,
                    amount = amount,
                    status = OrderStatus.CONFIRMED,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                ),
            )

        moneyLedgerRepository.save(
            MoneyLedger(
                id = IdGenerator.next(),
                accountId = moneyAccount.id,
                type = LedgerType.PURCHASE,
                amount = -amount,
                balanceAfter = moneyAccount.balance,
                orderId = purchaseOrder.id,
                createdAt = Instant.now(),
            ),
        )

        return PlaceOrderResult(
            orderId = purchaseOrder.id,
            amount = amount,
            balanceAfter = moneyAccount.balance,
        )
    }
}
