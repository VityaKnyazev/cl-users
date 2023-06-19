package ru.clevertec.ecl.knyazev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.clevertec.ecl.knyazev.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findRoleByName(String roleName);
	
}
