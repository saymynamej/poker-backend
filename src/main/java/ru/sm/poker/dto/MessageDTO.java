package ru.sm.poker.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class MessageDTO {
    private String name;
    private String count;
    private String gameName;
}