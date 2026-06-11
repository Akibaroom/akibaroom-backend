package com.akibaroom.shared.common.exception

interface ErrorCode {
    val code: String
    val httpStatus: Int
    val message: String
}
