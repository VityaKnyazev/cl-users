package ru.clevertec.ecl.knyazev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.clevertec.ecl.knyazev.dto.RoleDTO;
import ru.clevertec.ecl.knyazev.dto.UserDTO;
import ru.clevertec.ecl.knyazev.entity.Role;
import ru.clevertec.ecl.knyazev.entity.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

	@Mapping(source = "roles", target = "rolesDTO")
	public abstract UserDTO toUserDTO(User user);

	@Mapping(source = "rolesDTO", target = "roles")
	public abstract User toUser(UserDTO userDTO);
	
	@Mapping(target = "userDTO", ignore = true)
	protected abstract RoleDTO toRoleDTO(Role role);
	
}
