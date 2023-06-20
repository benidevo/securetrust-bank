package com.stb.bankaccountservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "SecureTrust Bank API - Bank Account Service",
				description = "Manages the creation of bank accounts, sets initial transaction limits, and processes account upgrades.",
				version = "v1.0",
				contact = @Contact(
						name = "Benjamin Idewor",
						email = "benjaminidewor@gmail.com",
						url = "https://github.com/benidevo"
				),
				license = @License(
						name = "MIT",
						url = "https://opensource.org/license/mit/"
				)
		)
)
public class BankAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountServiceApplication.class, args);
	}

}
