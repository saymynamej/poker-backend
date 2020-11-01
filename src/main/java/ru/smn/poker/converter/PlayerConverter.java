package ru.smn.poker.converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smn.poker.dto.Bot;
import ru.smn.poker.dto.Card;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.PlayerType;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerConverter {

    public static List<PlayerEntity> toEntities(List<Player> players) {
        return players.stream()
                .map(PlayerConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> toEntities(List<Player> playerDTO, GameEntity gameEntity) {
        return playerDTO.stream()
                .map(player -> toEntity(player, gameEntity))
                .collect(Collectors.toList());
    }

    public static PlayerEntity toEntity(Player player, GameEntity gameEntity) {
        final PlayerEntity playerEntity = getPlayerEntity(player, gameEntity);
        playerEntity.setGame(gameEntity);
        return playerEntity;
    }

    public static PlayerEntity toEntity(Player player) {
        return getPlayerEntity(player);
    }

    private static PlayerEntity getPlayerEntity(Player player) {
        return getDefaultPlayerEntity(player);
    }

    private static PlayerEntity getDefaultPlayerEntity(Player player) {
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final PlayerEntity playerEntity = PlayerEntity.builder()
                .name(player.getName())
                .roleType(player.getRoleType())
                .playerType(player.getPlayerType())
                .password(bCryptPasswordEncoder.encode(player.getName()))
                .active(player.isActive())
                .stateType(player.getStateType())
                .enable(true)
                .timeBank(player.getTimeBank())
                .build();

        if (player.getId() != null) {
            playerEntity.setId(player.getId());
        }
        return playerEntity;
    }

    private static PlayerEntity getPlayerEntity(Player player, GameEntity gameEntity) {
        final PlayerEntity playerEntity = getDefaultPlayerEntity(player);
        if (player.getCards() != null) {
            playerEntity.setCards(player.getCards().stream()
                    .map(card -> CardEntity.builder()
                            .cardType(card.getCardType())
                            .id(card.getId())
                            .player(playerEntity)
                            .game(gameEntity)
                            .build())
                    .collect(Collectors.toList())
            );
        }
        final ChipsCountEntity chipsCountEntity = ChipsCountConverter.toEntity(player, gameEntity);
        playerEntity.setChipsCount(chipsCountEntity);
        return playerEntity;
    }

    public static List<Player> toDTOs(List<PlayerEntity> playerEntities) {
        return playerEntities.stream()
                .map(PlayerConverter::toDTO)
                .collect(Collectors.toList());
    }

    public static Player toDTO(PlayerEntity playerEntity) {
        if (playerEntity.getPlayerType() == PlayerType.BOT) {
            return Bot.builder()
                    .chipsCount(playerEntity.getChipsCount().getCount())
                    .cards(playerEntity.getCards().stream()
                            .map(cardEntity -> Card.builder()
                                    .cardType(cardEntity.getCardType())
                                    .id(cardEntity.getId())
                                    .build())
                            .collect(Collectors.toList()))
                    .id(playerEntity.getId())
                    .chipsId(playerEntity.getChipsCount().getId())
                    .gameName(playerEntity.getGame().getName())
                    .name(playerEntity.getName())
                    .build();
        }
        return Player.builder()
                .chipsCount(playerEntity.getChipsCount().getCount())
                .cards(playerEntity.getCards() == null ? null : playerEntity.getCards().stream()
                        .map(cardEntity -> Card.builder()
                                .id(cardEntity.getId())
                                .cardType(cardEntity.getCardType())
                                .build())
                        .collect(Collectors.toList()))
                .chipsId(playerEntity.getChipsCount().getId())
                .id(playerEntity.getId())
                .gameName(playerEntity.getGame().getName())
                .name(playerEntity.getName())
                .build();
    }
}
