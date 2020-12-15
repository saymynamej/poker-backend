package ru.smn.poker.game;

import lombok.RequiredArgsConstructor;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.action.holdem.Fold;
import ru.smn.poker.action.holdem.Wait;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.*;
import ru.smn.poker.util.PlayerUtil;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.smn.poker.util.HistoryUtil.unionHistory;

@RequiredArgsConstructor
public class HoldemRoundSettingsManager implements RoundSettingsManager {
    private final SecureRandom random = new SecureRandom();
    private final List<CardType> allCards = CardType.getAllCardsAsList();
    private final List<PlayerEntity> players;
    private final List<CardType> flop = setFlop();
    private final CardType tern = getRandomCard();
    private final CardType river = getRandomCard();
    private final String gameName;
    private final long bigBlindBet;
    private final long smallBlindBet;
    private final long gameId;

    @Override
    public RoundSettings getSettings(RoundSettings prevSettings) {
        if (prevSettings == null || prevSettings.isFinished()) {
            return getPreflopSettings();
        }
        switch (prevSettings.getStageType()) {
            case PREFLOP:
                return getPostFlopSettings(prevSettings.getBank(), prevSettings.getStageHistory(), prevSettings.getRoundId());
            case FLOP:
                return getPostFlopSettingsWithTern(prevSettings.getBank(), unionHistory(
                        prevSettings.getStageHistory(),
                        prevSettings.getFullHistory()),
                        prevSettings.getRoundId()
                );
            case TERN:
                return getPostFlopSettingsWithRiver(prevSettings.getBank(), unionHistory(
                        prevSettings.getStageHistory(),
                        prevSettings.getFullHistory()),
                        prevSettings.getRoundId()
                );
            default:
                throw new RuntimeException();
        }
    }

    private RoundSettings getPreflopSettings() {
        setAllPlayersGameName();
        dealCards();
        setButton();
        setSmallBlind();
        setBigBlind();
        setAllActivePlayers();
        return HoldemRoundSettings.builder()
                .gameName(gameName)
                .bank(bigBlindBet + smallBlindBet)
                .smallBlindBet(smallBlindBet)
                .bigBlindBet(bigBlindBet)
                .fullHistory(new HashMap<>())
                .isAfk(false)
                .gameId(gameId)
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElse(null))
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElse(null))
                .button(getPlayerByRole(RoleType.BUTTON).orElse(null))
                .players(players)
                .stageType(StageType.PREFLOP)
                .stageHistory(setBlindsHistory())
                .lastBet(bigBlindBet)
                .isFinished(false)
                .build();
    }


    private RoundSettings getPostFlopSettings(
            long bank,
            Map<PlayerEntity, List<Action>> prevHistory,
            Long roundId
    ) {
        setAllActivePlayersTest();
        return HoldemRoundSettings.builder()
                .flop(flop)
                .gameName(gameName)
                .bank(bank)
                .smallBlindBet(smallBlindBet)
                .fullHistory(prevHistory)
                .gameId(gameId)
                .isAfk(false)
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElse(null))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .players(players)
                .roundId(roundId)
                .stageHistory(new HashMap<>())
                .lastBet(0L)
                .isFinished(false)
                .stageType(StageType.FLOP)
                .build();
    }

    private RoundSettings getPostFlopSettingsWithTern(
            long bank,
            Map<PlayerEntity, List<Action>> fullHistory,
            Long roundId
    ) {
        setAllActivePlayersTest();
        return HoldemRoundSettings.builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .bank(bank)
                .smallBlindBet(smallBlindBet)
                .isAfk(false)
                .gameId(gameId)
                .fullHistory(fullHistory)
                .roundId(roundId)
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElse(null))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .stageType(StageType.TERN)
                .stageHistory(new HashMap<>())
                .lastBet(0L)
                .isFinished(false)
                .players(players)
                .build();
    }


    private RoundSettings getPostFlopSettingsWithRiver(
            long bank,
            Map<PlayerEntity, List<Action>> fullHistory,
            Long roundId
    ) {
        setAllActivePlayersTest();
        return HoldemRoundSettings.builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .river(river)
                .lastBet(0L)
                .isAfk(false)
                .gameId(gameId)
                .roundId(roundId)
                .bank(bank)
                .stageType(StageType.RIVER)
                .smallBlindBet(smallBlindBet)
                .fullHistory(fullHistory)
                .stageHistory(new HashMap<>())
                .bigBlindBet(bigBlindBet)
                .isFinished(false)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElse(null))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .players(players)
                .build();
    }

    private void setAllPlayersGameName() {
        players.forEach(player -> player.setGameName(gameName));
    }

    private void dealCards() {
        PlayerUtil.getPlayerWhichMayPlay(players).forEach(
                player -> player.addCards(
                        Arrays.asList(
                                CardEntity.builder()
                                        .cardType(getRandomCard())
                                        .player(player)
                                        .build(),
                                CardEntity.builder()
                                        .cardType(getRandomCard())
                                        .player(player)
                                        .build()
                        ))
        );
    }

    private CardType getRandomCard() {
        final int randomIntCard = getRandomIntCard();
        final CardType cardType = allCards.get(randomIntCard);
        removeCard(randomIntCard);
        return cardType;
    }

    private int getRandomIntCard() {
        return random.nextInt(allCards.size());
    }

    private void removeCard(int randomIntCard) {
        allCards.remove(randomIntCard);
    }


    private int getRandomPlayer() {
        return random.nextInt(players.size());
    }

    private int getIndexOfSmallBlind() {
        return players.indexOf(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow(() -> new RuntimeException("cannot find small blind")));
    }


    private int getIndexOfButton() {
        return players.indexOf(getPlayerByRole(RoleType.BUTTON).orElseThrow(() -> new RuntimeException("cannot find button")));
    }

    private List<CardType> setFlop() {
        return Stream.generate(this::getRandomCard).limit(3)
                .collect(Collectors.toList());
    }

    private PlayerEntity getPlayer(int indexOfSmallBlind) {
        int indexOfBigBlind = 0;
        PlayerEntity bigBlind;
        if (indexOfSmallBlind + 1 < players.size()) {
            indexOfBigBlind = indexOfSmallBlind + 1;
        }
        bigBlind = players.get(indexOfBigBlind);
        return bigBlind;
    }

    private void removeChips(PlayerEntity player, long chips) {
        player.removeChips(chips);
    }

    private void setAllActivePlayers() {
        this.players.forEach(player -> {
            if (player.hasNotSpecialRoles()) {
                player.setRole(RoleType.ORDINARY);
            }
            player.setAction(new Wait());
        });
    }


    protected void setSmallBlind() {
        clearRole(RoleType.SMALL_BLIND);
        final int indexOfButton = getIndexOfButton();
        int indexOfSmallBlind = 0;
        PlayerEntity smallBlind;
        if (indexOfButton + 1 < players.size()) {
            indexOfSmallBlind = indexOfButton + 1;
        }
        smallBlind = players.get(indexOfSmallBlind);
        smallBlind.setRole(RoleType.SMALL_BLIND);
        removeChipsFromPlayer(smallBlind, smallBlindBet);
    }

    protected void setButton() {
        final List<PlayerEntity> players = PlayerUtil.getPlayerWhichMayPlay(this.players);
        final Optional<PlayerEntity> foundButton = players
                .stream()
                .filter(PlayerEntity::isButton)
                .findAny();

        if (foundButton.isEmpty()) {
            players.get(getRandomPlayer()).setButton();
            return;
        }

        final PlayerEntity button = foundButton.get();
        button.removeRole();
        int indexOfButton = players.indexOf(button);

        if (indexOfButton + 1 < players.size()) {
            players.get(indexOfButton + 1).setButton();
            return;
        }

        indexOfButton = 0;
        players.get(indexOfButton).setButton();

    }


    protected void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final int indexOfSmallBlind = getIndexOfSmallBlind();
        PlayerEntity bigBlind = getPlayer(indexOfSmallBlind);
        bigBlind.setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(bigBlind, bigBlindBet);
    }

    protected void clearRole(RoleType roleType) {
        final Optional<PlayerEntity> playerByRole = players.stream()
                .filter(player -> player.getRoleType() == roleType)
                .findAny();

        playerByRole.ifPresent(PlayerEntity::removeRole);
    }

    protected void removeChipsFromPlayer(PlayerEntity player, long chips) {
        removeChips(player, chips);
    }

    protected Optional<PlayerEntity> getPlayerByRole(RoleType roleType) {
        return this.players.stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

    protected Map<PlayerEntity, List<Action>> setBlindsHistory() {
        final Map<PlayerEntity, List<Action>> history = new HashMap<>();
        final Optional<PlayerEntity> optionalSmallBlind = getPlayerByRole(RoleType.SMALL_BLIND);
        final Optional<PlayerEntity> optionalBigBlind = getPlayerByRole(RoleType.BIG_BLIND);

        if (optionalSmallBlind.isEmpty() || optionalBigBlind.isEmpty()) {
            throw new RuntimeException("global error, cannot find blinds");
        }

        final PlayerEntity bigBlind = optionalBigBlind.get();
        final PlayerEntity smallBlind = optionalSmallBlind.get();
        final List<Action> forBigBlind = new ArrayList<>();
        forBigBlind.add(new Call(bigBlindBet));
        final List<Action> forSmallBlind = new ArrayList<>();
        forSmallBlind.add(new Call(smallBlindBet));
        history.put(bigBlind, forBigBlind);
        history.put(smallBlind, forSmallBlind);
        return history;
    }

    protected List<PlayerEntity> getPlayers() {
        return players;
    }

    protected long getBigBlindBet() {
        return bigBlindBet;
    }

    protected long getSmallBlindBet() {
        return smallBlindBet;
    }

    protected void setAllActivePlayersTest() {
        this.players.forEach(player -> {
            if (player.getAction() != null && player.getRoleType() != null) {
                if (!(player.getAction() instanceof Fold) && player.getAction().getActionType() != ActionType.ALLIN && player.getStateType() == StateType.IN_GAME) {
                    player.setAction(new Wait());
                }
            }
        });
    }
}
