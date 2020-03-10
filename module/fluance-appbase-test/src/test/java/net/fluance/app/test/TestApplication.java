/**
 * 
 */
package net.fluance.app.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import net.fluance.app.test.mock.AuthorizationServerMock;

@Configuration
@ComponentScan(basePackages = {"net.fluance.app.test"})
@EnableAutoConfiguration
@PropertySource("classpath:test.properties")
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Bean
	public AuthorizationServerMock authorizationServerMock() {
		return new AuthorizationServerMock();
	}
}
