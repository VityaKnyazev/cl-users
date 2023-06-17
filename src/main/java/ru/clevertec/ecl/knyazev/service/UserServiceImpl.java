package ru.clevertec.ecl.knyazev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.entity.User;
import ru.clevertec.ecl.knyazev.mapper.UserMapper;
import ru.clevertec.ecl.knyazev.repository.UserRepository;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @Autowired })
@Slf4j
public class UserServiceImpl implements UserService {
	
	private static final String ADDING_ERROR = "Error on adding user";
	
	private UserRepository userRepository;
	
	private UserMapper userMapperImpl;

	@Override
	public UserDTO showUserByName(String userName) throws ServiceException {
		
		User user = userRepository.findUserByName(userName).orElseThrow(() -> {
			log.error("User with name={} not found", userName);
			return new ServiceException("User not found");
		});		
		
		return userMapperImpl.toUserDTO(user);
	}

	@Override
	public UserDTO registerUser(UserDTO userDTO) throws ServiceException {
		
		if (userDTO == null || userDTO.getId() != null) {
			throw new ServiceException(ADDING_ERROR);
		}
		
		try {
			User savingUser = userMapperImpl.toUser(userDTO);
			
			User savedUser = userRepository.save(savingUser);
			
			return userMapperImpl.toUserDTO(savedUser);
			
		} catch (DataAccessException e) {
			log.error("Error when adding user: {}", e.getMessage(), e);
			throw new ServiceException(ADDING_ERROR);
		}
		
	}

}
