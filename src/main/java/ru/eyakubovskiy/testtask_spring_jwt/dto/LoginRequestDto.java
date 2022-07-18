package ru.eyakubovskiy.testtask_spring_jwt.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record LoginRequestDto(String name, String password) {
}
