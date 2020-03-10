package net.fluance.app.spring.data.jpa.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.fluance.app.spring.core.AbstractApplication;

@EnableJpaRepositories("net.fluance")
@EntityScan("net.fluance")
public class Application extends AbstractApplication {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}