package org.book.app.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.lang.NonNull

/**
 * Jacksonの設定クラス
 */
@Configuration
class JacksonConfig {
    /**
     * ObjectMapperをカスタマイズしてBeanとして提供する。
     *
     * @return カスタマイズされたObjectMapper
     */
    @Bean
    @NonNull
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.findAndRegisterModules()
        objectMapper.setDateFormat(StdDateFormat())

        return objectMapper
    }

    /**
     * カスタマイズされたObjectMapperを使用して、
     * MappingJackson2HttpMessageConverterをBeanとして提供する。
     *
     * @return カスタマイズされたMappingJackson2HttpMessageConverter
     */
    @Bean
    fun jsonMessageConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = objectMapper()

        return converter
    }
}
