package org.book.app.study.util

import org.book.app.study.util.StudyDateUtils.getFirstDayOfMonth
import org.book.app.study.util.StudyDateUtils.getFirstDayOfYear
import org.book.app.study.util.StudyDateUtils.getLastDayOfYear
import org.book.app.study.util.StudyDateUtils.getNextMonth
import org.book.app.study.util.StudyDateUtils.getOneYearAgoMonth
import org.book.app.study.util.StudyDateUtils.getPreviousMonth
import org.book.app.study.util.StudyDateUtils.getYearMonth
import org.book.app.study.util.StudyDateUtils.localDateTimeToStr
import org.book.app.study.util.StudyDateUtils.strToLocalDate
import org.book.app.study.util.StudyDateUtils.strToLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class StudyDateUtilsTest {
    @Test
    fun `getNextMonth with LocalDate returns first day of next month`() {
        val date = LocalDate.of(2023, 3, 15) // 2023年3月15日
        val expected = LocalDate.of(2023, 4, 1).atStartOfDay()  //2023年4月1日
        val actual = getNextMonth(date)
        assertEquals(expected, actual, "1ヶ月後の日付が正しく計算されていません")
    }

    @Test
    fun `getNextMonth with LocalDateTime preserves time and moves to next month`() {
        val date = LocalDateTime.of(2023, 3, 15, 1, 0, 0) // 2023年3月15日 01:00
        val expected = LocalDateTime.of(2023, 4, 1, 1, 0) // 2023年4月1日 01:00
        val actual = getNextMonth(date)
        assertEquals(expected, actual, "1ヶ月後の日付が正しく計算されていません")
    }

    @Test
    fun `getPreviousMonth with LocalDate returns first day of previous month`() {
        val date = LocalDate.of(2023, 3, 15) // 2023年3月15日
        val expected = LocalDate.of(2023, 2, 1).atStartOfDay() // 2023年2月1日
        val actual = getPreviousMonth(date)
        assertEquals(expected, actual, "1ヶ月前の日付が正しく計算されていません")
    }

    @Test
    fun `getPreviousMonth with LocalDateTime preserves time and moves to previous month`() {
        val date = LocalDateTime.of(2023, 3, 15, 10, 30) // 2023年3月15日 10:30
        val expected = LocalDateTime.of(2023, 2, 1, 10, 30) // 2023年2月1日 10:30
        val actual = getPreviousMonth(date)
        assertEquals(expected, actual, "1ヶ月前の日付が正しく計算されていません")
    }

    @Test
    fun `getFirstDayOfMonth returns the first day of the month with time unchanged`() {
        val date = LocalDateTime.of(2023, 3, 15, 10, 30) // 2023年3月15日 10:30
        val expected = LocalDateTime.of(2023, 3, 1, 10, 30) // 2023年3月1日 10:30
        val actual = getFirstDayOfMonth(date)
        assertEquals(expected, actual, "月の最初の日が正しく取得されていません")
    }

    @Test
    fun `getFirstDayOfYear returns the first day of the year with time unchanged`() {
        val date = LocalDateTime.of(2023, 3, 15, 0, 0)
        val expected = LocalDateTime.of(2023, 1, 1, 0, 0)
        val actual = getFirstDayOfYear(date)
        assertEquals(expected, actual, "年の最初の日が正しく取得されていません")
    }

    @Test
    fun `getLastDayOfYear returns the last day of the year with time unchanged`() {
        val date = LocalDateTime.of(2023, 3, 15, 0, 0)
        val expected = LocalDateTime.of(2023, 12, 31, 0, 0)
        val actual = getLastDayOfYear(date)
        assertEquals(expected, actual, "年の最後の日が正しく取得されていません")
    }

    @Test
    fun `getOneYearAgoMonth returns the first day of the same month one year ago`() {
        val date = LocalDateTime.of(2023, 3, 15, 0, 0)
        val expected = LocalDateTime.of(2022, 3, 1, 0, 0)
        val actual = getOneYearAgoMonth(date)
        assertEquals(expected, actual, "1年前の月の最初の日が正しく取得されていません")
    }

    @Test
    fun `getBetweenYears returns list of years between two dates inclusive`() {
        val start = LocalDateTime.of(2020, 1, 1, 0, 0)
        val end = LocalDateTime.of(2023, 1, 1, 0, 0)
        val expected = listOf("2020", "2021", "2022", "2023")

        val actual = StudyDateUtils.getBetweenYears(start, end)

        assertEquals(expected, actual, "期間内の年リストが正しく取得されていません")
    }

    @Test
    fun `getYearOfMonthInPreviousWeeks calculates correct year for given month considering weeks back`() {
        val baseDate = LocalDate.of(2023, 5, 1) // Starting from May 1st, 2023
        val weeksBack = 20 // Going back 20 weeks
        val targetMonth = 12 // Targeting December of the previous year
        val expectedYear = 2022 // Expect December 2022 since 20 weeks back from May 2023 crosses into the previous year
        val resultYear = StudyDateUtils.getYearOfMonthInPreviousWeeks(baseDate, weeksBack, targetMonth)
        assertEquals(expectedYear, resultYear, "対象の月の年が正しく取得されていません")
    }

    @ParameterizedTest
    @CsvSource(
        "2023-3-15 00:00, 2023",
        "2024-1-01 00:00, 2024",
        "2022-12-31 00:00, 2022"
    )
    fun `getYearOfStr extracts year from LocalDateTime`(input: String, expectedYear: String) {
        val date = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm"))
        val result = StudyDateUtils.getYearOfStr(date)
        assertEquals(expectedYear, result, "日付から年を正しく取得できていません")
    }

    @ParameterizedTest
    @CsvSource(
        "2023-3-15 00:00, 3",
        "2024-1-01 00:00, 1",
        "2022-12-31 00:00, 12"
    )
    fun `getMonthOfStr extracts month from LocalDateTime`(input: String, expectedMonth: String) {
        val date = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm"))
        val result = StudyDateUtils.getMonthOfStr(date)
        assertEquals(expectedMonth, result, "日付から月を正しく取得できていません")
    }

    @ParameterizedTest
    @CsvSource(
        "2023-3-15 00:00, 15",
        "2024-1-01 00:00, 1",
        "2022-12-31 00:00, 31"
    )
    fun `getDayOfStr extracts day from LocalDateTime`(input: String, expectedDay: String) {
        val date = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm"))
        val result = StudyDateUtils.getDayOfStr(date)
        assertEquals(expectedDay, result, "日付から日を正しく取得できていません")
    }

    @Test
    fun `getYearMonth formats LocalDateTime as yyyy-MM`() {
        val date = LocalDateTime.of(2023, 1, 1, 0, 0)
        val expected = "2023/01"
        val actual = getYearMonth(date)
        assertEquals(expected, actual, "日付から年/月を正しくフォーマットできていません")
    }

    @Test
    fun `getYearMonthDay formats LocalDateTime as yyyy-MM-dd`() {
        val date = LocalDateTime.of(2024, 1, 1, 0, 0)
        assertEquals(
            "2024/01/01",
            StudyDateUtils.getYearMonthDay(date),
            "日付から年/月/日を正しくフォーマットできていません"
        )
    }

    @Test
    fun `strToLocalDate converts yyyy-MM-dd string to LocalDate`() {
        val dateStr = "2023/01/01"
        val expected = LocalDate.of(2023, 1, 1)
        val actual = strToLocalDate(dateStr, "yyyy/MM/dd")
        assertEquals(expected, actual, "文字列からLocalDateへの変換が正しく行われていません")
    }

    @Test
    fun `strToLocalDateTime converts yyyy-MM-dd HH-mm string to LocalDateTime`() {
        val dateTimeStr = "2023/01/01 00:00"
        val expected = LocalDateTime.of(2023, 1, 1, 0, 0)
        val actual = strToLocalDateTime(dateTimeStr, "yyyy/MM/dd HH:mm")
        assertEquals(expected, actual, "文字列からLocalDateTimeへの変換が正しく行われていません")
    }

    @Test
    fun `localDateTimeToStr formats LocalDateTime as specified pattern`() {
        val date = LocalDateTime.of(2023, 1, 1, 0, 0)
        val expected = "2023/01/01"
        val actual = localDateTimeToStr(date, "yyyy/MM/dd")
        assertEquals(expected, actual, "LocalDateTimeから文字列への変換が正しく行われていません")
    }
}
