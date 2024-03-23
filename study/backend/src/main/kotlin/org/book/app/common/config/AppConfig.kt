package org.book.app.common.config

import org.book.app.common.filter.UniqueRequestIDFilter
import org.book.app.common.logging.HttpSessionEventLoggingListener
import org.book.app.common.logging.ReauestLoggingListener
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets

@Configuration
class AppConfig : WebMvcConfigurer {
    /**
     * メッセージソースを定義
     *
     * @return メッセージソース
     */
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        // ValidationMessage.propertiesを使用
        messageSource.setBasenames(
            "classpath:config/properties/i18n/ValidationMessages",
            "classpath:config/properties/i18n/LogMessages",
            "classpath:config/properties/i18n/SpringSecurityMessages"
        )
        // メッセージプロパティの文字コードを指定
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name())
        
        return messageSource
    }

    /**
     * バリデータ
     *
     * @return バリデータ
     */
    @Bean
    fun validator(): LocalValidatorFactoryBean {
        val localValidatorFactoryBean = LocalValidatorFactoryBean()
        localValidatorFactoryBean.setValidationMessageSource(messageSource())

        return localValidatorFactoryBean
    }

    /**
     * バリデータを取得
     *
     * @return バリデータ
     */
    override fun getValidator(): Validator? {
        return validator()
    }

    /**
     * デフォルトサーブレットも使用
     *
     * @return
     */
    @Bean
    fun enableDefaultServlet(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        return WebServerFactoryCustomizer { factory: ConfigurableServletWebServerFactory ->
            factory.setRegisterDefaultServlet(
                true
            )
        }
    }

    /**
     * デフォルトサーブレットの転送機能を許可
     *
     * @return
     */
    override fun configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) {
        // デフォルトサーブレットへの転送機能を有効化
        // これを許可することによって静的コンテンツ(cssやjsなど)へのアクセスを許可
        configurer.enable()
    }

    /**
     * リクエストのトレースログ用
     *
     * @return
     */
    @Bean
    fun traceReauestInterceptor(): ReauestLoggingListener {
        return ReauestLoggingListener()
    }

    /**
     * リクエストに一意の値を振る
     *
     * @return
     */
    @Bean
    fun uniqueRequestIDFilter(): UniqueRequestIDFilter {
        return UniqueRequestIDFilter()
    }

    /**
     * セッションのトレースログ用
     *
     * @return
     */
    @Bean
    fun httpSessionEventLoggingListener(): HttpSessionEventLoggingListener {
        return HttpSessionEventLoggingListener()
    }

    /**
     * addInterceptorsを追加<br></br>
     * 複数可
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(traceReauestInterceptor()).addPathPatterns("/**")
            .excludePathPatterns("/resources/**")
    }
}
