package ru.clevertec.ecl.knyazev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.clevertec.ecl.knyazev.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByName(String name);

}
