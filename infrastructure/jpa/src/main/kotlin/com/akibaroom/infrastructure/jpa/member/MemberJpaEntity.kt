package com.akibaroom.infrastructure.jpa.member

import com.akibaroom.domain.member.domain.Member
import com.akibaroom.infrastructure.jpa.BaseJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "member")
class MemberJpaEntity(
    id: UUID,
    createdAt: Instant,
) : BaseJpaEntity(id, createdAt) {
    fun toDomain() =
        Member(
            id = id,
            createdAt = createdAt,
        )

    companion object {
        fun from(member: Member) =
            MemberJpaEntity(
                id = member.id,
                createdAt = member.createdAt,
            )
    }
}
