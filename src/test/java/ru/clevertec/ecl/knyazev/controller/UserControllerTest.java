package ru.clevertec.ecl.knyazev.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.clevertec.ecl.knyazev.dto.RoleDTO;
import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	
	private static final String REQUEST = "/users";
	
	@Mock
	private UserService userServiceImplMock;
	
	@InjectMocks
	private UserController userController;
	
	private MockMvc mockMVC;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void setUp() {
		mockMVC = MockMvcBuilders.standaloneSetup(userController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.defaultRequest(MockMvcRequestBuilders.get("/"))
				.build();
		
		objectMapper = new Jackson2ObjectMapperBuilder().build();
	}
	
	@Test
	public void checkGetUserShouldReturnOk() throws Exception {
		
		UserDTO expectedUserDTO = UserDTO.builder()
						.id(9L)
						.name("Misha")
						.email("misha@mail.ru")
						.password("{Bcrypt}hash")
						.rolesDTO(new ArrayList<>() {
							
							private static final long serialVersionUID = -572319945882270172L;
		
						{
							add(RoleDTO.builder()
									   .id(2L)
									   .name("ROLE_JOURNALIST")
									   .build());
						}})
						.build();
		
		Mockito.when(userServiceImplMock.showUserByName(Mockito.anyString()))
		       .thenReturn(expectedUserDTO);
		
		String inputUserName = "Misha";
		
		MvcResult mvcResult = mockMVC.perform(MockMvcRequestBuilders.get(REQUEST)
											  .param("user_name", inputUserName))
        									  .andReturn();
		
		Integer actualStatus = mvcResult.getResponse().getStatus();
		UserDTO actualUserDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
													   UserDTO.class);
		
		assertAll(
				() -> assertThat(actualStatus).isEqualTo(200),
				() -> assertThat(actualUserDTO).isNotNull(),
				() -> assertThat(actualUserDTO).isEqualTo(expectedUserDTO)
		);
		
	}
	
	@Test
	public void checkGetUserShouldReturnBadRequest() throws Exception {
		
		Mockito.when(userServiceImplMock.showUserByName(Mockito.anyString()))
	       .thenThrow(ServiceException.class);
		
		String inputUserName = "Mosya";
		
		MvcResult mvcResult = mockMVC.perform(MockMvcRequestBuilders.get(REQUEST)
											  .param("user_name", inputUserName))
        									  .andReturn();
		
		Integer actualStatus = mvcResult.getResponse().getStatus();
		
		assertThat(actualStatus).isEqualTo(400);		
	}
	
	@Test
	public void checkRegisterUserShouldReturnOk() throws Exception {
		
		UserDTO expectedUserDTO = UserDTO.builder()
				.name("Misha")
				.password("{Bcrypt}hash")
				.build();

		Mockito.when(userServiceImplMock.registerUser(Mockito.any(UserDTO.class)))
		       .thenReturn(expectedUserDTO);
		
		UserDTO inputUserDTO = UserDTO.builder()
									  .name("Misha")
									  .password("qwerty")
									  .build();
		
		MvcResult mvcResult = mockMVC.perform(MockMvcRequestBuilders.post(REQUEST)
											  .contentType(MediaType.APPLICATION_JSON)
											  .characterEncoding(Charset.forName("UTF-8"))
											  .content(objectMapper.writeValueAsBytes(inputUserDTO)))
											  .andReturn();
		
		Integer actualStatus = mvcResult.getResponse().getStatus();
		UserDTO actualUserDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
													   UserDTO.class);
		
		assertAll(
				() -> assertThat(actualStatus).isEqualTo(201),
				() -> assertThat(actualUserDTO).isNotNull(),
				() -> assertThat(actualUserDTO).isEqualTo(expectedUserDTO)
		);
	}
		
	@Test
	public void checkRegisterUserShouldReturnBadRequest() throws Exception {
		
		Mockito.when(userServiceImplMock.registerUser(Mockito.any(UserDTO.class)))
	       .thenThrow(ServiceException.class);
	
		UserDTO inputUserDTO = UserDTO.builder()
									  .name("Mashka")
									  .password("gjxtveznfrfzlehf")
									  .build();
		
		MvcResult mvcResult = mockMVC.perform(MockMvcRequestBuilders.post(REQUEST)
											  .contentType(MediaType.APPLICATION_JSON)
											  .characterEncoding(Charset.forName("UTF-8"))
											  .content(objectMapper.writeValueAsBytes(inputUserDTO)))
											  .andReturn();
		
		Integer actualStatus = mvcResult.getResponse().getStatus();
		
		assertThat(actualStatus).isEqualTo(400);
		
	}

}
