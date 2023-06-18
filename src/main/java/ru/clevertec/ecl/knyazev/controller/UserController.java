package ru.clevertec.ecl.knyazev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;


@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @Autowired })
@Validated
public class UserController {
	
	private UserService userServiceImpl;
	
	@GetMapping(value = "/users")
	public ResponseEntity<?> getSecurityUser(@RequestParam(name = "user_name")
										@NotNull(message = "User name must must be no null")
			                         	@Size(message = "User name must be greater than 3 and less than 30 symbols") 
	                                 	String userName) {		
		
		try {
			User securityUser = userServiceImpl.showSecurityUserByName(userName);
			return ResponseEntity.ok().body(securityUser);
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping(value = "/users")
	public ResponseEntity<?> registerUser(@Valid 
										@RequestBody 
										UserDTO userDTO) {
		
		try {
			UserDTO savedUserDTO = userServiceImpl.registerUser(userDTO);
			return ResponseEntity.status(HttpStatus.CREATED)
					             .body(savedUserDTO);
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
}
