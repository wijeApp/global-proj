package com.tas.global.globalven.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    /**
     * MySQL DataSource - Primary database for all JPA entities (users, employees, etc.)
     * This is the @Primary datasource for Spring Boot auto-configuration
     */    @Bean("mysqlDataSource")
    @Primary
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://shortline.proxy.rlwy.net:30481/globalschema?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8")
                .username("root")
                .password("TvWBneTFotjOYGjediHZiBzLGDdBnaiP")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    /**
     * SQL Server DataSource - Used ONLY for GL Reference Codes API
     * This is a secondary datasource specifically for GL Reference Code operations
     */
    @Bean("sqlServerDataSource")
    public DataSource sqlServerDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:sqlserver://124.43.199.76:1497;databaseName=TasGlobalDB;encrypt=false;trustServerCertificate=true")
                .username("sa")
                .password("m@18953789")
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .build();
    }

    /**
     * SQL Server JdbcTemplate - Used ONLY for GL Reference Codes API
     */
    @Bean("sqlServerJdbcTemplate")
    public JdbcTemplate sqlServerJdbcTemplate(@Qualifier("sqlServerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
