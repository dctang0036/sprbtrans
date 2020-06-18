package com.trans.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DruidConfiguration.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DruidConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.trans.domain";
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    /**
     * 创建数据源
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /*@Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String drivername;

    @Value("${spring.datasource.druid.initial-size}")
    private Integer initialsize;

    @Value("${spring.datasource.druid.min-idle}")
    private Integer minIdle;

    @Value("${spring.datasource.druid.max-active}")
    private Integer maxActive;

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(drivername);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        dataSource.setInitialSize(initialsize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);

        try {
            dataSource.setFilters("stat");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }*/

    /**
     * 声明事务
     * @return
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 声明SqlSession
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("com.trans.domain");
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DruidConfiguration.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }



    /**
     * 配置监控服务器
     * @return 返回监控注册的servlet对象
     * @author SimpleWu
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        servletRegistrationBean.addInitParameter("deny", "");
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }
}
