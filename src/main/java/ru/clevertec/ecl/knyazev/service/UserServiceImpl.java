package ru.clevertec.ecl.knyazev.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.entity.User;
import ru.clevertec.ecl.knyazev.mapper.UserMapper;
import ru.clevertec.ecl.knyazev.repository.UserRepository;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UserServiceImpl implements UserService {
	
	private static final String ADDING_ERROR = "Error on adding user";
	
	private static final String DEFAULT_ROLE = "ROLE_SUBSCRIBER";
	
	private RoleService roleServiceImpl;
	
	private UserRepository userRepository;
	
	private UserMapper userMapperImpl;
	
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public UserDTO showUserByName(String userName) throws ServiceException {
		
		User user = userRepository.findUserByName(userName).orElseThrow(() -> {
			log.error("User with name={} not found", userName);
			return new ServiceException("User not found");
		});		
		
		return userMapperImpl.toUserDTO(user);
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class)
	public UserDTO registerUser(UserDTO userDTO) throws ServiceException {
		
		if (userDTO == null || userDTO.getId() != null) {
			log.error("Invalid user given for registration");
			throw new ServiceException(ADDING_ERROR);
		}
		
		try {
			User savingUser = userMapperImpl.toUser(userDTO);
			
			String password = savingUser.getPassword();
			
			
			savingUser.setPassword(passwordEncoder.encode(password));			
				
			Role defaultRole = roleServiceImpl.showRoleByName(DEFAULT_ROLE);
				
			savingUser.setRoles(new ArrayList<>() {
				
				private static final long serialVersionUID = -7942440692758081191L;
				
			{
				add(defaultRole);
			}});
			
			savingUser.setEnabled(true);
			
			User savedUser = userRepository.save(savingUser);
			
			return userMapperImpl.toUserDTO(savedUser);
			
		} catch (DataAccessException e) {
			log.error("Error when adding user: {}", e.getMessage(), e);
			throw new ServiceException(ADDING_ERROR);
		}
		
	}

}
