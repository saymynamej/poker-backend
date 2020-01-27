package ru.sm.poker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType {
    SUCCESS_JOIN_IN_QUEUE("player = %s, joined in queue"),
    SUCCESS_JOIN_IN_GAME("player = %s, joined in queue"),
    SUCCESS_CONNECTED_TO_SERVER("player = %s, connected"),
    QUEUE_ERROR("oops, player with name = %s want to break our rules, he is not active now"),
    FIND_PLAYER_ERROR("oops, cannot find player with name = %s"),
    GAME_NOT_FOUND("game was not found"),
    PLAYER_ALREADY_EXIST("player already exist"),
    ONLY_ONE_TABLE_MESSAGE("our service can service only one table, sorry"),
    SETTINGS_NOT_FOUND("cannot find settings for player = %s");

    @Getter
    private final String message;


}
