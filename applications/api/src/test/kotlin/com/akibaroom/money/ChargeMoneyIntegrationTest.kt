package com.akibaroom.money

import com.akibaroom.IntegrationTestBase
import com.akibaroom.domain.member.domain.MemberErrorCode
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

class ChargeMoneyIntegrationTest : IntegrationTestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val memberId = "019ebc6d-2cb7-759d-854f-2728b598628e"
    private val unknownMemberId = "00000000-0000-0000-0000-000000000000"

    @BeforeEach
    fun setUp() {
        jdbcTemplate.update("DELETE FROM money_ledger")
        jdbcTemplate.update("UPDATE money_account SET balance = 0")
    }

    @Test
    fun `정상 충전 시 잔액이 충전금액만큼 증가한다`() {
        mockMvc.perform(
            post("/api/v1/charges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "amount": 1000}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.data.balance").value(1000))
    }

    @Test
    fun `정상 충전 시 balance와 원장 합이 일치한다`() {
        mockMvc.perform(
            post("/api/v1/charges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$memberId", "amount": 1000}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.data.balance").value(1000))

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
    fun `존재하지 않는 회원 충전 시 MEMBER_001을 반환한다`() {
        mockMvc.perform(
            post("/api/v1/charges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"memberId": "$unknownMemberId", "amount": 1000}"""),
        )
            .andExpect(status().isNotFound)
            .andExpect(
                jsonPath("$.code").value(MemberErrorCode.MEMBER_001.code),
            )
    }
}
