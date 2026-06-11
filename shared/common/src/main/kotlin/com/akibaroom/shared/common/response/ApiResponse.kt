package com.akibaroom.shared.common.response

import com.akibaroom.shared.common.exception.ErrorDetail

data class SuccessResponse<T>(val data: T)

data class ErrorResponse(val code: String, val message: String, val errors: List<ErrorDetail>? = null)
