package ru.smn.poker.dto;

import lombok.Builder;
import lombok.Getter;
import ru.smn.poker.enums.MessageType;

@Builder
@Getter
public class Error {
    private final MessageType messageType;
}
