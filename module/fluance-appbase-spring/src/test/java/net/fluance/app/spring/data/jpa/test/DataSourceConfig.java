/**
 * 
 */
package net.fluance.app.spring.data.jpa.test;

import javax.sql.DataSource;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("classpath:conf/datasource.properties")
public class DataSourceConfig {

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;
	@Value("${spring.datasource.platform}")
	private String platform;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	public String getDriverClassName() {
		return driverClassName;
	}



	public String getPlatform() {
		return platform;
	}



	public String getUrl() {
		return url;
	}



	public String getUsername() {
		return username;
	}



	public String getPassword() {
		return password;
	}

    /**
     * As data source we use an external database
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }


	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
