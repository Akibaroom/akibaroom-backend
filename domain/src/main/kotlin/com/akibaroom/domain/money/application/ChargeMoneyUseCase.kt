package com.akibaroom.domain.money.application

interface ChargeMoneyUseCase {
    fun charge(chargeMoneyCommand: ChargeMoneyCommand): ChargeMoneyResult
}
