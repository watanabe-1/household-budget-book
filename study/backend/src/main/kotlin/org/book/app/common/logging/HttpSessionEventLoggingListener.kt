package org.book.app.common.logging

import jakarta.servlet.http.*
import org.book.app.study.util.logger

/**
 * セッション関連のイベントをログに記録するリスナー
 */
class HttpSessionEventLoggingListener : HttpSessionListener, HttpSessionAttributeListener,
    HttpSessionActivationListener {

    private val logger = logger<HttpSessionEventLoggingListener>()

    /**
     * セッションが非アクティブ化される直前に呼ばれる
     *
     * @param se セッションイベント
     */
    override fun sessionWillPassivate(se: HttpSessionEvent) {
        logger.trace(
            "1.02.01.1003",
            se.session.id, se.source
        )
    }

    /**
     * セッションがアクティブ化された後に呼ばれる
     *
     * @param se セッションイベント
     */
    override fun sessionDidActivate(se: HttpSessionEvent) {
        logger.trace(
            "1.02.01.1004",
            se.session.id, se.source
        )
    }

    /**
     * セッションに属性が追加されたときに呼ばれる
     *
     * @param se セッションバインディングイベント
     */
    override fun attributeAdded(se: HttpSessionBindingEvent) {
        logger.trace(
            "1.02.01.1005",
            se.session.id, se.name, se.value
        )
    }

    /**
     * セッションから属性が削除されたときに呼ばれる
     *
     * @param se セッションバインディングイベント
     */
    override fun attributeRemoved(se: HttpSessionBindingEvent) {
        logger.trace(
            "1.02.01.1006",
            se.session.id, se.name, se.value
        )
    }

    /**
     * セッションの属性が置き換えられたときに呼ばれる
     *
     * @param se セッションバインディングイベント
     */
    override fun attributeReplaced(se: HttpSessionBindingEvent) {
        logger.trace(
            "1.02.01.1007",
            se.session.id, se.name, se.value
        )
    }

    /**
     * セッションが作成されたときに呼ばれる
     *
     * @param se セッションイベント
     */
    override fun sessionCreated(se: HttpSessionEvent) {
        logger.trace(
            "1.02.01.1008",
            se.session.id, se.source
        )
    }

    /**
     * セッションが破棄されるときに呼ばれる
     *
     * @param se セッションイベント
     */
    override fun sessionDestroyed(se: HttpSessionEvent) {
        logger.trace(
            "1.02.01.1009",
            se.session.id, se.source
        )
    }
}
