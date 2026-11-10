package br.senac.sp.tads.dsw.exemplo6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

//Apenas para o Swagger saber alterar a estrutura do Token????
@SecurityScheme (
	name = "bearerToken",
	type = SecuritySchemeType.HTTP,
	scheme =  "bearer",
	bearerFormat = "JWT"
)

@SpringBootApplication
public class Exemplo6Application {

	public static void main(String[] args) {
		SpringApplication.run(Exemplo6Application.class, args);
	}

}
