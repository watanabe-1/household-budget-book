package org.book.app.study.enums.type

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * アカウント権限
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class AccountType(override val code: String, override val displayName: String, val baseRole: String) : Type {
    SYSTEM("01", "システム管理者", "SYSTEM"),
    ADMIN("02", "管理者", "ADMIN"),
    USER("03", "一般ユーザ", "USER"),
    REFRESH("04", "リフレッシュ", "REFRESH");

    val role: String get() = "ROLE_$baseRole"
    val jwtRole: String get() = "SCOPE_$role"

    companion object {
        fun codeOf(code: String) = Type.codeOf<AccountType>(code)
        fun nameOf(displayName: String) = Type.nameOf<AccountType>(displayName)
    }
}
