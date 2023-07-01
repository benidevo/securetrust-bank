package com.stb.transactionservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@OpenAPIDefinition(
		info = @Info(
				title = "SecureTrust Bank API - Transaction Service",
				description = "Handles sending and receiving money between bank accounts, enforces transaction " +
						"limits, and manages transaction history.",
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
public class TransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}
