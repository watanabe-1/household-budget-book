package org.book.app.common.exception

/**
 * ビジネス例外用クラス
 */
class BusinessException(
    val messageKey: String,
    vararg val args: Any?
) : RuntimeException()