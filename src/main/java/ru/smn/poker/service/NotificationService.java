package ru.smn.poker.service;

public interface NotificationService {

    void sendGameInformationToUser(String userName, Object message);

    void sendSystemMessageToUser(String userName, Object message);

    void sendSystemMessageToAll(Object message);

    void sendGameInformationToAll(Object games);

    void sendGamesInformationToAll(Object games);
}
