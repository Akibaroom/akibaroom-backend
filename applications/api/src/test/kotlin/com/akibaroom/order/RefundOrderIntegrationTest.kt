package com.akibaroom.order

import com.akibaroom.IntegrationTestBase
import com.akibaroom.domain.order.domain.OrderErrorCode
import com.akibaroom.domain.order.domain.OrderStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class RefundOrderIntegrationTest : IntegrationTestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val memberId = "019ebc6d-2cb7-759d-854f-2728b598628e"
    private val goodsId = "019ebc6d-2cb7-740f-b7fa-1acf0863cbcf"

    @BeforeEach
    fun setUp() {
        jdbcTemplate.update("DELETE FROM money_ledger")
        jdbcTemplate.update("DELETE FROM purchase_order")
        jdbcTemplate.update("UPDATE money_account SET balance = 0")
        jdbcTemplate.update("UPDATE goods_stock SET remaining_quantity = total_quantity")
    }

    private fun charge(amount: Long) {
        mockMvc.perform(
            post("/api/v1/charges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "amount": $amount}"""),
        )
            .andExpect(status().isCreated)
    }

    private fun placeOrder(): String {
        val result =
            mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Idempotency-Key", UUID.randomUUID().toString())
                    .content("""{"memberId": "$memberId", "goodsId": "$goodsId", "quantity": 1}"""),
            )
                .andExpect(status().isCreated)
                .andReturn()

        return objectMapper.readTree(result.response.contentAsString)
            .get("data").get("orderId").asText()
    }

    @Test
    fun `정상 환불 시 잔액이 복구되고 주문이 CANCELLED가 된다`() {
        charge(15000)
        val orderId = placeOrder()

        mockMvc.perform(
            post("/api/v1/orders/$orderId/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.refundAmount").value(15000))
            .andExpect(jsonPath("$.data.balanceAfter").value(15000))

        val orderStatus =
            jdbcTemplate.queryForObject(
                "SELECT status FROM purchase_order WHERE id = UUID_TO_BIN(?)",
                String::class.java,
                orderId,
            )

        assertEquals(OrderStatus.CANCELLED.name, orderStatus)
    }

    @Test
    fun `환불 후 balance와 원장 합이 일치한다`() {
        charge(15000)
        val orderId = placeOrder()

        mockMvc.perform(
            post("/api/v1/orders/$orderId/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.refundAmount").value(15000))
            .andExpect(jsonPath("$.data.balanceAfter").value(15000))

        val balance =
            jdbcTemplate.queryForObject(
                "SELECT balance FROM money_account WHERE member_id = UUID_TO_BIN(?)",
                Long::class.java,
                memberId,
            )

        val ledgerSum =
            jdbcTemplate.queryForObject(
                """
                SELECT SUM(ml.amount)
                FROM money_ledger ml
                JOIN money_account ma ON ml.account_id = ma.id
                WHERE ma.member_id = UUID_TO_BIN(?)
                """.trimIndent(),
                Long::class.java,
                memberId,
            )

        assertEquals(balance, ledgerSum)
    }

    @Test
    fun `존재하지 않는 주문 환불 시 ORDER_002를 반환한다`() {
        val unknownOrderId = UUID.randomUUID().toString()

        mockMvc.perform(
            post("/api/v1/orders/$unknownOrderId/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId"}"""),
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value(OrderErrorCode.ORDER_002.code))
    }

    @Test
    fun `이미 취소된 주문 환불 시 ORDER_003을 반환한다`() {
        charge(15000)
        val orderId = placeOrder()

        mockMvc.perform(
            post("/api/v1/orders/$orderId/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.refundAmount").value(15000))
            .andExpect(jsonPath("$.data.balanceAfter").value(15000))

        mockMvc.perform(
            post("/api/v1/orders/$orderId/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId"}"""),
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value(OrderErrorCode.ORDER_003.code))
    }

    @Test
    fun `다른 회원 주문 환불 시 ORDER_004를 반환한다`() {
        charge(15000)
        val orderId = placeOrder()
        val otherMemberId = UUID.randomUUID().toString()

        mockMvc.perform(
            post("/api/v1/orders/$orderId/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$otherMemberId"}"""),
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.code").value(OrderErrorCode.ORDER_004.code))
    }
}
