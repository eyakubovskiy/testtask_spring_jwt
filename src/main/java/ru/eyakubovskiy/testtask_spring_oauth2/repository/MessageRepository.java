package ru.eyakubovskiy.testtask_spring_oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eyakubovskiy.testtask_spring_oauth2.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
