package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import ru.sm.poker.enums.MessageType;

@Builder
@Getter
public class ErrorDTO {
    private final MessageType messageType;
}
