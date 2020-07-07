package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import ru.sm.poker.enums.MessageType;

@Builder
@Getter
public class Error {
    private final MessageType messageType;
}
