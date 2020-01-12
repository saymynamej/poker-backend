package ru.sm.poker.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class
ActionDTO {
    private String name;
    private String count;
    private String gameName;
}
