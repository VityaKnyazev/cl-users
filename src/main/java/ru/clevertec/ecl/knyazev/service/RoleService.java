package ru.clevertec.ecl.knyazev.service;

import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

public interface RoleService {
	
	/**
	 * 
	 * Show role by role name.
	 * 
	 * @param roleName role name
	 * @return role by role name
	 * @throws ServiceException when role not found
	 */
	Role showRoleByName(String roleName) throws ServiceException;
}
