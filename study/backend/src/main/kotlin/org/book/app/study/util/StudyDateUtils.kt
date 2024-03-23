package org.book.app.study.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/**
 * 日付けを扱うutilクラス
 */
object StudyDateUtils {
    /**
     * 開始日
     */
    const val START: String = "start"

    /**
     * 終了日
     */
    const val END: String = "end"

    /**
     * 日付けフォーマット 年
     */
    const val FMT_YEAR: String = "yyyy"

    /**
     * 日付けフォーマット 月
     */
    const val FMT_MONTH: String = "MM"

    /**
     * 日付けフォーマット 日
     */
    const val FMT_DAY: String = "dd"

    /**
     * 日付けフォーマット 時間
     */
    const val FMT_HOUR: String = "hh"

    /**
     * 日付けフォーマット 分
     */
    const val FMT_MINUTE: String = "mm"

    /**
     * 日付けフォーマット 年/月
     */
    const val FMT_YEAR_MONTH_SLASH: String = "yyyy/MM"

    /**
     * 日付けフォーマット 年/月/日
     */
    const val FMT_YEAR_MONTH_DAY_SLASH: String = "yyyy/MM/dd"

    /**
     * 日付けフォーマット 年/月/日
     */
    const val FMT_YEAR_ONEMONTH_ONEDAY_SLASH: String = "yyyy/M/d"

    /**
     * タイムゾーン 世界標準時
     */
    const val TIMEZONE_UTC: String = "UTC"

    /**
     * タイムゾーン 東京
     */
    const val TIMEZONE_ASIA_TOKYO: String = "Asia/Tokyo"

    /**
     * 1月加算してその月の最初の日を取得
     *
     * @param date 加算したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getNextMonth(date: LocalDate): LocalDateTime {
        return getNextMonth(localDateToLocalDateTime(date))
    }

    /**
     * 1月加算してその月の最初の日を取得
     *
     * @param date 加算したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getNextMonth(date: LocalDateTime): LocalDateTime {
        return date.with(TemporalAdjusters.firstDayOfNextMonth())
    }

    /**
     * 1ヶ月減算してその月の最初の日を取得
     *
     * @param date 減算したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getPreviousMonth(date: LocalDate): LocalDateTime {
        return getPreviousMonth(localDateToLocalDateTime(date))
    }

    /**
     * 1ヶ月減算してその月の最初の日を取得
     *
     * @param date 減算したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getPreviousMonth(date: LocalDateTime): LocalDateTime {
        return date.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth())
    }

    /**
     * その月の最初の日に変換して返却
     *
     * @param date 変更したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getFirstDayOfMonth(date: LocalDateTime): LocalDateTime {
        return date.with(TemporalAdjusters.firstDayOfMonth())
    }

    /**
     * その月の最後の日に変換して返却
     *
     * @param date 変更したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getLastDayOfMonth(date: LocalDateTime): LocalDateTime {
        return date.with(TemporalAdjusters.lastDayOfMonth())
    }

    /**
     * その年の最初の日に変換して返却
     *
     * @param date 変更したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getFirstDayOfYear(date: LocalDateTime): LocalDateTime {
        return date.with(TemporalAdjusters.firstDayOfYear())
    }

    /**
     * その年の最後の日に変換して返却
     *
     * @param date 変更したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getLastDayOfYear(date: LocalDateTime): LocalDateTime {
        return date.with(TemporalAdjusters.lastDayOfYear())
    }

    /**
     * 1年前の月の最初の日を取得
     *
     * @param date 変更したい日付
     * @return LocalDateTime 変換後の日付
     */
    fun getOneYearAgoMonth(date: LocalDateTime): LocalDateTime {
        val startMonth = date.minusYears(1)

        return getFirstDayOfMonth(startMonth)
    }

    /**
     * 指定した日付の最小値と最大値の間の年のリストを取得
     *
     * @param min 最小日付
     * @param max 最大日付
     * @return 年のリスト
     */
    fun getBetweenYears(min: LocalDateTime, max: LocalDateTime): List<String> {
        // 年の範囲を生成して、それぞれの年を文字列に変換
        return (min.year..max.year).map { it.toString() }
    }

    /**
     * 引数から年のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 年 文字列
     */
    fun getYearOfStr(date: LocalDateTime): String {
        return date.year.toString()
    }

    /**
     * 引数から月のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 月 文字列
     */
    fun getMonthOfStr(date: LocalDateTime): String {
        return date.monthValue.toString()
    }

    /**
     * 引数から日のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 日 文字列
     */
    fun getDayOfStr(date: LocalDateTime): String {
        return date.dayOfMonth.toString()
    }

    /**
     * 引数から年/月のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 年/月 文字列
     */
    fun getYearMonth(date: LocalDate): String {
        return getYearMonth(localDateToLocalDateTime(date))
    }

    /**
     * 引数から年/月のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 年/月 文字列
     */
    fun getYearMonth(date: LocalDateTime): String {
        return localDateTimeToStr(date, FMT_YEAR_MONTH_SLASH)
    }

    /**
     * 引数から年/月/日のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 年/月/日 文字列
     */
    fun getYearMonthDay(date: LocalDate): String {
        return getYearMonthDay(localDateToLocalDateTime(date))
    }

    /**
     * 引数から年/月/日のみ抜き出し返却
     *
     * @param date 取得対象
     * @return 年/月/日 文字列
     */
    fun getYearMonthDay(date: LocalDateTime): String {
        return localDateTimeToStr(date, FMT_YEAR_MONTH_DAY_SLASH)
    }

    /**
     * 基準となる日付から指定された週数だけさかのぼった範囲に含まれる月の年を取得
     *
     * @param baseDate 基準となる日付
     * @param weeksToGoBack さかのぼる週数
     * @param targetMonth 検索する対象の月
     * @return 指定された週数だけさかのぼった範囲に含まれる対象の月の年。見つからなかった場合はnull。
     */
    fun getYearOfMonthInPreviousWeeks(baseDate: LocalDate, weeksToGoBack: Int, targetMonth: Int): Int? {
        val startDate = baseDate.minusWeeks(weeksToGoBack.toLong())

        // Generate a sequence of dates from startDate to endDate
        val dateSequence = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { it <= baseDate }

        // Find the first date that matches the targetMonth and return its year
        return dateSequence.firstOrNull { it.monthValue == targetMonth }?.year
    }

    /**
     * LocalDateをLocalDateTimeに変換する（その日の始まり、つまり真夜中に設定）
     *
     * @param date 変換するLocalDate
     * @return 変換されたLocalDateTime
     */
    fun localDateToLocalDateTime(date: LocalDate): LocalDateTime {
        return date.atStartOfDay()
    }

    /**
     * StringからLocalDateに変換
     *
     * @param str 変換対象
     * @param fmtPattern 変換パターン
     * @return LocalDateTime
     */
    fun strToLocalDate(str: String?, fmtPattern: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(fmtPattern)

        return LocalDate.parse(str, formatter)
    }

    /**
     * StringからLocalDateTimeに変換
     *
     * @param str 変換対象
     * @param fmtPattern 変換パターン
     * @return LocalDateTime
     */
    fun strToLocalDateTime(str: String?, fmtPattern: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(fmtPattern)

        return LocalDateTime.parse(str, formatter)
    }

    /**
     * LocalDateTimeからStringに変換
     *
     * @param localDateTimeLocalDateTime 変換対象
     * @param fmtPattern 変換パターン
     * @return 文字列形式の日付
     */
    fun localDateTimeToStr(localDateTimeLocalDateTime: LocalDateTime, fmtPattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(fmtPattern)

        return localDateTimeLocalDateTime.format(formatter)
    }
}
