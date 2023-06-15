package app.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Classe principal para iniciar a aplicação do banco.
 */
@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableAutoConfiguration
@ComponentScan("app.controllers")
@ComponentScan("app.services")
@Component
public class BankApp {

	/**
	 * Método principal que inicia a aplicação do banco.
	 *
	 * @param args Os argumentos de linha de comando (opcional).
	 */
	public static void main(String[] args) {
		SpringApplication.run(BankApp.class, args);
	}

}
