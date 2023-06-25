package ru.clevertec.ecl.knyazev.integration.testconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.clevertec.ecl.knyazev.config.SecurityWebConfig;
import ru.clevertec.ecl.knyazev.config.WebConfig;

@Import(value = { WebConfig.class, SecurityWebConfig.class })
@EnableWebMvc
@EnableJpaRepositories("ru.clevertec.ecl.knyazev.repository")
@ComponentScan(basePackages = { "ru.clevertec.ecl.knyazev.service", "ru.clevertec.ecl.knyazev.mapper",
		"ru.clevertec.ecl.knyazev.controller", "ru.clevertec.ecl.knyazev.config.connection" })
public class TestConfig {
	
	@Bean
	ObjectMapper objectMapper() {

		return new Jackson2ObjectMapperBuilder()
									    .build();
	}
	
}
