package com.example.datasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/1 15:49
 * description:
 */
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "quartzDataSource")
    @Qualifier("quartzDataSource")
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource.quartz-data")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

}
