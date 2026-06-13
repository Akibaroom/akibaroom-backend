package com.akibaroom.config

import com.akibaroom.shared.common.exception.BusinessException
import com.akibaroom.shared.common.exception.CommonErrorCode
import com.akibaroom.shared.common.exception.ErrorDetail
import com.akibaroom.shared.common.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BusinessException::class)
    fun handleBusiness(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorCode.httpStatus)
            .body(ErrorResponse(code = e.errorCode.code, message = e.errorCode.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors =
            e.bindingResult.fieldErrors.map { fieldError ->
                ErrorDetail(field = fieldError.field, reason = fieldError.defaultMessage ?: "")
            }
        return ResponseEntity
            .status(CommonErrorCode.COMMON_001.httpStatus)
            .body(
                ErrorResponse(
                    code = CommonErrorCode.COMMON_001.code,
                    message = CommonErrorCode.COMMON_001.message,
                    errors = errors,
                ),
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(CommonErrorCode.COMMON_002.httpStatus)
            .body(ErrorResponse(code = CommonErrorCode.COMMON_002.code, message = CommonErrorCode.COMMON_002.message))

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(CommonErrorCode.COMMON_003.httpStatus)
            .body(ErrorResponse(code = CommonErrorCode.COMMON_003.code, message = CommonErrorCode.COMMON_003.message))

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleMediaTypeNotSupported(e: HttpMediaTypeNotSupportedException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(CommonErrorCode.COMMON_004.httpStatus)
            .body(ErrorResponse(code = CommonErrorCode.COMMON_004.code, message = CommonErrorCode.COMMON_004.message))

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("예상치 못한 오류 발생", e)
        return ResponseEntity
            .status(CommonErrorCode.COMMON_999.httpStatus)
            .body(ErrorResponse(code = CommonErrorCode.COMMON_999.code, message = CommonErrorCode.COMMON_999.message))
    }
}
