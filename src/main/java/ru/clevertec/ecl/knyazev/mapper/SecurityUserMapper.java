package ru.clevertec.ecl.knyazev.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Mapper(componentModel = "spring")
public interface SecurityUserMapper {
	
	
	default User toSecurityUser(ru.clevertec.ecl.knyazev.entity.User user) {
		
		if (user == null) {
			return null;
		}
		
		String name = user.getName();
		String password = user.getPassword();
		Set<GrantedAuthority> grantedAuthority = user.getRoles().stream()
	               .map(role -> new SimpleGrantedAuthority(role.getName()))
	               .collect(Collectors.toSet());
		
		return new User(name, password, grantedAuthority);
	}
	
}
