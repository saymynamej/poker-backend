package ru.sm.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorType {
    SUCCESS_JOIN_IN_QUEUE("player = %s, joined in queue"),
    SUCCESS_JOIN_IN_GAME("player = %s, joined in queue"),
    QUEUE_ERROR("oops, player with name = %s want to break our rules, he is not active now"),
    FIND_PLAYER_ERROR("oops, cannot find player with name = %s"),
    GAME_NOT_FOUND("game was not found"),
    PLAYER_ALREADY_EXIST("Player already exist"),
    SETTINGS_NOT_FOUND("cannot find settings for player = %s");

    @Getter
    private final String message;


}
