package com.akibaroom.domain.money.application

import com.akibaroom.domain.ledger.domain.LedgerType
import com.akibaroom.domain.ledger.domain.MoneyLedger
import com.akibaroom.domain.ledger.domain.MoneyLedgerRepository
import com.akibaroom.domain.member.domain.MemberErrorCode
import com.akibaroom.domain.member.domain.MemberRepository
import com.akibaroom.domain.money.domain.MoneyAccountRepository
import com.akibaroom.domain.money.domain.MoneyErrorCode
import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.id.IdGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class MoneyService(
    private val memberRepository: MemberRepository,
    private val moneyAccountRepository: MoneyAccountRepository,
    private val moneyLedgerRepository: MoneyLedgerRepository,
) : ChargeMoneyUseCase {
    @Transactional
    override fun charge(chargeMoneyCommand: ChargeMoneyCommand): ChargeMoneyResult {
        val memberId = chargeMoneyCommand.memberId
        val amount = chargeMoneyCommand.amount

        if (memberRepository.findById(memberId) == null) throw BusinessException(MemberErrorCode.MEMBER_001)

        var moneyAccount =
            moneyAccountRepository.findByMemberId(memberId)
                ?: throw BusinessException(MoneyErrorCode.MONEY_003)
        moneyAccount.charge(amount)
        moneyAccount = moneyAccountRepository.save(moneyAccount)

        moneyLedgerRepository.save(
            MoneyLedger(
                id = IdGenerator.next(),
                accountId = moneyAccount.id,
                type = LedgerType.CHARGE,
                amount = amount,
                balanceAfter = moneyAccount.balance,
                orderId = null,
                createdAt = Instant.now(),
            ),
        )

        return ChargeMoneyResult(
            accountId = moneyAccount.id,
            balance = moneyAccount.balance,
        )
    }
}
