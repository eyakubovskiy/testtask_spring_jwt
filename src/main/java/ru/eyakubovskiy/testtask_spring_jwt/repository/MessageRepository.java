package ru.eyakubovskiy.testtask_spring_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eyakubovskiy.testtask_spring_jwt.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
