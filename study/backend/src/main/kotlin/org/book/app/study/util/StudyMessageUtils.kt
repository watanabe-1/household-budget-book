package org.book.app.study.util

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.book.app.study.util.StudyStringUtils.replaceFirstOneRight
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

/**
 * メッセージ用utilクラス
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
object StudyMessageUtils {
    private const val SEPARATOR_BY_BINDERROR_FIELD = "."

    /**
     * バインドエラー時の配列用フィールド名作成
     *
     * @param arrayFieldName 配列フィールド名
     * @param index インデックス
     * @param fieldNames fieldNames 対象フィールド名
     * @return
     */
    fun getArrayFieldName(arrayFieldName: String?, index: Int, vararg fieldNames: String?): String {
        val ret = StringBuffer().append(arrayFieldName)
            .append("[")
            .append(index)
            .append("]")
            .append(SEPARATOR_BY_BINDERROR_FIELD)
            .append(getFieldName(*fieldNames))
            .toString()

        return ret
    }

    /**
     * バインドエラー時のフィールド名作成
     *
     * @param fieldNames 対象フィールド名
     * @return フィールド名
     */
    fun getFieldName(vararg fieldNames: String?): String {
        val sb = StringBuilder()
        for (fieldName in fieldNames) {
            sb.append(fieldName).append(SEPARATOR_BY_BINDERROR_FIELD)
        }
        return replaceFirstOneRight(sb.toString(), SEPARATOR_BY_BINDERROR_FIELD, "")
    }

    /**
     * エラーを結果に追加
     *
     * @param result エラー結果
     * @param field 対象フィールド名
     * @param code エラーコード
     */
    fun addError(result: BindingResult, field: String, code: String?) {
        addError(result, field, code, "")
    }

    /**
     * エラーを結果に追加
     *
     * @param result エラー結果
     * @param field 対象フィールド名
     * @param code エラーコード
     * @param arguments エラー文字列内で参照する値
     */
    fun addError(
        result: BindingResult, field: String, code: String?,
        vararg arguments: Any?
    ) {
        addErrorOnDefMsg(result, field, code, code, *arguments)
    }

    /**
     * エラーを結果に追加(デフォルトメッセージ有)
     *
     * @param result エラー結果
     * @param field 対象フィールド名
     * @param code エラーコード
     * @param defaultMessage デフォルトメッセージ
     * @param arguments エラー文字列内で参照する値
     */
    fun addErrorOnDefMsg(
        result: BindingResult, field: String,
        code: String?, defaultMessage: String?, vararg arguments: Any?
    ) {
        result.addError(
            FieldError(
                result.objectName, field, null, false, arrayOf(code), arguments,
                defaultMessage
            )
        )
    }
}
