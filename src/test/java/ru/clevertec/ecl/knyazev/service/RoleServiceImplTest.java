package ru.clevertec.ecl.knyazev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.repository.RoleRepository;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
	
	@Mock
	private RoleRepository roleRepository;
	
	@InjectMocks
	RoleServiceImpl roleServiceImplMock;
	
	@Test
	public void checkShowRoleByNameShouldReturnRole() throws ServiceException {
		
		Optional<Role> expectedRoleWrap = Optional.of(Role.builder()
												  .name("ROLE_USER")
												  .build());
		
		Mockito.when(roleRepository.findRoleByName(Mockito.anyString()))
								   .thenReturn(expectedRoleWrap);
		
		String inputRoleName = "ROLE_USER";
		
		Role actualRole = roleServiceImplMock.showRoleByName(inputRoleName);
		
		assertAll( 
				() -> assertThat(actualRole).isNotNull(),
				() -> assertThat(actualRole.getName()).isEqualTo("ROLE_USER")
		);
	}
	
	@Test
	public void checkShowRoleByNameShouldThrowServiceException() {
		
		Mockito.when(roleRepository.findRoleByName(Mockito.anyString()))
		   .thenReturn(Optional.empty());
		
		String inputRoleName = "ROLE_SASHA";
		
		assertThatExceptionOfType(ServiceException.class)
						.isThrownBy(() -> roleServiceImplMock.showRoleByName(inputRoleName));
	}

}
