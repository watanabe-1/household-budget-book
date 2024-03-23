package org.book.app.study.util

import common.dto.data.TestFileRow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StudyStringUtilsTest {

    data class TestPerson(val name: String, val age: Int)

    data class TestStringPojo(val property: String)

    @Test
    fun `test pathJoin with two parameters`() {
        assertEquals(
            "base${StudyStringUtils.SEPARATOR_BY_PATH}path",
            StudyStringUtils.pathJoin("base", "path")
        )
        assertEquals(
            "base${StudyStringUtils.SEPARATOR_BY_PATH}path",
            StudyStringUtils.pathJoin("base" + StudyStringUtils.SEPARATOR_BY_PATH, "path")
        )
        assertEquals(
            "base${StudyStringUtils.SEPARATOR_BY_PATH}path",
            StudyStringUtils.pathJoin("base", StudyStringUtils.SEPARATOR_BY_PATH + "path")
        )
    }

    @Test
    fun `test pathJoin with multiple parameters`() {
        assertEquals(
            "base${StudyStringUtils.SEPARATOR_BY_PATH}path${StudyStringUtils.SEPARATOR_BY_PATH}to${StudyStringUtils.SEPARATOR_BY_PATH}resource",
            StudyStringUtils.pathJoin("base", "path", "to", "resource")
        )
    }

    @Test
    fun `test replaceFirstOneLeft`() {
        assertEquals(
            "replacementString",
            StudyStringUtils.replaceFirstOneLeft("targetString", "target", "replacement")
        )
        assertEquals(
            "targetString",
            StudyStringUtils.replaceFirstOneLeft("targetString", "nonexistent", "replacement")
        )
    }

    @Test
    fun `test replaceFirstOneRight`() {
        assertEquals(
            "Stringreplacement",
            StudyStringUtils.replaceFirstOneRight("Stringtarget", "target", "replacement")
        )
        assertEquals(
            "Stringtarget",
            StudyStringUtils.replaceFirstOneRight("Stringtarget", "nonexistent", "replacement")
        )
    }

    @Test
    fun `test replaceLast`() {
        assertEquals(
            "firstTargetSecondReplacement",
            StudyStringUtils.replaceLast("firstTargetSecondTarget", "Target", "Replacement")
        )
    }

    @Test
    fun `test objectToJsonStr`() {
        val json = StudyStringUtils.objectToJsonStr(TestStringPojo("value"))
        assertEquals("{\"property\":\"value\"}", json)
    }

    @Test
    fun `jsonToObject should correctly deserialize a JSON string to a Person object`() {
        // JSON文字列
        val json = """{"name": "John Doe", "age": 30}"""

        // jsonToObject関数を呼び出して、JSON文字列からPersonオブジェクトに変換
        val result: TestPerson = StudyStringUtils.jsonToObject(json)

        // 期待されるPersonオブジェクト
        val expected = TestPerson("John Doe", 30)

        // 結果と期待値を比較
        assertEquals(expected, result)
    }

    @Test
    fun `test upperCaseFirst`() {
        assertEquals("Test", StudyStringUtils.upperCaseFirst("test"))
    }

    @Test
    fun `test lowerCaseFirst`() {
        assertEquals("test", StudyStringUtils.lowerCaseFirst("Test"))
    }

    @Test
    fun `test getLowerCaseFirstClassName`() {
        assertEquals(
            "testStringPojo", StudyStringUtils.getlowerCaseFirstClassName(TestStringPojo::class.java)
        )
    }

    @Test
    fun `test isFirstChar`() {
        assertTrue(StudyStringUtils.isFirstChar("apple", 'a'))
        assertFalse(StudyStringUtils.isFirstChar("banana", 'a'))
    }

    @Test
    fun `test delete`() {
        assertEquals("HelloWorld", StudyStringUtils.delete("Hello World", " "))
        assertEquals("HelloWorld", StudyStringUtils.delete("Hello World", mutableListOf(" ")))
    }

    @Test
    fun `test objectToCsvStr`() {
        val pojo = TestStringPojo("value")
        val csv = StudyStringUtils.objectToCsvStr(pojo, TestStringPojo::class.java, true)
        assertNotNull(csv)
    }

    @Test
    fun `test objectToTsvStr`() {
        val pojo = TestStringPojo("value")
        val tsv = StudyStringUtils.objectToTsvStr(pojo, TestStringPojo::class.java, true)
        assertNotNull(tsv)
    }

    @Test
    fun `test objectToStrByCsvMapper`() {
        val pojo = TestStringPojo("value")
        val str = StudyStringUtils.objectToStrByCsvMapper(pojo, TestStringPojo::class.java, ',', isHeader = true, true)
        assertNotNull(str)
    }

    @Test
    fun `test csvStrToList`() {
        val csv = "field\nvalue"
        val result: List<TestFileRow> = StudyStringUtils.csvStrToList(csv, TestFileRow::class.java, true)
        assertNotNull(result)
    }

    @Test
    fun `test tsvStrToList`() {
        val tsv = "field\tvalue"
        val result: List<TestFileRow> = StudyStringUtils.tsvStrToList(tsv, TestFileRow::class.java, true)
        assertNotNull(result)
    }

    @Test
    fun `test strToListByCsvMapper`() {
        val str = "field,value"
        val result: List<TestFileRow> =
            StudyStringUtils.strToListByCsvMapper(str, TestFileRow::class.java, ',', isHeader = true, true)
        assertNotNull(result)
    }
}
