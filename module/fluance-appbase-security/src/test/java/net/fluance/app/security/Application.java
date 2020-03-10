/**
 * 
 */
package net.fluance.app.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import net.fluance.app.security.util.JwtHelper;

@Configuration
@ComponentScan(basePackages = {"net.fluance.app.security"})
@EnableAutoConfiguration
@PropertySource({"classpath:webapps/conf/security.properties", "classpath:test.properties"})
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public JwtHelper jwtHelper() {
		return new JwtHelper();
	}
}