package org.book.app.study.model.response

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
data class TokenResponse(
    val token: String,
    val refreshToken: String,
    val expiresAt: Long,
    val user: UserResponse
)