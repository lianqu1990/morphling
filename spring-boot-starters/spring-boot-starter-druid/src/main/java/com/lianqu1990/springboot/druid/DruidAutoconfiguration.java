package com.lianqu1990.springboot.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author hanchao
 * @date 2017/1/14 23:03
 */
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
public class DruidAutoconfiguration {
    @Autowired
    private DruidProperties druidProperties;
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        if(druidProperties.getInitialSize()!=null){
            dataSource.setInitialSize(druidProperties.getInitialSize());
        }
        if(druidProperties.getMinIdle()!=null){
            dataSource.setMinIdle(druidProperties.getMinIdle());
        }
        if(druidProperties.getMaxActive()!=null){
            dataSource.setMaxActive(druidProperties.getMaxActive());
        }
        if(druidProperties.getMaxWait()!=null){
            dataSource.setMaxWait(druidProperties.getMaxWait());
        }
        if(druidProperties.getTimeBetweenEvictionRunsMillis()!=null){
            dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        }
        if(druidProperties.getMinEvictableIdleTimeMillis()!=null){
            dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        }
        if(druidProperties.getValidationQuery() != null){
            dataSource.setValidationQuery(druidProperties.getValidationQuery());
        }
        dataSource.setPoolPreparedStatements(druidProperties.isPoolPreparedStatements());
        if(druidProperties.isPoolPreparedStatements() && druidProperties.getMaxPoolPreparedStatementPerConnectionSize()!=null){
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());
        }
        dataSource.setTestWhileIdle(druidProperties.isTestWhileIdle());
        dataSource.setTestOnBorrow(druidProperties.isTestOnBorrow());
        dataSource.setTestOnReturn(druidProperties.isTestOnReturn());
        if(druidProperties.getMaxPoolPreparedStatementPerConnectionSize()!=null){
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());
        }
        if(druidProperties.getFilters()!=null){
            dataSource.setFilters(druidProperties.getFilters());
        }
        if(druidProperties.getConnectionProperties()!=null){
            dataSource.setConnectProperties(druidProperties.getConnectionProperties());
        }
        return dataSource;
    }
}

/**
 * 用法示例:
spring:

        output:
        ansi:
        enabled: always


        datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://172.18.129.96:3306/agent_plat?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&autoReconnectForPools=true&failOverReadOnly=false&connectTimeout=5000&useSSL=false
        username: root
        password: lihaojie

        druid:
        initialSize: 10
        minIdle: 10
        maxActive: 60
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis : 300000
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        validationQuery: "select 'x'"
        filters: stat,slf4j
*/