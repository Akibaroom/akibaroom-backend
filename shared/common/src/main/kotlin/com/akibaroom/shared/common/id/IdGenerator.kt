package com.akibaroom.shared.common.id

import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID

object IdGenerator {
    fun next(): UUID = UuidCreator.getTimeOrderedEpoch()
}
