package org.book.app.study.util

import common.dto.data.TestFileRow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.MockitoAnnotations
import org.springframework.core.io.ClassPathResource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files

internal class StudyFileUtilsTest {
    companion object {
        private const val RESOURCE_DIR = "org/book/app/study/util/studyFileUtilTest/"
    }

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @ParameterizedTest
    @CsvSource("testfile,txt,testfile.txt", "testfile.txt,txt,testfile.txt", "'',txt,.txt")
    fun `addExtension returns expected filename`(fileName: String, extension: String?, expected: String?) {
        assertEquals(expected, StudyFileUtils.addExtension(fileName, extension))
    }

    @Test
    fun `csvFileToList with header correctly parses CSV file`() {
        val resource = ClassPathResource("$RESOURCE_DIR/csvRowWithHeader.csv")
        val resultList = StudyFileUtils.csvFileToList<TestFileRow>(resource, true)

        assertListContent(resultList)
    }

    @Test
    fun `csvFileToList with no header correctly parses CSV file`() {
        val resource = ClassPathResource("$RESOURCE_DIR/csvRow.csv")
        val resultList = StudyFileUtils.csvFileToList<TestFileRow>(resource, false)

        assertListContent(resultList)
    }

    @Test
    fun `tsvFileToList with header correctly parses TSV file`() {
        val resource = ClassPathResource("$RESOURCE_DIR/tsvRowWithHeader.tsv")
        val resultList = StudyFileUtils.tsvFileToList<TestFileRow>(resource, true)

        assertListContent(resultList)
    }

    @Test
    fun `tsvFileToList with no header correctly parses TSV file`() {
        val resource = ClassPathResource("$RESOURCE_DIR/tsvRow.tsv")
        val resultList = StudyFileUtils.tsvFileToList<TestFileRow>(resource, false)

        assertListContent(resultList)
    }

    @Test
    fun `fileToListByCsvMapper with header and quote correctly parses CSV file`() {
        val resource = ClassPathResource("$RESOURCE_DIR/csvRowWithHeader.csv")
        val resultList = StudyFileUtils.fileToListByCsvMapper(
            resource, UTF_8, TestFileRow::class.java, ',', true, true
        )

        assertListContent(resultList)
    }

    @Test
    fun `fileToListByCsvMapper with no header and no quote correctly parses TSV file`() {
        val resource = ClassPathResource("$RESOURCE_DIR/tsvRow.tsv")
        val resultList = StudyFileUtils.fileToListByCsvMapper(
            resource, UTF_8, TestFileRow::class.java, '\t', false, false
        )

        assertListContent(resultList)
    }

    @Test
    fun `detectFileEncoding detects correct encoding for UTF-8 file`() {
        val utf8File = ClassPathResource("$RESOURCE_DIR/charset_test_utf8.txt")
        val encoding = StudyFileUtils.detectFileEncoding(utf8File)

        assertEquals(UTF_8, encoding)
    }

    @Test
    fun `detectFileEncoding detects correct encoding for Shift_JIS file`() {
        val sjisFile = ClassPathResource("$RESOURCE_DIR/charset_test_shiftjis.txt")
        val encoding = StudyFileUtils.detectFileEncoding(sjisFile)

        assertEquals(Charset.forName("Shift_JIS"), encoding)
    }

    @Test
    fun `readClassPathFile reads UTF-8 encoded file content correctly`() {
        val expectedContent = "こんにちは"
        val fileContent = StudyFileUtils.readClassPathFile("$RESOURCE_DIR/charset_test_utf8.txt", UTF_8)

        assertEquals(expectedContent, fileContent)
    }

    @Test
    fun `deleteFile removes the file successfully`() {
        // Assuming StudyTestUtil.testTempDirPath.toPath() returns a valid Path
        // and that creating a temp file is desired for this test.
        val tempFile = Files.createTempFile("tempFile", ".txt").toFile().apply {
            writeText("This is a temporary file.")
        }

        assertTrue(tempFile.exists())

        StudyFileUtils.deleteFile(tempFile)

        assertFalse(tempFile.exists())
    }

    private fun assertListContent(resultList: List<TestFileRow>) {
        assertNotNull(resultList)
        assertFalse(resultList.isEmpty())
        assertEquals(1, resultList.size)
        resultList[0].apply {
            assertEquals("value1", column1)
            assertEquals("value2", column2)
            assertEquals("value3", column3)
        }
    }


}
