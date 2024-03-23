package org.book.app.study.util

import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory

/**
 * Loggerを取得
 *
 * @param T
 * @return
 */
inline fun <reified T : Any> T.logger(): XLogger {
    return XLoggerFactory.getXLogger(T::class.java)
}