package org.slf4j.ext

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.NoSuchMessageException
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import java.util.*
import java.util.function.Supplier

/**
 * slf4jが用意している拡張方法
 * lombockの@XSlf4j を使用するために作成
 * メッセージをプロパティファイル参照にする拡張クラス
 */
class XLogger {
    /**
     * ロガー
     */
    private val logger: Logger

    /**
     * コンストラクター
     * SLF4Jを使用
     *
     * @param clazz クラス
     */
    constructor(clazz: Class<*>) {
        logger = LoggerFactory.getLogger(clazz)
    }

    /**
     * テストのための追加コンストラクタ
     *
     * @param logger ロガー
     */
    constructor(logger: Logger) {
        this.logger = logger
    }

    val name: String
        /**
         * ログ出力クラス名を返却
         */
        get() = logger.name


    val isDebugEnabled: Boolean
        /**
         * DEBUGレベルのログ出力を許可しているか、判定する。
         *
         * @return 判定結果
         */
        get() = logger.isDebugEnabled

    val isTraceEnabled: Boolean
        /**
         * TRACEレベルのログ出力を許可しているか、判定する。
         *
         * @return 判定結果
         */
        get() = logger.isTraceEnabled

    /**
     * DEBUGレベルのログはそのままメッセージを出力
     *
     * @param format フォーマット
     * @param args   埋め込み文字
     */
    fun debug(format: String?, vararg args: Any?) {
        logger.debug(format, *args)
    }

    /**
     * INFOレベルのログ
     * 外部ファイルからのメッセージを読み込む
     *
     * @param id   メッセージID
     * @param args 埋め込み文字
     */
    fun info(id: String, vararg args: Any?) {
        if (logger.isInfoEnabled) {
            logger.info(createLogMessage(id, *args))
        }
    }

    /**
     * INFOレベルのログ
     * 外部ファイルからのメッセージを読み込む
     * 埋め込み文字引数を遅延評価する
     *
     * @param id           メッセージID
     * @param argsSupplier 埋め込み文字引数、ログを出力する時のみ評価 nullの時はログを出力しない
     */
    fun info(id: String, argsSupplier: Supplier<Array<Any>?>) {
        if (logger.isInfoEnabled) {
            val args = argsSupplier.get()
            if (args != null) {
                logger.info(createLogMessage(id, *args))
            }
        }
    }

    /**
     * WARNレベルのログ
     * 外部ファイルからのメッセージを読み込む
     *
     * @param id   メッセージID
     * @param args 埋め込み文字
     */
    fun warn(id: String, vararg args: Any?) {
        if (logger.isWarnEnabled) {
            logger.warn(createLogMessage(id, *args))
        }
    }

    /**
     * WARNレベルのログ
     * 外部ファイルからのメッセージを読み込む
     * 埋め込み文字引数を遅延評価する
     *
     * @param id           メッセージID
     * @param argsSupplier 埋め込み文字引数、ログを出力する時のみ評価 nullの時はログを出力しない
     */
    fun warn(id: String, argsSupplier: Supplier<Array<Any>?>) {
        if (logger.isWarnEnabled) {
            val args = argsSupplier.get()
            if (args != null) {
                logger.warn(createLogMessage(id, *args))
            }
        }
    }

    /**
     * ERRORレベルのログ
     * 外部ファイルからのメッセージを読み込む
     *
     * @param id   メッセージID
     * @param args 埋め込み文字
     */
    fun error(id: String, vararg args: Any?) {
        if (logger.isErrorEnabled) {
            logger.error(createLogMessage(id, *args))
        }
    }

    /**
     * ERRORレベルのログ
     * 外部ファイルからのメッセージを読み込む
     * 埋め込み文字引数を遅延評価する
     *
     * @param id           メッセージID
     * @param argsSupplier 埋め込み文字引数、ログを出力する時のみ評価 nullの時はログを出力しない
     */
    fun error(id: String, argsSupplier: Supplier<Array<Any>?>) {
        if (logger.isErrorEnabled) {
            val args = argsSupplier.get()
            if (args != null) {
                logger.error(createLogMessage(id, *args))
            }
        }
    }

    /**
     * TRACEレベルのログ
     * 外部ファイルからのメッセージを読み込む
     *
     * @param id   メッセージID
     * @param args 埋め込み文字
     */
    fun trace(id: String, vararg args: Any?) {
        if (logger.isTraceEnabled) {
            logger.trace(createLogMessage(id, *args))
        }
    }

    /**
     * TRACEレベルのログ
     * 外部ファイルからのメッセージを読み込む
     * 埋め込み文字引数を遅延評価する
     *
     * @param id           メッセージID
     * @param argsSupplier 埋め込み文字引数、ログを出力する時のみ評価 nullの時はログを出力しない
     */
    fun trace(id: String, argsSupplier: Supplier<Array<Any>?>) {
        if (logger.isTraceEnabled) {
            val args = argsSupplier.get()
            if (args != null) {
                logger.trace(createLogMessage(id, *args))
            }
        }
    }

    /**
     * WARNレベルのログ
     * 外部ファイルからのメッセージを読み込む
     *
     * @param id   メッセージID
     * @param t    the exception (throwable) to log
     * @param args 埋め込み文字
     */
    fun warn(id: String, t: Throwable?, vararg args: Any?) {
        if (logger.isWarnEnabled) {
            logger.warn(createLogMessage(id, *args), t)
        }
    }

    /**
     * WARNレベルのログ
     * 外部ファイルからのメッセージを読み込む
     * 埋め込み文字引数を遅延評価する
     *
     * @param id           メッセージID
     * @param t            the exception (throwable) to log
     * @param argsSupplier 埋め込み文字引数、ログを出力する時のみ評価 nullの時はログを出力しない
     */
    fun warn(id: String, t: Throwable?, argsSupplier: Supplier<Array<Any>?>) {
        if (logger.isWarnEnabled) {
            val args = argsSupplier.get()
            if (args != null) {
                logger.warn(createLogMessage(id, *args), t)
            }
        }
    }

    /**
     * ERRORレベルのログ
     * 外部ファイルからのメッセージを読み込む
     *
     * @param id   メッセージID
     * @param t    the exception (throwable) to log
     * @param args 埋め込み文字
     */
    fun error(id: String, t: Throwable?, vararg args: Any) {
        if (logger.isErrorEnabled) {
            logger.error(createLogMessage(id, *args), t)
        }
    }

    /**
     * ERRORレベルのログ
     * 外部ファイルからのメッセージを読み込む
     * 埋め込み文字引数を遅延評価する
     * 埋め込み文字引数がnullの場合ログを出力しない
     *
     * @param id           メッセージID
     * @param t            the exception (throwable) to log
     * @param argsSupplier 埋め込み文字引数、ログを出力する時のみ評価 nullの時はログを出力しない
     */
    fun error(id: String, t: Throwable?, argsSupplier: Supplier<Array<Any>?>) {
        if (logger.isErrorEnabled) {
            val args = argsSupplier.get()
            if (args != null) {
                logger.error(createLogMessage(id, *args), t)
            }
        }
    }

    /**
     * ログメッセージの作成
     *
     * @param id   メッセージID
     * @param args 埋め込み文字
     * @return ログメッセージ
     */
    private fun createLogMessage(id: String, vararg args: Any?): String {
        return MessageFormat.format(LOG_MESSAGE_FORMAT, id, getMessage(id, *args))
    }

    /**
     * ログメッセージの取得
     *
     * @param id   メッセージID
     * @param args 埋め込み文字
     * @return ログメッセージ
     */
    private fun getMessage(id: String, vararg args: Any?): String {
        var message: String
        try {
            val locale = Locale.getDefault()
            message = if (locale == null) "" else messageSource.getMessage(id, args, locale)
        } catch (e: NoSuchMessageException) {
            message = MessageFormat.format(UNDEFINED_MESSAGE_FORMAT, id, args.contentToString())
        }

        return message
    }

    companion object {
        /**
         * ログID未定義時のログメッセージ
         */
        private const val UNDEFINED_MESSAGE_FORMAT = "UNDEFINED-MESSAGE id:{0} arg:{1}"

        private const val LOG_MESSAGE_FORMAT = "[{0}] {1}"

        /**
         * MessageSourceでログメッセージを取得する
         */
        private val messageSource = ReloadableResourceBundleMessageSource()

        // staticイニシャライザにてMessageSourceを生成する
        init {
            // プロパティファイルをパースする際に使用する文字コードを指定
            messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name())
            // i18nに配置したLogMessages.propertiesを読み込む
            messageSource.setBasenames("classpath:config/properties/i18n/LogMessages")
        }
    }
}
