package org.book.app.study.util

import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import org.book.app.common.exception.BusinessException
import java.io.IOException
import java.io.InputStream
import java.io.Reader

/**
 * jacksonを扱うutilクラス
 */
object StudyJacksonUtils {
    /**
     * csvmapperの作成
     *
     * @param isQuote 文字列にダブルクオートをつけるか
     * @return CsvMapper
     */
    fun createCsvMapper(isQuote: Boolean): CsvMapper {
        val mapper = CsvMapper()
        mapper.findAndRegisterModules()
        mapper.setDateFormat(StdDateFormat())

        // ダブルクオートあり
        if (isQuote) {
            mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true)
        }

        return mapper
    }

    /**
     * csvmapperの作成
     *
     * @param mapper   CsvMapper
     * @param pojoType カラム情報が記載されているクラス
     * @param sep      区切り文字
     * @param isHeader ヘッダーをつけるか
     * @return CsvSchema
     */
    fun <T> createCsvSchema(
        mapper: CsvMapper, pojoType: Class<T>?, sep: Char,
        isHeader: Boolean
    ): CsvSchema {
        var schema = mapper.schemaFor(pojoType).withColumnSeparator(sep)

        // ヘッダーあり
        if (isHeader) {
            schema = schema.withHeader()
        }

        return schema
    }

    /**
     * CSVデータを読み取り、指定されたPOJO型のリストに変換する
     *
     * @param T マッピングするPOJOの型
     * @param obj 変換対象のInputStream、Reader、またはString
     * @param mapper JacksonのCsvMapper
     * @param schema CsvSchema、列定義を含む
     * @param pojoType カラム情報が記載されているクラス
     * @return 変換されたオブジェクトのリスト
     * @throws BusinessException 変換中にエラーが発生した場合
     */
    fun <T> readValues(
        obj: Any?, mapper: CsvMapper, schema: CsvSchema?,
        pojoType: Class<T>?
    ): List<T> = try {
        val objectReader = mapper.readerFor(pojoType).with(schema)
        val objectMappingIterator = when (obj) {
            is InputStream -> objectReader.readValues<T>(obj)
            is Reader -> objectReader.readValues<T>(obj)
            is String -> objectReader.readValues<T>(obj)
            else -> throw BusinessException("1.01.01.1009", "Unsupported type for readValues")
        }
        objectMappingIterator.readAll()
    } catch (e: IOException) {
        throw BusinessException("1.01.01.1009", e.message)
    }

    /**
     * objctからList形式に変換
     *
     * @param obj      変換対象(InputStream、Reader、String型のみ)
     * @param pojoType カラム情報が記載されているクラス
     * @param sep      区切り文字
     * @param isHeader ヘッダーをつけるか
     * @param isQuote  文字列にダブルクオートをつけるか
     * @return List
     */
    fun <T> objectToListByCsvMapper(
        obj: Any?,
        pojoType: Class<T>?, sep: Char, isHeader: Boolean, isQuote: Boolean
    ): List<T> {
        val mapper = createCsvMapper(isQuote)
        val schema = createCsvSchema(mapper, pojoType, sep, isHeader)

        return readValues(obj, mapper, schema, pojoType)
    }
}
