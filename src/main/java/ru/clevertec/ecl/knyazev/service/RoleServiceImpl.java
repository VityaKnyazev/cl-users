package ru.clevertec.ecl.knyazev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.repository.RoleRepository;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class RoleServiceImpl implements RoleService {
	
	private RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public Role showRoleByName(String roleName) throws ServiceException {
		
		Role role = roleRepository.findRoleByName(roleName).orElseThrow(() -> {
			log.error("Role with name={} not found", roleName);
			return new ServiceException("Role not found");
		});		
		
		return role;
	}

}
