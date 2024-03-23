package org.slf4j.ext

object XLoggerFactory {
    /**
     * XLogger instance 返却クラス
     *
     * @param clazz 対象のクラス
     * @return XLogger instance
     */
    fun getXLogger(clazz: Class<*>): XLogger {
        return XLogger(clazz)
    }
}
