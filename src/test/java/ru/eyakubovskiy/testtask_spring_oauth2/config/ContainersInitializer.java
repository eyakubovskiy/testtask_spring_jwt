package ru.eyakubovskiy.testtask_spring_oauth2.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class ContainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres14.4")
                    .asCompatibleSubstituteFor("postgres"))
                    .withDatabaseName("oauth2demo")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withStartupTimeout(Duration.ofSeconds(30));


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        dbContainer.withInitScript("db/init.db.sql").start();

        TestPropertyValues.of(
                "spring.datasource.url:" + dbContainer.getJdbcUrl(),
                "spring.datasource.username:" + dbContainer.getUsername(),
                "spring.datasource.password:" + dbContainer.getPassword(),
                "spring.liquibase.enabled:true",
                "spring.datasource.driver-class-name:org.postgresql.Driver")
                .applyTo(applicationContext);
    }

}
