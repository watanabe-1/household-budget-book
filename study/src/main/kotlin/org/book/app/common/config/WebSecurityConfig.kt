package org.book.app.common.config

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import lombok.RequiredArgsConstructor
import org.book.app.study.enums.type.AccountType
import org.book.app.study.model.properties.RsaKeyProperties
import org.book.app.study.service.AppUserDetailsService
import org.book.app.study.util.StudyUtils.getLoginUser
import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter


/**
 * スプリングセキュリティ設定クラス
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
class WebSecurityConfig(
    private val appUserDetailsService: AppUserDetailsService,
    private val rsaKeys: RsaKeyProperties
) {
    companion object {
        const val USER_ID = "user"
    }

    /**
     * アカウント登録時のパスワードエンコードで利用するためDI管理する
     *
     * @return PasswordEncoder
     */
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 認証ロジッククラス
     *
     * @return AppUserDetailsService
     */
    fun appUserDetailsService(): AppUserDetailsService? {
        return appUserDetailsService
    }

    /**
     * Jwt decoder
     *
     * @return JwtDecoder
     */
    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey).build()
    }

    /**
     * Jwt encoder
     *
     * @return JwtEncoder
     */
    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(rsaKeys.publicKey).privateKey(rsaKeys.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))

        return NimbusJwtEncoder(jwks)
    }

    /**
     * スプリングセキュリティ設定
     *
     * @param http HttpSecurity
     * @return 設定結果
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            // csrfは使用しない
            .csrf { csrf -> csrf.disable() }
            // 全てのリクエストに認証が必要
            .authorizeHttpRequests { auth ->
                // トークン認証時にはデフォルトの「JwtAuthenticationConverter」では「SCOPE_」の prefix が付与される
                auth
                    // トークンの発行はユーザとパスワードで認証したときのみ
                    .requestMatchers("/oauth2/token").hasAnyRole(
                        AccountType.USER.baseRole,
                        AccountType.ADMIN.baseRole,
                        AccountType.SYSTEM.baseRole
                    )
                    // トークンのリフレッシュはリフレッシュトークン(リフレッシュの権限をもったトークン)のみ
                    .requestMatchers("/oauth2/refresh").hasAnyAuthority(AccountType.REFRESH.jwtRole)
                    // リフレッシュトークンの削除は認証済みトークンどれでも
                    .requestMatchers("/oauth2/revoke").hasAnyAuthority(
                        AccountType.USER.jwtRole,
                        AccountType.ADMIN.jwtRole,
                        AccountType.SYSTEM.jwtRole,
                        AccountType.REFRESH.jwtRole
                    )
                    // 上記以外はトークン認証(リフレッシュトークン除く)の時のみ
                    .requestMatchers("/**")
                    .hasAnyAuthority(
                        AccountType.USER.jwtRole,
                        AccountType.ADMIN.jwtRole,
                        AccountType.SYSTEM.jwtRole
                    )
                    .anyRequest().authenticated()
            }
            // OAuth 2 リソースサーバーとして扱うようにする
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            // Spring Security は HttpSession を作成したり、それを使用してセキュリティ コンテキストを取得しない
            .sessionManagement { session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            // filterを使用してログ出力用情報をMDCに付与
            .addFilterAfter({ servletRequest: ServletRequest?, servletResponse: ServletResponse?, filterChain: FilterChain ->
                try {
                    // useridをMDCに付与
                    // logback-sprig.xml内で%X{user}として参照できる
                    MDC.put(USER_ID, getLoginUser())
                    filterChain.doFilter(servletRequest, servletResponse)
                } finally {
                    MDC.remove(USER_ID)
                }
            }, AuthorizationFilter::class.java)
            .httpBasic(Customizer.withDefaults())
            .build()
    }
}
