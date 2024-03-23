package org.book.app.study.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.book.app.study.model.entity.Account
import org.book.app.study.model.properties.RefreshTokenProperties
import org.book.app.study.model.properties.TokenProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtEncoder

@ExtendWith(MockKExtension::class)
class TokenServiceTest {

    @MockK
    lateinit var jwtEncoder: JwtEncoder

    @MockK
    lateinit var tokenProperties: TokenProperties

    @MockK
    lateinit var refreshTokenProperties: RefreshTokenProperties

    @MockK
    lateinit var accountService: AccountService

    @InjectMockKs
    lateinit var tokenService: TokenService

    private val refreshTokenEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8()
    private lateinit var authentication: Authentication
    private val username = "user"
    private val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
    private val jwtTokenValue = "jwtToken123"
    private val refreshTokenValue = "refreshToken123"
    private val encodedRefreshToken = refreshTokenEncoder.encode(refreshTokenValue)

    @BeforeEach
    fun setUp() {
        authentication = mockk(relaxed = true)
        every { authentication.name } returns username
        every { authentication.authorities } returns authorities
    }

    @Test
    fun `generateToken should produce a valid JWT for the given Authentication`() {
        // モックの認証オブジェクトをセットアップ
        every { authentication.name } returns username
        every { authentication.authorities } returns authorities

        // TokenProperties と RefreshTokenProperties のモックをセットアップ
        every { tokenProperties.expiration } returns 5L
        every { refreshTokenProperties.expiration } returns 60L

        // JwtEncoder のモックを設定
        val jwt = Jwt.withTokenValue(jwtTokenValue)
            .claim("scope", "test")
            .header("alg", "none")
            .build()
        every { jwtEncoder.encode(any()) } returns jwt

        // メソッドをテスト
        val token = tokenService.generateToken(authentication)

        // 期待される結果を検証
        assertEquals(jwtTokenValue, token)
        verify(exactly = 1) { jwtEncoder.encode(any()) }
    }

    @Test
    fun `generateRefreshToken should produce a valid refresh token and update it for the user`() {
        val jwt = Jwt.withTokenValue(refreshTokenValue).claim("scope", "test").header("alg", "none").build()
        every { jwtEncoder.encode(any()) } returns jwt
        every { refreshTokenProperties.expiration } returns 60L
        every { accountService.updateRefreshToken(any(), username) } returns 1

        val refreshToken = tokenService.generateRefreshToken(authentication)

        assert(refreshToken == refreshTokenValue)
        verify(exactly = 1) { jwtEncoder.encode(any()) }
        verify(exactly = 1) { accountService.updateRefreshToken(any(), eq(username)) }
    }

    @Test
    fun `verifyRefreshToken should return true for valid refresh token`() {
        val account = Account(
            "",
            username,
            "",
            "",
            "",
            encodedRefreshToken,
            null,
            null,
            null,
            null
        )

        every { accountService.findOne(username) } returns account
        every { authentication.name } returns username

        val isValid = tokenService.verifyRefreshToken(refreshTokenValue, username)

        assert(isValid)
        verify(exactly = 1) { accountService.findOne(username) }
    }

    @Test
    fun `verifyRefreshToken should return false for valid refresh token`() {
        val dummyEncodedRefreshToken = refreshTokenEncoder.encode("dummy")
        val account = Account(
            "",
            username,
            "",
            "",
            "",
            dummyEncodedRefreshToken,
            null,
            null,
            null,
            null
        )

        every { accountService.findOne(username) } returns account
        every { authentication.name } returns username

        val isValid = tokenService.verifyRefreshToken(refreshTokenValue, username)

        assert(!isValid)
        verify(exactly = 1) { accountService.findOne(username) }
    }

    @Test
    fun `verifyRefreshToken should return false for none refresh token`() {
        val account = Account(
            "",
            username,
            "",
            "",
            "",
            "",
            null,
            null,
            null,
            null
        )

        every { accountService.findOne(username) } returns account
        every { authentication.name } returns username

        val isValid = tokenService.verifyRefreshToken(refreshTokenValue, username)

        assert(!isValid)
        verify(exactly = 1) { accountService.findOne(username) }
    }

    @Test
    fun `revokeRefreshToken should update user's refresh token to an empty string`() {
        every { accountService.updateRefreshToken("", username) } returns 1

        val result = tokenService.revokeRefreshToken(username)

        assert(result == 1)
        verify(exactly = 1) { accountService.updateRefreshToken("", username) }
    }
}