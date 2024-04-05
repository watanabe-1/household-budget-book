package org.book.app.study.model.dto

data class TokenDto(
    val token: String,
    val expiresAt: Long,
    val name: String,
    val role: String
)