package ru.sm.poker.service;

public interface NotificationService {
    void sendToUser(String userName, Object message);

    void sendErrorToUser(String userName, Object message);

    void sendErrorToAll(Object message);

    void sendToAll(Object games);

    void sendGamesInformationToAll(Object games);
}
