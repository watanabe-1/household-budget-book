package org.book.app.common.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.stereotype.Service
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.io.IOException
import javax.sql.DataSource

/**
 * DB関連設定クラス<br></br>
 * プロパティファイルの読み込み<br></br>
 * このjava以外でも読み込んだファイルの値は使用可能
 */
@Configuration
@PropertySource("classpath:config/properties/database.properties")
@MapperScan("org.book.app.study.mapper")
class DbConfig {

    /**
     * jdbc.hikari に基づく設定を適用
     *
     * @return HikariConfig
     */
    @Bean
    @ConfigurationProperties(prefix = "jdbc")
    fun hikariConfig(): HikariConfig {
        return HikariConfig()
    }

    /**
     * DB接続設定<br></br>
     * プロパティファイルの中身を設定
     *
     * @return dataSource
     */
    @Bean
    fun dataSource(): DataSource {
        return HikariDataSource(hikariConfig())
    }

    /**
     * MyBatis マッピング設定<br></br>
     *
     * @param dataSource the data source
     * @return sqlSessionFactory
     * @throws IOException
     */
    @Bean
    @Throws(IOException::class)
    fun sqlSessionFactory(dataSource: DataSource?): SqlSessionFactoryBean {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)

        val resolver = ResourcePatternUtils.getResourcePatternResolver(DefaultResourceLoader())
        sqlSessionFactoryBean.setMapperLocations(*resolver.getResources("classpath:config/mappers/**/*.xml"))

        val myBatisConfig = org.apache.ibatis.session.Configuration()
        myBatisConfig.isMapUnderscoreToCamelCase = true
        sqlSessionFactoryBean.setConfiguration(myBatisConfig)

        return sqlSessionFactoryBean
    }

    /**
     * トランザクションマネージャーの設定<br></br>
     *
     * @param dataSource the data source
     * @return transactionManager
     */
    @Bean
    fun transactionManager(dataSource: DataSource?): DataSourceTransactionManager {
        val transactionManager = DataSourceTransactionManager()
        transactionManager.dataSource = dataSource

        return transactionManager
    }

    /**
     * aopでトランザクションを制御
     *
     * @param transactionManager
     * @return
     */
    @Bean
    fun transactionAdvisor(transactionManager: TransactionManager?): BeanFactoryTransactionAttributeSourceAdvisor {
        val source = NameMatchTransactionAttributeSource()
        source.addTransactionalMethod("save*", RuleBasedTransactionAttribute())
        source.addTransactionalMethod("update*", RuleBasedTransactionAttribute())
        source.addTransactionalMethod("delete*", RuleBasedTransactionAttribute())

        val advisor = BeanFactoryTransactionAttributeSourceAdvisor()
        advisor.setTransactionAttributeSource(source)
        advisor.advice = TransactionInterceptor(transactionManager!!, source)
        // @Service が付与されているクラスを対象に
        // フィルタ条件を実装し、`setClassFilter`を呼び出す
        advisor.setClassFilter { clazz: Class<*>? ->
            AnnotationUtils.findAnnotation(
                clazz!!, Service::class.java
            ) != null
        }

        return advisor
    }
}
