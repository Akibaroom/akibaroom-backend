package com.akibaroom.shared.common.exception

enum class CommonErrorCode(
    override val httpStatus: Int,
    override val message: String,
) : ErrorCode {
    COMMON_001(400, "입력값이 올바르지 않습니다."),
    COMMON_002(400, "요청 형식이 올바르지 않습니다."),
    COMMON_003(405, "지원하지 않는 요청 방식입니다."),
    COMMON_004(415, "지원하지 않는 요청 형식입니다."),
    COMMON_005(429, "요청이 너무 많습니다. 잠시 후 다시 시도해 주세요."),
    COMMON_006(400, "요청에 Idempotency-Key 헤더가 필요합니다."),
    COMMON_999(500, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    ;

    override val code: String get() = name
}
