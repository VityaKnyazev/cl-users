package ru.clevertec.ecl.knyazev.service;

import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

public interface UserService {

	/**
	 * 
	 * Get user representation DTO by user name with user roles DTO
	 * 
	 * @param userName user name
	 * @return User DTO by it's name
	 * @throws ServiceException when invalid user name given or nothing found
	 */
	UserDTO showByUserName(String userName) throws ServiceException;

	/**
	 * 
	 * Register user using given user DTO.
	 * 
	 * @param userDTO user DTO to register user
	 * @return user DTO of registered user
	 * @throws ServiceException when user registration failed
	 */
	UserDTO registerUser(UserDTO userDTO) throws ServiceException;

}
