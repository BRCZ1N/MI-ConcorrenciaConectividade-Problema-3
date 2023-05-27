package app.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableAutoConfiguration
@ComponentScan("application.controllers")
@ComponentScan("application.services")
@Component
public class BankApp {

	public static void main(String[] args) {
		SpringApplication.run(BankApp.class, args);
	}

}
