package org.book.app.study.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
data class TokenResponse(
    val token: String,
    @JsonProperty("refresh-token")
    val refreshToken: String
)