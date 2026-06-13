package com.akibaroom.domain.member.domain

import java.util.UUID

interface MemberRepository {
    fun findById(id: UUID): Member?
}
