package org.book.app.study.model.form

import java.io.Serializable

/**
 * ACCOUNT:アカウント(アカウント情報保持テーブル)のentityクラス
 */
data class AccountForm(

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
    var accountType: String

) : Serializable, Form
