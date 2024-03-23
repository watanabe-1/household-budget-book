package org.book.app.study.model.entity

import java.io.Serializable
import java.time.LocalDateTime

/**
 * ACCOUNT:アカウント(アカウント情報保持テーブル)のentityクラス
 */
data class Account(
    /**
     * シリアルキー
     */
    override var serialKey: String,

    /**
     * ユーザーID
     */
    var userId: String,

    /**
     * パスワード
     */
    var password: String,

    /**
     * ユーザー名
     */
    var userName: String,

    /**
     * アカウント種別
     */
    var accountType: String,

    /**
     * リフレッシュトークン
     */
    var refreshToken: String,

    /**
     * 登録日時
     */
    override var insDate: LocalDateTime?,

    /**
     * 登録ユーザー
     */
    override var insUser: String?,

    /**
     * 更新日時
     */
    override var updDate: LocalDateTime?,

    /**
     * 更新ユーザー
     */
    override var updUser: String?,
) : Serializable, Entity
