package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.*;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.model.Player;

import java.security.SecureRandom;
import java.util.*;

@RequiredArgsConstructor
public final class HoldemRoundSettingsManager implements RoundSettingsManager {

    private final SecureRandom random = new SecureRandom();
    private final List<CardType> allCards = CardType.getAllCardsAsList();
    private final List<Player> players;
    private final List<CardType> flop = setFlop();
    private final CardType tern = getRandomCard();
    private final CardType river = getRandomCard();
    private final String gameName;
    private final long bigBlindBet;
    private final long smallBlindBet;

    private void setAllPlayersGameName() {
        players.forEach(player -> player.setGameName(gameName));
    }


    public HoldemRoundSettingsDTO getPreflopSettings() {

        if (players.size() < 4) throw new RuntimeException("players size bust be more than 4");

        setAllPlayersGameName();
        dealCards();
        setButton();
        setSmallBlind();
        setBigBlind();
        setAllActivePlayers();

        return HoldemRoundSettingsDTO
                .builder()
                .gameName(gameName)
                .bank(bigBlindBet + smallBlindBet)
                .smallBlindBet(smallBlindBet)
                .bigBlindBet(bigBlindBet)
                .isAfk(false)
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .players(players)
                .stageType(StageType.PREFLOP)
                .history(setBlindsHistory())
                .lastBet(bigBlindBet)
                .build();
    }

    public HoldemRoundSettingsDTO getPostFlopSettings(long bank) {
        setAllActivePlayersTest();
        return HoldemRoundSettingsDTO
                .builder()
                .flop(flop)
                .gameName(gameName)
                .bank(bank)
                .smallBlindBet(smallBlindBet)
                .isAfk(false)
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .players(players)
                .history(new HashMap<>())
                .lastBet(0L)
                .stageType(StageType.FLOP)
                .build();
    }

    public HoldemRoundSettingsDTO getPostFlopSettingsWithTern(long bank) {
        setAllActivePlayersTest();
        return HoldemRoundSettingsDTO
                .builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .bank(bank)
                .smallBlindBet(smallBlindBet)
                .isAfk(false)
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .stageType(StageType.TERN)
                .history(new HashMap<>())
                .lastBet(0L)
                .players(players)
                .build();
    }


    public HoldemRoundSettingsDTO getPostFlopSettingsWithRiver(long bank) {
        setAllActivePlayersTest();
        return HoldemRoundSettingsDTO
                .builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .river(river)
                .lastBet(0L)
                .isAfk(false)
                .bank(bank)
                .stageType(StageType.RIVER)
                .smallBlindBet(smallBlindBet)
                .history(new HashMap<>())
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .players(players)
                .build();
    }


    private void dealCards() {
        players.forEach(
                player -> player.addCards(Arrays.asList(getRandomCard(), getRandomCard()))
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


    private void setButton() {
        final List<Player> players = this.players;
        final Optional<Player> foundButton = players
                .stream()
                .filter(Player::isButton)
                .findAny();

        if (foundButton.isEmpty()) {
            players.get(getRandomPlayer()).setButton();
            return;
        }

        final Player button = foundButton.get();
        button.removeRole();
        int indexOfButton = players.indexOf(button);

        if (indexOfButton + 1 < players.size()) {
            players.get(indexOfButton + 1).setButton();
            return;
        }

        indexOfButton = 0;
        players.get(indexOfButton).setButton();

    }

    private int getRandomPlayer() {
        return random.nextInt(players.size());
    }


    private void setSmallBlind() {
        clearRole(RoleType.SMALL_BLIND);
        final int indexOfButton = getIndexOfButton();
        int indexOfSmallBlind = 0;
        Player smallBlind;
        if (indexOfButton + 1 < players.size()) {
            indexOfSmallBlind = indexOfButton + 1;
        }
        smallBlind = players.get(indexOfSmallBlind);
        smallBlind.setRole(RoleType.SMALL_BLIND);
        removeChipsFromPlayer(smallBlind, smallBlindBet);
    }

    private void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final int indexOfSmallBlind = getIndexOfSmallBlind();
        Player bigBlind = getPlayer(indexOfSmallBlind);
        bigBlind.setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(bigBlind, bigBlindBet);
    }


    private void clearRole(RoleType roleType) {
        final Optional<Player> playerByRole = players.stream()
                .filter(player -> player.getRoleType() == roleType)
                .findAny();

        playerByRole.ifPresent(Player::removeRole);
    }


    private void removeChipsFromPlayer(Player player, long chips) {
        removeChips(player, chips);
    }


    private Optional<Player> getPlayerByRole(RoleType roleType) {
        return this.players.stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

    private Map<Player, List<CountAction>> setBlindsHistory() {
        final Map<Player, List<CountAction>> history = new HashMap<>();
        final Optional<Player> optionalSmallBlind = getPlayerByRole(RoleType.SMALL_BLIND);
        final Optional<Player> optionalBigBlind = getPlayerByRole(RoleType.BIG_BLIND);

        if (optionalSmallBlind.isEmpty() || optionalBigBlind.isEmpty()) {
            throw new RuntimeException("global error, cannot find blinds");
        }

        final Player bigBlind = optionalBigBlind.get();
        final Player smallBlind = optionalSmallBlind.get();
        final List<CountAction> forBigBlind = new ArrayList<>();
        forBigBlind.add(new Call(bigBlindBet));
        final List<CountAction> forSmallBlind = new ArrayList<>();
        forSmallBlind.add(new Call(smallBlindBet));
        history.put(bigBlind, forBigBlind);
        history.put(smallBlind, forSmallBlind);
        return history;
    }

    private int getIndexOfSmallBlind() {
        return players.indexOf(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow(() -> new RuntimeException("cannot find small blind")));
    }


    private int getIndexOfButton() {
        return players.indexOf(getPlayerByRole(RoleType.BUTTON).orElseThrow(() -> new RuntimeException("cannot find button")));
    }

    private List<CardType> setFlop() {
        final List<CardType> generatedFlop = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            generatedFlop.add(getRandomCard());
        }
        return generatedFlop;
    }

    private Player getPlayer(int indexOfSmallBlind) {
        int indexOfBigBlind = 0;
        Player bigBlind;
        if (indexOfSmallBlind + 1 < players.size()) {
            indexOfBigBlind = indexOfSmallBlind + 1;
        }
        bigBlind = players.get(indexOfBigBlind);
        return bigBlind;
    }

    private void removeChips(Player player, long chips) {
        player.removeChips(chips);
    }

    private void setAllActivePlayers() {
        this.players.forEach(player -> {
            if (!player.isSmallBlind() && !player.isBigBlind() && !player.isButton()) {
                player.setRole(RoleType.PLAYER);
            }
            player.setAction(new Wait());
        });
    }

    private void setAllActivePlayersTest() {
        this.players.forEach(player -> {
            if (player.getAction() != null && player.getRoleType() != null) {
                if (!(player.getAction() instanceof Fold) && player.getAction().getActionType() != ActionType.ALLIN && player.getStateType() == StateType.IN_GAME) {
                    player.setAction(new Wait());
                }
            }
        });
    }
}
