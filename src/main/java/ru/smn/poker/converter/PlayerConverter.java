package ru.smn.poker.converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smn.poker.dto.Card;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.*;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerConverter {

    public static List<PlayerEntity> toEntities(List<Player> players) {
        return players.stream()
                .map(PlayerConverter::toEntity)
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> toEntities(List<Player> playerDTO, GameEntity gameEntity) {
//        return playerDTO.stream()
//                .map(player -> toEntity(player, gameEntity))
//                .collect(Collectors.toList());
        return null;
    }

    public static PlayerEntity toEntity(Player player, TableEntity gameEntity) {
//        final PlayerEntity playerEntity = getPlayerEntity(player, gameEntity);
//        playerEntity.setGame(gameEntity);
//        return playerEntity;
        return null;
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
                .settings(PlayerSettingsEntity.builder()
                        .roleType(player.getRoleType())
                        .playerType(player.getPlayerType())
                        .active(player.isActive())
                        .stateType(player.getStateType())
                        .timeBank(player.getTimeBank())
                        .build())
                .password(bCryptPasswordEncoder.encode(player.getName()))
                .enable(true)
                .build();

        if (player.getId() != null) {
            playerEntity.setId(player.getId());
        }
        return playerEntity;
    }

    private static PlayerEntity getPlayerEntity(Player player, GameEntity gameEntity) {
        final PlayerEntity playerEntity = getDefaultPlayerEntity(player);
        if (player.getCards() != null) {
            playerEntity.addCards(player.getCards().stream()
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
        if (playerEntity == null) {
            return null;
        }
        return Player.builder()
                .chipsCount(playerEntity.getSettings().getChipsCount().getCount())
                .cards(playerEntity.getSettings().getCards() == null ? null : playerEntity.getSettings().getCards().stream()
                        .map(cardEntity -> Card.builder()
                                .id(cardEntity.getId())
                                .cardType(cardEntity.getCardType())
                                .build())
                        .collect(Collectors.toList()))
                .chipsId(playerEntity.getSettings().getChipsCount().getId())
                .id(playerEntity.getId())
                .timeBank(playerEntity.getTimeBank())
                .active(playerEntity.getSettings().isActive())
                .roleType(playerEntity.getSettings().getRoleType())
                .action(playerEntity.getAction())
                .name(playerEntity.getName())
                .build();
    }
}
