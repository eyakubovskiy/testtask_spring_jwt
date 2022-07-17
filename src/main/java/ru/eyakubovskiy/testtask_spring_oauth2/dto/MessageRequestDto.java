package ru.eyakubovskiy.testtask_spring_oauth2.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import ru.eyakubovskiy.testtask_spring_oauth2.model.Message;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record MessageRequestDto(String name, String message) {
}
