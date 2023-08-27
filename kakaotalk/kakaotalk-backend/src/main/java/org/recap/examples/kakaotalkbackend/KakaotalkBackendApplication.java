package org.recap.examples.kakaotalkbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KakaotalkBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(KakaotalkBackendApplication.class, args);
	}

}