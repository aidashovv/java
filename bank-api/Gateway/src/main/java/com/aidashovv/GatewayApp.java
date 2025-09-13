package com.aidashovv;

import auth.repositories.ClientRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "auth")
@EnableJpaRepositories(basePackageClasses = ClientRepository.class)
@EntityScan(basePackages = "auth.domains")
public class GatewayApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(GatewayApp.class, args);
    }
}
