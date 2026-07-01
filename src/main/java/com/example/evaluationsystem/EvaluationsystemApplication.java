// src/main/java/com/example/evaluationsystem/EvaluationsystemApplication.java
package com.example.evaluationsystem;

import com.example.evaluationsystem.config.DotenvPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvaluationsystemApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(EvaluationsystemApplication.class);
		application.addInitializers(new DotenvPropertySource());
		application.run(args);
	}
}