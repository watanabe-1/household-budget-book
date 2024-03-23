package org.book.app.study.model.entity

import java.time.LocalDateTime

/**
 * study用の定義
 */
interface Entity {
    /**
     * シリアルキー
     */
    var serialKey: String

    /**
     * 登録日時
     */
    var insDate: LocalDateTime?

    /**
     * 登録ユーザー
     */
    var insUser: String?

    /**
     * 更新日時
     */
    var updDate: LocalDateTime?

    /**
     * 更新ユーザー
     */
    var updUser: String?
}
