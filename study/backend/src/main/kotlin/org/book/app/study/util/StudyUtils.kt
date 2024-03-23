package org.book.app.study.util

import org.book.app.study.service.AppUserDetails
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Clock
import java.time.ZonedDateTime

/**
 * 便利メソッドクラス
 */
/**
 * 便利メソッドクラス
 */
object StudyUtils {

    private const val COMMON_USER = "common"
    private const val NO_USER = "no_user"
    private const val NO_IMAGE_CODE = "no_image"

    /**
     * ログインユーザーIdを取得する。
     * ユーザが取得できなかった時はノーユーザ(デフォルトユーザ)を返却する。
     */
    fun getLoginUser(): String =
        SecurityContextHolder.getContext()?.authentication?.let { getUserIdFromAuthentication(it) } ?: getNoUser()

    /**
     * ログインユーザーIdを認証情報から取得。
     * 認証情報がnullであることも考慮。
     */
    private fun getUserIdFromAuthentication(authentication: Authentication): String? =
        (authentication.principal as? AppUserDetails)?.account?.userId

    /**
     * 共通ユーザーIdを取得する。
     */
    fun getCommonUser(): String = COMMON_USER

    /**
     * ユーザが存在しないときのデフォルトユーザを取得する。
     */
    fun getNoUser(): String = NO_USER

    /**
     * NoImage画像のコードを取得。
     */
    fun getNoImageCode(): String = NO_IMAGE_CODE

    /**
     * 現在の日時を取得する。
     * 基本的に引数には何もわたさない想定
     *
     * @param clock 基本敵に何も渡さない(デフォルトを使用)
     * @return
     */
    fun getNowDate(clock: Clock = Clock.systemDefaultZone()): ZonedDateTime = ZonedDateTime.now(clock)

}

