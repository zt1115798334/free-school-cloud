package com.example.datasource.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/16 11:29
 * description:
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPrimary",
        transactionManagerRef = "transactionManagerPrimary",
        basePackages = {"com.example.datasource.repo"})

public class PrimaryConfig {

    @Resource
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Resource
    private EntityManagerFactoryBuilder builder;

    @Primary
    @Bean(name = "entityManagerPrimary")
    public EntityManager entityManager() {
        return Objects.requireNonNull(entityManagerFactoryPrimary().getObject()).createEntityManager();
    }

    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private HibernateProperties hibernateProperties;

    private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }

    /**
     * 设置实体类所在位置
     */
    @Primary
    @Bean(name = "entityManagerFactoryPrimary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary() {
        return builder
                .dataSource(primaryDataSource)
                .packages("com.example.datasource.entity")
                .persistenceUnit("primaryPersistenceUnit")
                .properties(getVendorProperties())
                .build();
    }

    @Primary
    @Bean(name = "transactionManagerPrimary")
    public PlatformTransactionManager transactionManagerPrimary() {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryPrimary().getObject()));
    }


}
