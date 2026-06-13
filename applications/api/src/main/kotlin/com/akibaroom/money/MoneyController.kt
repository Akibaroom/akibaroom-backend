package com.akibaroom.money

import com.akibaroom.domain.money.application.ChargeMoneyCommand
import com.akibaroom.domain.money.application.ChargeMoneyResult
import com.akibaroom.domain.money.application.ChargeMoneyUseCase
import com.akibaroom.shared.common.response.SuccessResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/charges")
class MoneyController(
    private val chargeMoneyUseCase: ChargeMoneyUseCase,
) {
    @PostMapping
    fun charge(
        @RequestBody @Valid chargeMoneyRequest: ChargeMoneyRequest,
    ): ResponseEntity<SuccessResponse<ChargeMoneyResult>> {
        val chargeMoneyCommand =
            ChargeMoneyCommand(
                memberId = chargeMoneyRequest.memberId!!,
                amount = chargeMoneyRequest.amount!!,
            )
        val chargeMoneyResult = chargeMoneyUseCase.charge(chargeMoneyCommand)
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse(chargeMoneyResult))
    }
}
