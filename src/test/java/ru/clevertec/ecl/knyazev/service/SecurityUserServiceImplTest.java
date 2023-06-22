package ru.clevertec.ecl.knyazev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.entity.User;
import ru.clevertec.ecl.knyazev.mapper.SecurityUserMapper;
import ru.clevertec.ecl.knyazev.mapper.SecurityUserMapperImpl;
import ru.clevertec.ecl.knyazev.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SecurityUserServiceImplTest {
		
	@Mock
	private UserRepository userRepositoryMock;
	
	@Mock
	private SecurityContext securityContextMock;
	
	@Spy
	private SecurityUserMapper securityUserMapperImpl = new SecurityUserMapperImpl();
	
	@InjectMocks
	private SecurityUserServiceImpl securityUserServiceImpl;
		
	
	@Test
	public void checkLoadUserByUsernameShouldReturnUser() {
		
		Optional<User> expectedUser = Optional.of(User.builder()
				 						 .name("Misha")
				 						 .password("{bcrypt}hash")
				 						 .email("misha@mail.ru")
				 						 .enabled(true)
				 						 .roles(new ArrayList<>() {
				 							 
											private static final long serialVersionUID = -6332293037689765330L;

										{
				 							add(Role.builder()
				 									.name("ROLE_ADMIN")
				 									.build()); 
				 						 }})
				 						 .build());
		
		Mockito.when(userRepositoryMock.findUserByName(Mockito.anyString()))
		       .thenReturn(expectedUser);
		
		String inputUserName = "Misha";
		
		org.springframework.security.core.userdetails.User actualUser = securityUserServiceImpl.loadUserByUsername(inputUserName);
		
		assertAll( 
				() -> assertThat(actualUser).isNotNull(),
				() -> assertThat(actualUser.getUsername()).isEqualTo("Misha"),
				() -> assertThat(actualUser.getAuthorities()).anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
		);
	}
	
	@Test
	public void checkLoadUserByUsernameShouldThrowUserNotFoundException() {
		
		Mockito.when(userRepositoryMock.findUserByName(Mockito.anyString()))
	       .thenReturn(Optional.empty());
		
		String inputUserName = "Markovkina";
		
		assertThatExceptionOfType(UsernameNotFoundException.class)
							 .isThrownBy(() -> securityUserServiceImpl.loadUserByUsername(inputUserName));
	}
	
}
