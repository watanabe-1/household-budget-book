package org.book.app.study.util

import org.book.app.study.model.entity.Account
import org.book.app.study.service.AppUserDetails
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

internal class StudyUtilsTest {
    @BeforeEach
    fun setUp() {
        SecurityContextHolder.clearContext() // コンテキストをクリア
    }

    @Test
    fun `getLoginUser with valid user returns UserId`() {
        // モックのセットアップ
        val userDetails: AppUserDetails = mock()
        val account: Account = mock()
        `when`(userDetails.account).thenReturn(account)
        `when`(account.userId).thenReturn("expectedUserId")

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null)
        SecurityContextHolder.getContext().authentication = authentication

        // テストの実行
        val userId = StudyUtils.getLoginUser()
        assertEquals("expectedUserId", userId)
    }

    @Test
    fun `getLoginUser with no authentication`() {
        val userId = StudyUtils.getLoginUser()
        assertEquals(StudyUtils.getNoUser(), userId)
    }

    @Test
    fun `getLoginUser with different principal type`() {
        val authentication = UsernamePasswordAuthenticationToken("notAppUserDetails", null)
        SecurityContextHolder.getContext().authentication = authentication

        val userId = StudyUtils.getLoginUser()
        assertEquals(StudyUtils.getNoUser(), userId)
    }

    @Test
    fun `getLoginUser with null UserId`() {
        val userDetails: AppUserDetails = mock()
        val account: Account = mock()
        `when`(userDetails.account).thenReturn(account)
        `when`(account.userId).thenReturn(null)

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null)
        SecurityContextHolder.getContext().authentication = authentication

        // テストの実行
        val userId = StudyUtils.getLoginUser()
        assertEquals(StudyUtils.getNoUser(), userId)
    }

    @Test
    fun `getCommonUser returns common UserId`() {
        // メソッドの実行
        val commonUserId = StudyUtils.getCommonUser()

        // 期待値との比較
        assertEquals("common", commonUserId)
    }

    @Test
    fun `getNoImageCode returns no image code`() {
        // メソッドの実行
        val noImageCode = StudyUtils.getNoImageCode()

        // 期待値との比較
        assertEquals("no_image", noImageCode)
    }

    @Test
    fun `getNowDate returns current date`() {
        // 時刻を固定するためのモックClockを作成
        val fixedInstant = ZonedDateTime.parse("2024-03-20T10:15:30+01:00[Europe/Paris]").toInstant()
        val fixedClock = Clock.fixed(fixedInstant, ZoneId.of("Europe/Paris"))

        // getNowDate関数をテスト
        val result = StudyUtils.getNowDate(fixedClock)

        // 期待される結果をアサート
        assertEquals(ZonedDateTime.ofInstant(fixedInstant, ZoneId.of("Europe/Paris")), result)
    }
}
