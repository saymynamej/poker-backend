package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import ru.sm.poker.enums.ErrorType;

@Builder
@Getter
public class ErrorDTO {
    private final ErrorType errorType;
}
