package ru.clevertec.ecl.knyazev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.entity.User;
import ru.clevertec.ecl.knyazev.mapper.UserMapper;
import ru.clevertec.ecl.knyazev.mapper.UserMapperImpl;
import ru.clevertec.ecl.knyazev.repository.UserRepository;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	@Mock
	private RoleService roleServiceImplMock;
	
	@Mock
	private UserRepository userRepositoryMock;
	
	@Spy
	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	@Spy
	private UserMapper userMapperImpl = new UserMapperImpl();
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	public void checkShowUserByNameShouldReturnUserDTO() throws ServiceException {
		
		Optional<User> expectedUserWrap = Optional.of(User.builder()
											  .id(8L)
											  .name("Ivan")
											  .password("{Bcrypt}hash")
											  .email("ivan@mail.ru")
											  .enabled(true)
											  .roles(new ArrayList<>() {
											  
								     		    private static final long serialVersionUID = 1245154L;

								     		    {
								     		    	add(Role.builder()
								     		    			.id(1L)
								     		    			.name("ROLE_USER")
								     		    			.build());
											  }}).build());
		
		Mockito.when(userRepositoryMock.findUserByName(Mockito.anyString()))
								   .thenReturn(expectedUserWrap);
		
		String inputUserName = "Ivan";
		
		UserDTO actualUserDTO = userServiceImpl.showUserByName(inputUserName);
		
		assertAll( 
				() -> assertThat(actualUserDTO).isNotNull(),
				() -> assertThat(actualUserDTO.getName()).isEqualTo("Ivan"),
				() -> assertThat(actualUserDTO.getEmail()).isEqualTo("ivan@mail.ru"),
				() -> assertThat(actualUserDTO.getRolesDTO()).anyMatch(rDTO -> rDTO.getName().equals("ROLE_USER"))
		);
	}
	
	@Test
	public void checkShowUserByNameShouldThrowServiceException() {
		
		Mockito.when(userRepositoryMock.findUserByName(Mockito.anyString()))
		   .thenReturn(Optional.empty());
		
		String inputUserName = "Misha";
		
		assertThatExceptionOfType(ServiceException.class)
						.isThrownBy(() -> userServiceImpl.showUserByName(inputUserName));
	}
	
	@Test
	public void checkRegisterUser() throws ServiceException {
		
		Role expectedRole = Role.builder()
					            .id(1L)
					            .name("ROLE_SUBSCRIBER")
					            .build();
		
		User expectedUser = User.builder()
								.id(9L)
								.name("Misha")
								.email("misha@mail.ru")
								.password(passwordEncoder.encode("12345"))
								.roles(new ArrayList<>() {
									
									private static final long serialVersionUID = -572319945882270172L;

								{
									add(expectedRole);
								}})
								.build();
		
		Mockito.when(roleServiceImplMock.showRoleByName(Mockito.anyString()))
		       .thenReturn(expectedRole);
		
		Mockito.when(userRepositoryMock.save(Mockito.any(User.class)))
			   .thenReturn(expectedUser);
		
		UserDTO inputUserDTO = UserDTO.builder()
									  .name("Misha")
									  .email("misha@mail.ru")
									  .password("12345")
									  .build();
		
		UserDTO actualUserDTO = userServiceImpl.registerUser(inputUserDTO);
		
		assertAll( 
				() -> assertThat(actualUserDTO).isNotNull(),
				() -> assertThat(actualUserDTO.getId()).isEqualTo(expectedUser.getId()),
				() -> assertThat(actualUserDTO.getName()).isEqualTo(expectedUser.getName()),
				() -> assertThat(actualUserDTO.getPassword()).isEqualTo(expectedUser.getPassword()),
				() -> assertThat(actualUserDTO.getRolesDTO()).anyMatch(rDTO -> rDTO.getName().equals("ROLE_SUBSCRIBER"))
		);		
		
	}
	
	@ParameterizedTest
	@MethodSource("getInvalidUserDTO")
	public void checkRegisterUserShouldThrowServiceExceptionOnInvalidInputUserDTO(UserDTO invalidUserDTO) {
		
		assertThatExceptionOfType(ServiceException.class)
		 					 .isThrownBy(() -> userServiceImpl.registerUser(invalidUserDTO));
		
	}
	
	@Test
	public void checkRegisterUserShouldThrowServiceExceptionOnFailedUserSaving() throws ServiceException {
		
		Role expectedRole = Role.builder()
	            .id(1L)
	            .name("ROLE_SUBSCRIBER")
	            .build();
		
		Mockito.when(roleServiceImplMock.showRoleByName(Mockito.anyString()))
	       .thenReturn(expectedRole);
	
		Mockito.when(userRepositoryMock.save(Mockito.any(User.class)))
		   .thenThrow(new DataAccessException("Saving user email constraint") {
			   
			private static final long serialVersionUID = 1L;
			   
		   });
		
		UserDTO inputUserDTO = UserDTO.builder()
				  .name("Moisha")
				  .email("moisha@mail.ru")
				  .password("qwerty")
				  .build();
		
		assertThatExceptionOfType(ServiceException.class)
		 .isThrownBy(() -> userServiceImpl.registerUser(inputUserDTO));
		
	}
	
	private static Stream<UserDTO> getInvalidUserDTO() {
		
		return Stream.of( 
			 null,
			 UserDTO.builder()
			 		.id(2L)
			 		.build()
		);
	}

}
