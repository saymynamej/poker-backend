package ru.sm.poker.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Message {
    private String name;
    private String count;
}
