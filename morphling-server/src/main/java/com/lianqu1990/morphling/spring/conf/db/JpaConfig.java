package com.lianqu1990.morphling.spring.conf.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hanchao
 * @date 2017/1/14 23:01
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
@EnableJpaRepositories(value = "com.lianqu1990.morphling.dao.jpa",repositoryImplementationPostfix = "CustomImpl")//jpa包配置
@EntityScan("com.lianqu1990.morphling.dao.jpa.entity")//jpa实体类包配置
public class JpaConfig {
}
