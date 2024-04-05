package org.book.app.study.model.response

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
data class UserResponse(
    val name: String,
    val role: String
)