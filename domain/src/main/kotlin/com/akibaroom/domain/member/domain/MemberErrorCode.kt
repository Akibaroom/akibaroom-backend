package com.akibaroom.domain.member.domain

import com.akibaroom.shared.common.exception.ErrorCode

enum class MemberErrorCode(
    override val httpStatus: Int,
    override val message: String,
) : ErrorCode {
    MEMBER_001(404, "회원 정보를 찾을 수 없습니다."),
    ;

    override val code: String get() = name
}
