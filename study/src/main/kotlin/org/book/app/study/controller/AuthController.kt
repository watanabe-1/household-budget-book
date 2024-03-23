package org.book.app.study.controller

import org.book.app.common.exception.BusinessException
import org.book.app.study.model.response.TokenResponse
import org.book.app.study.service.TokenService
import org.book.app.study.util.logger
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

/**
 * Auth controller
 *
 * @property tokenService
 * @constructor Create empty Auth controller
 */
@RestController
class AuthController(private val tokenService: TokenService) {

    private val logger = logger<AuthController>()

    /**
     * Tokenの発行
     * ユーザ、パスワードの認証が通ったときのみ呼ばれる想定
     * トークンの認証が通っても呼ばれない想定
     *
     * @param authentication
     * @return
     */
    @PostMapping("/oauth2/token")
    fun token(authentication: Authentication): TokenResponse {
        logger.debug("Token requested for user: '{}'", authentication.name)
        val token: String = tokenService.generateToken(authentication)
        logger.debug("Token granted: {}", token)
        val refreshToken: String = tokenService.generateRefreshToken(authentication)
        logger.debug("Refresh Token granted: {}", refreshToken)

        return TokenResponse(token, refreshToken)
    }

    /**
     * Refresh Tokenの検証とTokenの発行
     * トークン認証が通ったかつ権限がREFRESHの時のみ呼ばれる想定
     *
     * @param authentication
     * @return
     */
    @PostMapping("/oauth2/refresh")
    fun refresh(authentication: JwtAuthenticationToken): TokenResponse {
        val refreshToken: String = authentication.token.tokenValue
        logger.debug("Request RefreshToken: {}", refreshToken)

        return tokenService.verifyRefreshToken(refreshToken, authentication.name).takeIf { it }?.let {
            logger.debug("Refresh Token requested for user: '{}'", authentication.name)
            val token: String = tokenService.generateToken(authentication)
            logger.debug("Token granted: {}", token)

            TokenResponse(token, refreshToken)
        } ?: throw BusinessException("1.01.01.1012")
    }

    /**
     * Refresh Tokenの検証とTokenの発行
     * トークン認証が通ったかつ権限がREFRESHの時のみ呼ばれる想定
     *
     * @param authentication
     * @return
     */
    @PostMapping("/oauth2/revoke")
    fun revoke(authentication: JwtAuthenticationToken): String {
        logger.debug("Refresh Token revoke requested for user: '{}'", authentication.name)
        tokenService.revokeRefreshToken(authentication.name)

        return "Refresh Token revoke successfully."
    }

    /**
     * Hello
     *
     * @param principal
     * @return
     */
    @GetMapping("/oauth2/hello")
    fun hello(principal: Principal): String {
        return "Hello, ${principal.name}"
    }
}