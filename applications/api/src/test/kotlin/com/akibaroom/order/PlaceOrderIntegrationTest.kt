package com.akibaroom.order

import com.akibaroom.IntegrationTestBase
import com.akibaroom.domain.member.domain.MemberErrorCode
import com.akibaroom.domain.money.domain.MoneyErrorCode
import com.akibaroom.domain.stock.domain.StockErrorCode
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

class PlaceOrderIntegrationTest : IntegrationTestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val memberId = "019ebc6d-2cb7-759d-854f-2728b598628e"
    private val unknownMemberId = "00000000-0000-0000-0000-000000000000"

    private val goodsId = "019ebc6d-2cb7-740f-b7fa-1acf0863cbcf"
    private val unknownGoodsId = "11111111-1111-1111-1111-111111111111"

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

    @Test
    fun `정식 구매 시 잔액이 차감되고 주문이 생성된다`() {
        charge(15000)

        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "goodsId": "$goodsId", "quantity": 1}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.data.amount").value(15000))
            .andExpect(jsonPath("$.data.balanceAfter").value(0))
    }

    @Test
    fun `정상 구매 시 balance와 원장 합이 일치한다`() {
        charge(15000)

        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "goodsId": "$goodsId", "quantity": 1}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.data.amount").value(15000))
            .andExpect(jsonPath("$.data.balanceAfter").value(0))

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

        assertEquals(
            balance,
            ledgerSum,
        )
    }

    @Test
    fun `존재하지 않는 회원 구매 시 MEMBER_001을 반환한다`() {
        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$unknownMemberId", "goodsId": "$goodsId", "quantity": 1}"""),
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value(MemberErrorCode.MEMBER_001.code))
    }

    @Test
    fun `존재하지 않는 굿즈 구매 시 STOCK_002를 반환한다`() {
        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "goodsId": "$unknownGoodsId", "quantity": 1}"""),
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value(StockErrorCode.STOCK_002.code))
    }

    @Test
    fun `재고 부족 시 STOCK_001을 반환한다`() {
        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "goodsId": "$goodsId", "quantity": 10000}"""),
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value(StockErrorCode.STOCK_001.code))
    }

    @Test
    fun `잔액 부족 시 MONEY_001을 반환하고 재고가 롤백된다`() {
        val beforeQuantity =
            jdbcTemplate.queryForObject(
                "SELECT remaining_quantity FROM goods_stock WHERE id = UUID_TO_BIN(?)",
                Long::class.java,
                goodsId,
            )

        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "goodsId": "$goodsId", "quantity": 1}"""),
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value(MoneyErrorCode.MONEY_001.code))

        val afterQuantity =
            jdbcTemplate.queryForObject(
                "SELECT remaining_quantity FROM goods_stock WHERE id = UUID_TO_BIN(?)",
                Long::class.java,
                goodsId,
            )

        assertEquals(beforeQuantity, afterQuantity)
    }
}
