package com.trans.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DruidSlaveConfiguration.PACKAGE, sqlSessionTemplateRef = "slaveSqlSessionTemplate")
// @MapperScan(basePackages = DruidSlaveConfiguration.PACKAGE)
public class DruidSlaveConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DruidSlaveConfiguration.class);

    // 精确到 slave 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.trans.dao.slave";
    static final String MAPPER_LOCATION = "classpath*:mapper/slave/*.xml";


    /**
     * 创建数据源
     * @return
     */
    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * 声明事务transactionManager
     * @return
     */
    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 声明SqlSession
     * @param slaveDataSource
     * @return
     * @throws Exception
     */

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("slaveDataSource") DataSource slaveDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(slaveDataSource);
        // sessionFactory.setTypeAliasesPackage("com.trans.domain.slave");
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DruidSlaveConfiguration.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Bean(name = "slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
