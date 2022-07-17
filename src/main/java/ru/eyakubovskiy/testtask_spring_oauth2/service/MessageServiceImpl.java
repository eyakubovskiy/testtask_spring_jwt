package ru.eyakubovskiy.testtask_spring_oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageRequestDto;
import ru.eyakubovskiy.testtask_spring_oauth2.model.Message;
import ru.eyakubovskiy.testtask_spring_oauth2.model.User;
import ru.eyakubovskiy.testtask_spring_oauth2.repository.MessageRepository;
import ru.eyakubovskiy.testtask_spring_oauth2.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public void addMessage(MessageRequestDto messageDto) {

        User user = userRepository.findByName(messageDto.getName());

        if (user == null) {

            throw new UsernameNotFoundException("Пользователь \"" + messageDto.getName() + "\" не найден в БД");
        }

        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setUser(user);

        messageRepository.save(message);
    }

    @Override
    public List<MessageResponseDto> getLastMessages(int countMessages) {
        Pageable pageable = PageRequest.of(0, countMessages, Sort.Direction.DESC, "id");
        List<MessageResponseDto> messages = messageRepository.findAll(pageable).getContent()
                .stream().map(MessageResponseDto::fromMessage).collect(Collectors.toList());
        return messages;
    }
}
