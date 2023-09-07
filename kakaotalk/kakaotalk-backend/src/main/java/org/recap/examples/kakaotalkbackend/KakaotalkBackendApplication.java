package org.recap.examples.kakaotalkbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KakaotalkBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("org.apache.tomcat.websocket.DEFAULT_BUFFER_SIZE", Integer.toString(1024 * 1024));
		SpringApplication.run(KakaotalkBackendApplication.class, args);
	}

}