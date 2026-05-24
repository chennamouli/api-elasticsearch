package com.elastic.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ApiElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiElasticsearchApplication.class, args);
	}

}
