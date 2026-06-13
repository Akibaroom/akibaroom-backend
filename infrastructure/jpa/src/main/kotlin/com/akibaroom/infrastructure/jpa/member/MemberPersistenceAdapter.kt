package com.akibaroom.infrastructure.jpa.member

import com.akibaroom.domain.member.domain.Member
import com.akibaroom.domain.member.domain.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun findById(id: UUID): Member? = memberJpaRepository.findByIdOrNull(id)?.toDomain()
}
