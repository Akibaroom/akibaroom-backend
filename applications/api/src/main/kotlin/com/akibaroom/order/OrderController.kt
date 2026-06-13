package com.akibaroom.order

import com.akibaroom.domain.order.application.PlaceOrderCommand
import com.akibaroom.domain.order.application.PlaceOrderResult
import com.akibaroom.domain.order.application.PlaceOrderUseCase
import com.akibaroom.shared.common.response.SuccessResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val placeOrderUseCase: PlaceOrderUseCase,
) {
    @PostMapping
    fun placeOrder(
        @RequestBody @Valid placeOrderRequest: PlaceOrderRequest,
    ): ResponseEntity<SuccessResponse<PlaceOrderResult>> {
        val placeOrderCommand =
            PlaceOrderCommand(
                memberId = placeOrderRequest.memberId!!,
                goodsId = placeOrderRequest.goodsId!!,
                quantity = placeOrderRequest.quantity!!,
            )
        val placeOrderResult = placeOrderUseCase.placeOrder(placeOrderCommand)
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse(placeOrderResult))
    }
}
