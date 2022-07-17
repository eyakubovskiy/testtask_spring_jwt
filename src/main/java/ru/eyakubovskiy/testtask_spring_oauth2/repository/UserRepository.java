package ru.eyakubovskiy.testtask_spring_oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eyakubovskiy.testtask_spring_oauth2.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
