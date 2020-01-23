package ru.sm.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InformationType {
    CHANGED_STATE_TYPE_INFO("player with name =%s, changed state on = %s");

    private final String message;

}
