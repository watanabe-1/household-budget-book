package org.book.app.study.enums.type

/**
 * タイプの定義
 */
internal interface Type {
    /**
     * コードを取得します.
     *
     * @return コード
     */
    val code: String

    /**
     * 名称を取得します.
     *
     * @return 名称
     */
    val displayName: String

    companion object {

        /**
         * タイプ用に拡張したEnumのcode値から取得した拡張Enumを生成する
         *
         * @param code
         * @return
         */
        inline fun <reified E> codeOf(code: String): E where E : Enum<E>, E : Type {
            return enumValues<E>().firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("enum class has not code : $code")
        }

        /**
         * タイプ用に拡張したEnumのname値から取得した拡張Enumを生成する
         *
         * @param E
         * @param displayName
         * @return
         */
        inline fun <reified E> nameOf(displayName: String): E where E : Enum<E>, E : Type {
            return enumValues<E>().firstOrNull { it.displayName == displayName }
                ?: throw IllegalArgumentException("enum class has not name : $displayName")

        }
    }
}
