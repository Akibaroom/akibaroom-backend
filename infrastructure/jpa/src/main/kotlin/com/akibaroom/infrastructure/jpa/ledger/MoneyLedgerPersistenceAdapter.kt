package com.akibaroom.infrastructure.jpa.ledger

import com.akibaroom.domain.ledger.domain.MoneyLedger
import com.akibaroom.domain.ledger.domain.MoneyLedgerRepository
import org.springframework.stereotype.Repository

@Repository
class MoneyLedgerPersistenceAdapter(
    private val moneyLedgerJpaRepository: MoneyLedgerJpaRepository,
) : MoneyLedgerRepository {
    override fun save(moneyLedger: MoneyLedger): MoneyLedger =
        moneyLedgerJpaRepository.save(MoneyLedgerJpaEntity.from(moneyLedger)).toDomain()
}
