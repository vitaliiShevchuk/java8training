package com.serena;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configurable
public class LoadDBAndData {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:~/java8trainingDB")
                .username("sa")
                .build();
    }

    public static void main(String[] args) throws SQLException {
        ConfigurableApplicationContext context = SpringApplication.run(LoadDBAndData.class);
        DataSource dataSource = context.getBean(DataSource.class);
        Connection connection = dataSource.getConnection();

        connection.close();
    }

}
