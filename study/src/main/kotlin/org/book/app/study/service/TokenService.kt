package org.book.app.study.service

import org.book.app.study.enums.type.AccountType
import org.book.app.study.model.properties.RefreshTokenProperties
import org.book.app.study.model.properties.TokenProperties
import org.book.app.study.util.StudyUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors


/**
 * Token service
 *
 * @property encoder
 * @property tokenProperties
 * @property refreshTokenProperties
 * @constructor Create empty Token service
 */
@Service
class TokenService(
    private val encoder: JwtEncoder,
    private val tokenProperties: TokenProperties,
    private val refreshTokenProperties: RefreshTokenProperties,
    private val accountService: AccountService
) {
    // パスワードのハッシュ化に使用しているBCryptPasswordEncoderはハッシュ化できる最大桁数が72バイトのため今回は使用しない
    // PasswordEncoderを複数bean登録(別名でも)すると既存の認証時にエラーになってしまうためPbkdf2PasswordEncoderをbean登録はしない
    private val refreshTokenEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    /**
     * Generate token
     *
     * @param authentication
     * @return
     */
    fun generateToken(authentication: Authentication): String {
        val now = StudyUtils.getNowDate().toInstant()
        val scope: String = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(" "))
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(tokenProperties.expiration, ChronoUnit.MINUTES))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()

        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }

    /**
     * Generate token
     *
     * @param authentication
     * @return
     */
    fun generateRefreshToken(authentication: Authentication): String {
        val now = StudyUtils.getNowDate().toInstant()
        val scope: String = AccountType.REFRESH.role
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(refreshTokenProperties.expiration, ChronoUnit.MINUTES))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()
        val refreshToken = encoder.encode(JwtEncoderParameters.from(claims)).tokenValue

        accountService.updateRefreshToken(refreshTokenEncoder.encode(refreshToken), authentication.name)

        return refreshToken
    }

    /**
     * Verify refresh token
     * リフレッシュトークンの有効期限はjwt認証で行っているため、ここでは行わない
     *
     * @param refreshToken
     * @param userid
     * @return
     */
    fun verifyRefreshToken(refreshToken: String, userid: String): Boolean {
        val account = accountService.findOne(userid)
        val currentRefreshToken = account?.refreshToken

        return !currentRefreshToken.isNullOrEmpty() && refreshTokenEncoder.matches(
            refreshToken,
            currentRefreshToken
        )
    }

    /**
     * Revoke refresh token
     *
     * @param userid
     * @return
     */
    fun revokeRefreshToken(userid: String): Int {
        return accountService.updateRefreshToken("", userid)
    }
}