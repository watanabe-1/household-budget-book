package org.book.app.study.controller

import io.mockk.*
import org.book.app.common.config.JwtConfig
import org.book.app.common.config.WebSecurityConfig
import org.book.app.common.exception.BusinessException
import org.book.app.study.model.dto.TokenDto
import org.book.app.study.model.entity.Account
import org.book.app.study.model.response.TokenResponse
import org.book.app.study.service.AccountService
import org.book.app.study.service.AppUserDetailsService
import org.book.app.study.service.TokenService
import org.book.app.study.util.StudyStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.security.Principal

@WebMvcTest(AuthControllerTest::class, AuthController::class)
@Import(
    WebSecurityConfig::class,
    TokenService::class,
    AppUserDetailsService::class,
    JwtConfig::class
)
internal class AuthControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var accountService: AccountService

    private lateinit var tokenService: TokenService
    private lateinit var authController: AuthController

    @BeforeEach
    fun setUp() {
        tokenService = mockk()
        authController = AuthController(tokenService)
    }

    @Test
    fun `root endpoint when unauthenticated should return 401 Unauthorized`() {
        mvc.perform(get("/"))
            .andExpect(status().isUnauthorized())
    }

    @Test
    @Throws(Exception::class)
    fun `root endpoint when authenticated should greet user`() {
        // アカウント情報
        val expectedAccount = Account(
            "",
            "testUser",
            "\$2a\$10\$A20zzLB2XpvjOPn7wN/Olu0MoIk8ilS4tfV.AXsxln.l/zt.PrQsm",
            "testUserName",
            "01",
            "",
            null,
            null,
            null,
            null
        )
        `when`(accountService.findOne(anyString())).thenReturn(expectedAccount)

        // トークン発行(認証成功)確認
        val result = mvc.perform(post("/oauth2/token").with(httpBasic("testUser", "admin")))
            .andExpect(status().isOk())
            .andReturn()

        val jsonObject = result.response.contentAsString

        val tokenRes = StudyStringUtils.jsonToObject<TokenResponse>(jsonObject);

        // 発行されたトークンを使用して認証が通るか確認
        mvc.perform(
            get("/oauth2/hello")
                .header("Authorization", "Bearer ${tokenRes.token}")
        )
            .andExpect(content().string("Hello, testUser"))
    }

    @Test
    fun `token() should return TokenResponse when authentication is successful`() {
        val authentication = UsernamePasswordAuthenticationToken("user", "password")
        every { tokenService.generateToken(any()) } returns TokenDto(
            "MockToken", 1, "", ""
        )
        every { tokenService.generateRefreshToken(any()) } returns "MockRefreshToken"

        val result = authController.token(authentication)

        assertEquals("MockToken", result.token)
        assertEquals("MockRefreshToken", result.refreshToken)
        verify(exactly = 1) { tokenService.generateToken(any()) }
        verify(exactly = 1) { tokenService.generateRefreshToken(any()) }
    }

    @Test
    fun `refresh() should return new TokenResponse when refresh token is valid`() {
        val jwtAuthenticationToken = mockk<JwtAuthenticationToken>()
        every { jwtAuthenticationToken.name } returns "user"
        every { jwtAuthenticationToken.token.tokenValue } returns "MockRefreshToken"
        every { tokenService.verifyRefreshToken(any(), any()) } returns true
        every { tokenService.generateToken(any()) } returns TokenDto(
            "NewMockToken", 1, "", ""
        )

        val result = authController.refresh(jwtAuthenticationToken)

        assertEquals("NewMockToken", result.token)
        assertEquals("MockRefreshToken", result.refreshToken)
        verify(exactly = 1) { tokenService.verifyRefreshToken(any(), any()) }
        verify(exactly = 1) { tokenService.generateToken(any()) }
    }

    @Test
    fun `refresh() should throw BusinessException when refresh token is invalid`() {
        val jwtAuthenticationToken = mockk<JwtAuthenticationToken>()
        every { jwtAuthenticationToken.name } returns "user"
        every { jwtAuthenticationToken.token.tokenValue } returns "InvalidRefreshToken"
        every { tokenService.verifyRefreshToken(any(), any()) } returns false

        assertThrows(BusinessException::class.java) {
            authController.refresh(jwtAuthenticationToken)
        }
        verify(exactly = 1) { tokenService.verifyRefreshToken(any(), any()) }
        verify(exactly = 0) { tokenService.generateToken(any()) }
    }

    @Test
    fun `revoke() should return success message when called`() {
        val jwtAuthenticationToken = mockk<JwtAuthenticationToken>()
        every { jwtAuthenticationToken.name } returns "user"
        every { tokenService.revokeRefreshToken(any()) } returns 1

        val result = authController.revoke(jwtAuthenticationToken)

        assertEquals("Refresh Token revoke successfully.", result)
        verify(exactly = 1) { tokenService.revokeRefreshToken(any()) }
    }

    @Test
    fun `hello() should return greeting message with the principal's name`() {
        val principal = mockk<Principal>()
        every { principal.name } returns "TestUser"

        val result = authController.hello(principal)

        assertEquals("Hello, TestUser", result)
    }
}