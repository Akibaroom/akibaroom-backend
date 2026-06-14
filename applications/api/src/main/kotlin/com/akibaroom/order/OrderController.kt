package com.akibaroom.order

import com.akibaroom.domain.order.application.PlaceOrderCommand
import com.akibaroom.domain.order.application.PlaceOrderResult
import com.akibaroom.domain.order.application.PlaceOrderUseCase
import com.akibaroom.domain.order.application.RefundOrderCommand
import com.akibaroom.domain.order.application.RefundOrderResult
import com.akibaroom.domain.order.application.RefundOrderUseCase
import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.exception.CommonErrorCode
import com.akibaroom.shared.common.response.SuccessResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val refundOrderUseCase: RefundOrderUseCase,
) {
    @PostMapping
    fun placeOrder(
        @RequestBody @Valid placeOrderRequest: PlaceOrderRequest,
        @RequestHeader("Idempotency-Key", required = false) idempotencyKey: String?,
    ): ResponseEntity<SuccessResponse<PlaceOrderResult>> {
        if (idempotencyKey.isNullOrBlank()) throw BusinessException(CommonErrorCode.COMMON_006)

        val placeOrderCommand =
            PlaceOrderCommand(
                memberId = placeOrderRequest.memberId!!,
                goodsId = placeOrderRequest.goodsId!!,
                quantity = placeOrderRequest.quantity!!,
                idempotencyKey = idempotencyKey,
            )
        val placeOrderResult = placeOrderUseCase.placeOrder(placeOrderCommand)
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse(placeOrderResult))
    }

    @PostMapping("/{orderId}/refund")
    fun refundOrder(
        @PathVariable orderId: UUID,
        @RequestBody @Valid refundOrderRequest: RefundOrderRequest,
    ): ResponseEntity<SuccessResponse<RefundOrderResult>> {
        val refundOrderCommand =
            RefundOrderCommand(
                orderId = orderId,
                memberId = refundOrderRequest.memberId!!,
            )
        val result = refundOrderUseCase.refundOrder(refundOrderCommand)
        return ResponseEntity.ok(SuccessResponse(result))
    }
}
