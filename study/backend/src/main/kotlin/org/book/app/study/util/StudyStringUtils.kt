package org.book.app.study.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.book.app.common.exception.BusinessException
import org.book.app.study.util.StudyJacksonUtils.createCsvMapper
import org.book.app.study.util.StudyJacksonUtils.createCsvSchema
import org.book.app.study.util.StudyJacksonUtils.objectToListByCsvMapper
import java.nio.file.FileSystems

/**
 * 文字列を扱うutilクラス
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
object StudyStringUtils {
    /**
     * csvの区切り文字
     */
    const val SEPARATOR_BY_CSV: Char = ','

    /**
     * tsvの区切り文字
     */
    const val SEPARATOR_BY_TSV: Char = '\t'

    /**
     * パスの区切り文字
     */
    val SEPARATOR_BY_PATH: String = FileSystems.getDefault().separator

    /**
     * キーの区切り文字
     */
    const val SEPARATOR_BY_KEY: String = "."

    /**
     * スネークケースの区切り文字
     */
    const val SEPARATOR_BY_SNAKE: String = "_"

    /**
     * 区切り文字で結合
     *
     * @param separator 区切り文字1文字
     * @param base      ベース
     * @param add       追加す対象
     * @return 結合したもの
     */
    fun joinBase(separator: String, base: String?, add: String?): String {
        // 'addがnullまたは空の場合、'base'のみを返却（nullの場合は空文字列として扱う）
        val safeAdd = add.orEmpty()
        if (safeAdd.isEmpty()) {
            return base.orEmpty()
        }

        // 'base'がnullまたは空の場合、'add'のみを返却
        val safeBase = base.orEmpty()
        if (safeBase.isEmpty()) {
            return safeAdd
        }

        // 'base'の末尾と'add'の先頭にセパレータが重複しているかをチェック
        val baseEndsWithSeparator = safeBase.endsWith(separator)
        val addStartsWithSeparator = safeAdd.startsWith(separator)

        return when {
            // 両方にセパレータがある場合、'base'のセパレータを削除して連結
            baseEndsWithSeparator && addStartsWithSeparator -> safeBase.dropLast(separator.length) + safeAdd
            // どちらか一方のみセパレータがある、または両方にセパレータがない場合、そのまま連結
            baseEndsWithSeparator || addStartsWithSeparator -> safeBase + safeAdd
            // それ以外の場合、セパレータを挟んで連結
            else -> safeBase + separator + safeAdd
        }
    }

    /**
     * 区切り文字で結合
     *
     * @param separator 区切り文字1文字
     * @param base      ベース
     * @param adds      追加する対象のリスト
     * @return 結合したもの
     */
    fun joinBases(separator: String, base: String, vararg adds: String): String {
        return adds.fold(base) { acc, add -> joinBase(separator, acc, add) }
    }

    /**
     * ベースのパスと追加のパス(複数可)を結合し返却する
     *
     * @param basePath ベースとなるパス
     * @param addPaths 追加したいパス
     * @return String 結合したパス
     */
    fun pathJoin(basePath: String, vararg addPaths: String): String {
        return joinBases(SEPARATOR_BY_PATH, basePath, *addPaths)
    }

    /**
     * ベースのキーと追加のキー(複数可)を結合し返却する
     *
     * @param baseKey ベースとなるキー
     * @param addkeys 追加したいキー
     * @return String 結合したキー
     */
    fun keyJoin(baseKey: String, vararg addkeys: String): String {
        return joinBases(SEPARATOR_BY_KEY, baseKey, *addkeys)
    }

    /**
     * ベースの文字列と追加の文字列(複数可)を結合し返却する
     *
     * @param baseStr ベースとなる文字列
     * @param addStrs 追加したい文字列
     * @return String 結合した文字列
     */
    fun snakeJoin(baseStr: String, vararg addStrs: String): String {
        return joinBases(SEPARATOR_BY_SNAKE, baseStr, *addStrs)
    }

    /**
     * 置換対象が文字列の先頭にあった場合のみ置換、それ以外は置換せずに返却
     *
     * @param str         置換対象
     * @param target      置換文字列
     * @param replaceMent 置換語文字列
     * @return String 置換結果
     */
    fun replaceFirstOneLeft(str: String, target: String, replaceMent: String?): String {
        return if (str.indexOf(target) == 0) str.replaceFirst(target.toRegex(), replaceMent!!) else str
    }

    /**
     * 置換対象が文字列の最後尾にあった場合のみ置換、それ以外は置換せずに返却
     *
     * @param str         置換対象
     * @param target      置換文字列
     * @param replaceMent 置換語文字列
     * @return String 置換結果
     */
    @JvmStatic
    fun replaceFirstOneRight(str: String, target: String, replaceMent: String?): String {
        // 置換対象が文字列の最後尾ににあった場合のみ置換、それ以外は置換せずに返却
        val lastIndex = str.lastIndexOf(target)
        if (lastIndex != -1 && lastIndex + target.length == str.length) {
            return replaceLast(str, target, replaceMent)
        }

        return str
    }

    /**
     * 最後尾からマッチしたものを一つだけ置換
     *
     * @param str         置換対象
     * @param regex       置換文字列(正規表現可)
     * @param replacement 置換語文字列
     * @return 置換結果
     */
    fun replaceLast(str: String, regex: String?, replacement: String?): String {
        return str.replaceFirst(
            StringBuffer().append(regex).append("(?!.*?").append(regex).append(")").toString().toRegex(), replacement!!
        )
    }

    /**
     * オブジェクトをjsonに変換
     *
     * @param target 変換対象
     * @return String json文字列
     */
    fun objectToJsonStr(target: Any?): String {
        val mapper = ObjectMapper()
        return try {
            mapper.writeValueAsString(target)
        } catch (e: JsonProcessingException) {
            throw BusinessException("1.01.01.1011", e.message ?: "Unknown error")
        }
    }

    /**
     * JSON文字列からオブジェクトに変換
     *
     * @param json 変換対象のJSON文字列
     * @return T 変換後のオブジェクト
     */
    inline fun <reified T : Any> jsonToObject(json: String): T {
        val mapper = jacksonObjectMapper()

        return mapper.readValue(json, T::class.java)
    }

    /**
     * オブジェクトをcsv形式の文字列に変換
     *
     * @param target   変換対象
     * @param pojoType カラム情報が記載されているクラス
     * @param isHeader ヘッダーをつけるか
     * @return String csv文字列
     */
    fun objectToCsvStr(target: Any?, pojoType: Class<*>, isHeader: Boolean): String {
        return objectToStrByCsvMapper(target, pojoType, SEPARATOR_BY_CSV, isHeader, true)
    }

    /**
     * オブジェクトをtsv形式の文字列に変換
     *
     * @param target   変換対象
     * @param pojoType カラム情報が記載されているクラス
     * @param isHeader ヘッダーをつけるか
     * @return String tsv文字列
     */
    fun objectToTsvStr(target: Any?, pojoType: Class<*>, isHeader: Boolean): String {
        return objectToStrByCsvMapper(target, pojoType, SEPARATOR_BY_TSV, isHeader, false)
    }

    /**
     * オブジェクトを区切り文字で区切った文字列に変換
     *
     * @param target   変換対象
     * @param pojoType カラム情報が記載されているクラス
     * @param sep      区切り文字
     * @param isHeader ヘッダーをつけるか
     * @param isQuote  文字列にダブルクオートをつけるか
     * @return String 文字列
     */
    fun objectToStrByCsvMapper(
        target: Any?, pojoType: Class<*>, sep: Char,
        isHeader: Boolean, isQuote: Boolean
    ): String {
        val mapper = createCsvMapper(isQuote)
        val schema = createCsvSchema(mapper, pojoType, sep, isHeader)
        return try {
            mapper.writer(schema).writeValueAsString(target)
        } catch (e: JsonProcessingException) {
            throw BusinessException("1.01.01.1011", e.message ?: "Unknown error")
        }
    }

    /**
     * 文字列からList形式に変換
     *
     * @param str      文字列
     * @param pojoType カラム情報が記載されているクラス
     * @param isHeader ヘッダーをつけるか
     * @return List
     */
    fun <T> csvStrToList(
        str: String, pojoType: Class<T>,
        isHeader: Boolean
    ): List<T> {
        return strToListByCsvMapper(
            str, pojoType,
            SEPARATOR_BY_CSV, isHeader, true
        )
    }

    /**
     * tsvファイル文字列からList形式に変換
     *
     * @param str      文字列
     * @param pojoType カラム情報が記載されているクラス
     * @param isHeader ヘッダーをつけるか
     * @return List
     */
    fun <T> tsvStrToList(
        str: String, pojoType: Class<T>,
        isHeader: Boolean
    ): List<T> {
        return strToListByCsvMapper(
            str, pojoType,
            SEPARATOR_BY_TSV, isHeader, false
        )
    }

    /**
     * 文字列からList形式に変換
     *
     * @param str      文字列
     * @param pojoType カラム情報が記載されているクラス
     * @param sep      区切り文字
     * @param isHeader ヘッダーをつけるか
     * @param isQuote  文字列にダブルクオートをつけるか
     * @return List
     */
    fun <T> strToListByCsvMapper(
        str: String,
        pojoType: Class<T>, sep: Char, isHeader: Boolean, isQuote: Boolean
    ): List<T> {
        return objectToListByCsvMapper(
            str, pojoType, sep, isHeader,
            isQuote
        )
    }

    /**
     * 文字列の最初の文字を大文字にする
     *
     * @param value 変換対象
     * @return 変換後文字列
     */
    fun upperCaseFirst(value: String): String =
        value.takeIf { it.isNotEmpty() }?.let { it[0].uppercase() + it.substring(1) } ?: value


    /**
     * 文字列の最初の文字を小文字にする
     *
     * @param value 変換対象
     * @return 変換後文字列
     */
    fun lowerCaseFirst(value: String): String =
        value.takeIf { it.isNotEmpty() }?.let { it[0].lowercase() + it.substring(1) } ?: value


    /**
     * 最初の文字を小文字にしたクラス名を取得する
     *
     * @param clazz クラス名取得対象クラス
     * @return 最初の文字が小文字のクラス名
     */
    fun getlowerCaseFirstClassName(clazz: Class<*>): String {
        return lowerCaseFirst(clazz.simpleName)
    }

    /**
     * 文字列の最初の文字が引数2と同じか判定
     *
     * @param target 判定対象
     * @param first  判定文字
     * @return 判定結果
     */
    fun isFirstChar(target: String, first: Char): Boolean {
        return target.toCharArray()[0] == first
    }

    /**
     * 文字列から指定の文字列を削除
     *
     * @param str     置換対象
     * @param targets 削除対象文字列のリスト
     * @return 削除後文字列
     */
    fun delete(str: String, targets: List<String>): String {
        return delete(str, *targets.toTypedArray())
    }

    /**
     * 文字列から指定の文字列を削除
     *
     * @param str     置換対象
     * @param targets 削除対象文字列
     * @return 削除後文字列
     */
    fun delete(str: String, vararg targets: String): String {
        // すべての対象文字列を正規表現のパターンに変換し、一度に置換
        val pattern = targets.joinToString("|") { Regex.escape(it) }
        return str.replace(Regex(pattern), "")
    }
}
