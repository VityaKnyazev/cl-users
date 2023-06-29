package ru.clevertec.ecl.knyazev.integration.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.nio.charset.Charset;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.integration.testconfig.TestConfig;
import ru.clevertec.ecl.knyazev.integration.testconfig.testcontainers.PostgreSQLContainersConfig;

@ActiveProfiles(profiles = { "test" })
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yaml" })
@EnableConfigurationProperties
@ConfigurationProperties(value = "spring.security.client-authentication")
@ContextHierarchy({
		@ContextConfiguration(classes = PostgreSQLContainersConfig.class),		
		@ContextConfiguration(classes = TestConfig.class)
})
@RequiredArgsConstructor(onConstructor_ = { @Autowired } )
@Setter
public class UserControllerTest {
		
	private static final String REQUEST = "/users";
	
	private final MockMvc mockMvc;
	
	private final ObjectMapper objectMapper;
	
	private String username;
	
	private String password;

	@Test
	@Transactional
	public void checkGetUserShouldReturnOK() throws Exception {
		
		String inputUserName = "Ivan";
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(Charset.forName("UTF-8"))
				.header("Authorization", getBaseAuthenticationHeaderValue())
				.param("user_name", inputUserName))
				.andReturn();
		
		UserDTO actualUserDTO = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
				 									   UserDTO.class);
		Integer actualStatus = result.getResponse().getStatus();
		
		assertAll(
				() -> assertThat(actualStatus).isEqualTo(200),
				() -> assertThat(actualUserDTO).isNotNull(),
				() -> assertThat(actualUserDTO.getName()).isEqualTo("Ivan"),
				() -> assertThat(actualUserDTO.getRolesDTO()).anyMatch(rDTO -> rDTO.getName()
						                                                           .equals("ROLE_JOURNALIST")) 
		);
		
	}
	
	@Test
	@Transactional
	public void checkGetUserShouldReturnBadRequest() throws Exception {
		
		String inputUserName = "Anton";
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(Charset.forName("UTF-8"))
				.header("Authorization", getBaseAuthenticationHeaderValue())
				.param("user_name", inputUserName))
				.andReturn();
		
		Integer actualStatus = result.getResponse().getStatus();
		
		assertThat(actualStatus).isEqualTo(400);
		
	}
	
	@Test
	@Transactional
	public void checkGetUserShouldReturnNotAuthorized() throws Exception {
		
		String inputUserName = "Alibaba";
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(Charset.forName("UTF-8"))
				.param("user_name", inputUserName))
				.andReturn();
		
		Integer actualStatus = result.getResponse().getStatus();
		
		assertThat(actualStatus).isEqualTo(401);
		
	}
	
	@Test
	@Transactional
	public void checkRegisterUserShouldReturnCreated() throws Exception {
		
		UserDTO inputUserDTO = UserDTO.builder()
									  .name("Alexander")
									  .email("alexander@mail.ru")
									  .password("alexander")
									  .build();
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(Charset.forName("UTF-8"))
				.header("Authorization", getBaseAuthenticationHeaderValue())
				.content(objectMapper.writeValueAsBytes(inputUserDTO)))
				.andReturn();
		
		UserDTO actualUserDTO = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
				   UserDTO.class);
		Integer actualStatus = result.getResponse().getStatus();
		
		assertAll(
		() -> assertThat(actualStatus).isEqualTo(201),
		() -> assertThat(actualUserDTO).isNotNull(),
		() -> assertThat(actualUserDTO.getName()).isEqualTo("Alexander"),
		() -> assertThat(actualUserDTO.getRolesDTO()).anyMatch(rDTO -> rDTO.getName()
		                                            .equals("ROLE_SUBSCRIBER")) 
		);
		
	}
	
	@Test
	@Transactional
	public void checkRegisterUserShouldReturnBadRequest() throws Exception {
		
		UserDTO inputUserDTO = UserDTO.builder()
				  .name("Ivan")
				  .email("ivan_1@mail.ru")
				  .password("alexander")
				  .build();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(REQUEST)
		.contentType(MediaType.APPLICATION_JSON)
		.characterEncoding(Charset.forName("UTF-8"))
		.header("Authorization", getBaseAuthenticationHeaderValue())
		.content(objectMapper.writeValueAsBytes(inputUserDTO)))
		.andReturn();
		
		Integer actualStatus = result.getResponse().getStatus();
		
		assertThat(actualStatus).isEqualTo(400);
		
	}
	
	@Test
	@Transactional
	public void checkRegisterUserShouldReturnNotAuthorized() throws Exception {
		
		UserDTO inputUserDTO = UserDTO.builder()
				  .name("Alexander")
				  .email("alexander@mail.ru")
				  .password("alexander")
				  .build();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(REQUEST)
		.contentType(MediaType.APPLICATION_JSON)
		.characterEncoding(Charset.forName("UTF-8"))
		.content(objectMapper.writeValueAsBytes(inputUserDTO)))
		.andReturn();
	
		Integer actualStatus = result.getResponse().getStatus();
		
		assertThat(actualStatus).isEqualTo(401);
		
	}
	
	private String getBaseAuthenticationHeaderValue() {
		String baseAuth = username + ":" + password;
	    byte[] encodedBaseAuth = Base64.getEncoder()
	           	                       .encode(baseAuth.getBytes(Charset.forName("US-ASCII")));
	    String baseAuthHeaderVal = "Basic " + new String(encodedBaseAuth);
	      
	    return baseAuthHeaderVal;
	}
	
}
