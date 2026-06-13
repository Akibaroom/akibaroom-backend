package com.akibaroom.infrastructure.jpa.money

import com.akibaroom.domain.money.domain.MoneyAccount
import com.akibaroom.domain.money.domain.MoneyAccountRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MoneyAccountPersistenceAdapter(
    private val moneyAccountJpaRepository: MoneyAccountJpaRepository,
) : MoneyAccountRepository {
    override fun save(moneyAccount: MoneyAccount): MoneyAccount =
        moneyAccountJpaRepository.save(MoneyAccountJpaEntity.from(moneyAccount)).toDomain()

    override fun findByMemberId(memberId: UUID): MoneyAccount? =
        moneyAccountJpaRepository.findByMemberId(
            memberId,
        )?.toDomain()

    @Deprecated("비관적 락 전용. Redis 분산락으로 대체됨.")
    override fun findByMemberIdForUpdate(memberId: UUID): MoneyAccount? =
        moneyAccountJpaRepository.findByMemberIdForUpdate(
            memberId,
        )?.toDomain()
}
