package net.fluance.app.spring.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"net.fluance"})
@EnableAutoConfiguration
public abstract class AbstractApplication {
}
