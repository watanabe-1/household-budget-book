package org.book.app.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.view.BeanNameViewResolver

@Configuration
class ViewConfig {
    @Bean
    fun beanNameViewResolver(): BeanNameViewResolver {
        val beanNameViewResolver = BeanNameViewResolver()
        beanNameViewResolver.order = 0
        return beanNameViewResolver
    }
}
