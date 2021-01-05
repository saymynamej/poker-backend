package ru.smn.poker.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.smn.poker.enums.ActionType;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class AdminActionInfo {
    private String count;
    private String name;
    private ActionType actionType;
}
