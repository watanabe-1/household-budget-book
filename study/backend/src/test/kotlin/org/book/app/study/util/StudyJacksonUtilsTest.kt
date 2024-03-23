package org.book.app.study.util

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import common.dto.data.TestFileRow
import org.book.app.common.exception.BusinessException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.nio.charset.StandardCharsets

internal class StudyJacksonUtilsTest {
    private val csvData = "column1,column2\nvalue1,value2"
    private val pojoType = TestFileRow::class.java

    @Test
    fun `createCsvMapper handles quotes appropriately`() {
        // テストデータの準備
        val testData = arrayOf("test1", "test2")

        // isQuote が true の場合
        val mapperWithQuote = StudyJacksonUtils.createCsvMapper(true)
        val resultWithQuote = writeCsvData(mapperWithQuote, testData)
        assertTrue { "\"test1\"" in resultWithQuote }

        // isQuote が false の場合
        val mapperWithoutQuote = StudyJacksonUtils.createCsvMapper(false)
        val resultWithoutQuote = writeCsvData(mapperWithoutQuote, testData)
        assertFalse { "\"test1\"" in resultWithoutQuote }
    }

    private fun writeCsvData(mapper: CsvMapper, data: Array<String>): String {
        val schema = CsvSchema.builder().addColumn("column").build()
        return StringWriter().apply {
            mapper.writer(schema).writeValues(this).writeAll(data.toList())
        }.toString()
    }

    @Test
    fun `createCsvSchema creates schema with or without header`() {
        val mapper = CsvMapper().apply { registerModule(JavaTimeModule()) }

        // 区切り文字とヘッダーありの場合
        val schemaWithHeader = StudyJacksonUtils.createCsvSchema(mapper, pojoType, ',', true)
        assertTrue(schemaWithHeader.usesHeader())

        // 区切り文字とヘッダーなしの場合
        val schemaWithoutHeader = StudyJacksonUtils.createCsvSchema(mapper, pojoType, ',', false)
        assertFalse(schemaWithoutHeader.usesHeader())

        // 区切り文字の確認
        assertEquals(',', schemaWithHeader.columnSeparator)
    }

    @Test
    fun `readValues correctly parses CSV data`() {
        val mapper = CsvMapper().apply { registerModule(JavaTimeModule()) }
        val schema = CsvSchema.emptySchema().withHeader()

        // StringReaderを使用
        testReadValues(mapper, schema, csvData.reader(), pojoType)

        // InputStreamを使用
        testReadValues(mapper, schema, ByteArrayInputStream(csvData.toByteArray(StandardCharsets.UTF_8)), pojoType)

        // Stringを使用
        testReadValues(mapper, schema, csvData, pojoType)
    }

    private fun <T> testReadValues(mapper: CsvMapper, schema: CsvSchema, source: Any, pojoType: Class<T>) {
        val result = StudyJacksonUtils.readValues(source, mapper, schema, pojoType)
        @Suppress("UNCHECKED_CAST")
        validateResult(result as List<TestFileRow>)
    }

    private fun validateResult(result: List<TestFileRow>) {
        assertNotNull(result)
        assertEquals(1, result.size)
        with(result.first()) {
            assertEquals("value1", column1)
            assertEquals("value2", column2)
        }
    }

    @Test
    fun `readValues throws BusinessException for invalid input`() {
        val mapper = CsvMapper().apply { registerModule(JavaTimeModule()) }
        val schema = CsvSchema.emptySchema().withHeader()

        // 不正な入力（サポートされていないオブジェクトタイプ）
        assertThrows<BusinessException> {
            StudyJacksonUtils.readValues(Any(), mapper, schema, pojoType)
        }
    }
}
