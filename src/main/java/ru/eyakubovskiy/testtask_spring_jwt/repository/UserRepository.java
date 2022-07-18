package ru.eyakubovskiy.testtask_spring_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eyakubovskiy.testtask_spring_jwt.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
