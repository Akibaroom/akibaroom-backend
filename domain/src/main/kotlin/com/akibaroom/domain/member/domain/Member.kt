package com.akibaroom.domain.member.domain

import java.time.Instant
import java.util.UUID

class Member(
    val id: UUID,
    val createdAt: Instant,
)
