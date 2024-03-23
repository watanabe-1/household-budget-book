package org.book.app.common.config

import org.book.app.study.model.properties.RefreshTokenProperties
import org.book.app.study.model.properties.RsaKeyProperties
import org.book.app.study.model.properties.TokenProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    @Bean
    @ConfigurationProperties(prefix = "security.jwt.rsa")
    fun rsaKeyProperties(): RsaKeyProperties {
        return RsaKeyProperties()
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwt.token")
    fun tokenProperties(): TokenProperties {
        return TokenProperties()
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwt.refresh-token")
    fun refreshTokenProperties(): RefreshTokenProperties {
        return RefreshTokenProperties()
    }
}