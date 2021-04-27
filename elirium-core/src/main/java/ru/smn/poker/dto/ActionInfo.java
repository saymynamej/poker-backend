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
public class ActionInfo {
    private String count;
    private ActionType actionType;
}
