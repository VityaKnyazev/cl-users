package ru.clevertec.ecl.knyazev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.clevertec.ecl.knyazev.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String name);

}
