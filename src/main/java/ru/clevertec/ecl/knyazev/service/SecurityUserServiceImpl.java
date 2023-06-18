package ru.clevertec.ecl.knyazev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.ecl.knyazev.mapper.SecurityUserMapper;
import ru.clevertec.ecl.knyazev.repository.UserRepository;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @Autowired })
@Slf4j
public class SecurityUserServiceImpl implements UserDetailsService {

	private static final String USER_NOT_FOUND = "User not found";

	private UserRepository userRepository;

	private SecurityUserMapper securityUserMapperImpl;

	@Override
	public User loadUserByUsername(String userName) throws UsernameNotFoundException {

		ru.clevertec.ecl.knyazev.entity.User user = userRepository.findUserByName(userName).orElseThrow(() -> {
			log.error(USER_NOT_FOUND + " with name {}", userName);
			return new UsernameNotFoundException(USER_NOT_FOUND);
		});

		User securityUser = securityUserMapperImpl.toSecurityUser(user);

		return securityUser;
	}

}
