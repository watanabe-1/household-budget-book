package org.book.app.study.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LoggingUtilsTest {

    @Test
    fun `logger function should provide correct logger instance for LoggingUtilsTest class`() {
        // LoggingUtilsTestクラスに対してlogger<LoggingUtilsTest>()を呼び出してロガーを取得
        val logger = logger<LoggingUtilsTest>()

        // 得られたXLoggerの名前が、LoggingUtilsTestクラスの完全修飾名と一致するか検証
        assertEquals(LoggingUtilsTest::class.java.name, logger.name)
    }

    @Test
    fun `logger extension should provide correct logger instance for the class`() {
        // LoggingUtilsTestのインスタンスに対してlogger()を呼び出す
        val logger = LoggingUtilsTest().logger()

        // XLoggerFactoryから得られるXLoggerの名前が、LoggingUtilsTestの完全修飾名と一致するか検証
        assertEquals(LoggingUtilsTest::class.java.name, logger.name)
    }
}