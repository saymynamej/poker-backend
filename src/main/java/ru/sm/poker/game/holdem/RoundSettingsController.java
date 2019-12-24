package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.action.Wait;
import ru.sm.poker.util.SortUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RequiredArgsConstructor
final class RoundSettingsController {

    private final SecureRandom random = new SecureRandom();
    private final List<CardType> allCards = CardType.getAllCardsAsList();
    private final List<Player> players;
    private final List<CardType> flop = setFlop();
    private final CardType tern = setTern();
    private final CardType river = setRiver();
    private final String gameName;
    private final long bigBlindBet;
    private final long smallBlindBet;

    @Setter
    private long bank = 0;


    public RoundSettingsDTO getPreflopSettings() {
        setAllActivePlayers();
        dealCards();
        setButton();
        setSmallBlind();
        setBigBlind();
        SortUtil.sortPreflop(players);

        return RoundSettingsDTO
                .builder()
                .gameName(gameName)
                .bank(bank)
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND))
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND))
                .button(getPlayerByRole(RoleType.BUTTON))
                .players(players)
                .lastBet(bigBlindBet)
                .build();
    }


    public RoundSettingsDTO getPostFlopSettings() {
        SortUtil.sortPostflop(players);

        return RoundSettingsDTO
                .builder()
                .flop(flop)
                .gameName(gameName)
                .button(getPlayerByRole(RoleType.BUTTON))
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND))
                .players(players)
                .build();
    }

    public RoundSettingsDTO getPostFlopSettingsWithTern() {
        SortUtil.sortPostflop(players);

        return RoundSettingsDTO
                .builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .button(getPlayerByRole(RoleType.BUTTON))
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND))
                .players(players)
                .build();
    }


    public RoundSettingsDTO getPostFlopSettingsWithRiver() {
        SortUtil.sortPostflop(players);

        return RoundSettingsDTO
                .builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .river(river)
                .button(getPlayerByRole(RoleType.BUTTON))
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND))
                .players(players)
                .build();
    }


    private void dealCards() {
        players.forEach(player -> player.addCards(Arrays.asList(getRandomCard(), getRandomCard())));
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


    private CardType setTern() {
        return getRandomCard();
    }

    private CardType setRiver() {
        return getRandomCard();
    }


    private void setButton() {
        final List<Player> players = this.players;
        final Optional<Player> foundButton = players
                .stream()
                .filter(Player::isButton)
                .findAny();

        if (foundButton.isPresent()) {
            final Player button = foundButton.get();
            button.removeRole();
            int indexOfButton = players.indexOf(button);
            if (indexOfButton + 1 >= players.size()) {
                indexOfButton = 0;
                players.get(indexOfButton).setButton();
            } else {
                players.get(indexOfButton + 1).setButton();
            }
        } else {
            players.get(getRandomPlayer()).setButton();
        }
    }

    private int getRandomPlayer() {
        return random.nextInt(players.size());
    }


    private void setSmallBlind() {
        clearRole(RoleType.SMALL_BLIND);
        final int indexOfButton = getIndexOfButton();
        int indexOfSmallBlind = 0;
        Player smallBlind;
        if (indexOfButton + 1 >= players.size()) {
            smallBlind = players.get(indexOfSmallBlind);
        } else {
            indexOfSmallBlind = indexOfButton + 1;
            smallBlind = players.get(indexOfSmallBlind);
        }
        smallBlind.setRole(RoleType.SMALL_BLIND);
        removeChipsPlayerAndAddToBank(smallBlind, smallBlindBet);
    }

    private void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final int indexOfSmallBlind = getIndexOfSmallBlind();
        Player bigBlind = getPlayer(indexOfSmallBlind);
        bigBlind.setRole(RoleType.BIG_BLIND);
        removeChipsPlayerAndAddToBank(bigBlind, bigBlindBet);
    }


    private void clearRole(RoleType roleType) {
        final Optional<Player> playerByRole = players
                .stream()
                .filter(
                        player -> player.getRoleType() == roleType)
                .findAny();
        playerByRole.ifPresent(Player::removeRole);
    }


    private void removeChipsPlayerAndAddToBank(Player player, long chips) {
        removeChips(player, chips);
        addBank(chips);
    }


    private Player getPlayerByRole(RoleType roleType) {
        return this
                .players
                .stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(format("Cannot find role:%s", roleType)));
    }

    private int getIndexOfSmallBlind() {
        return players.indexOf(getPlayerByRole(RoleType.SMALL_BLIND));
    }

    private int getIndexOfBigBlind() {
        return players.indexOf(getPlayerByRole(RoleType.BIG_BLIND));
    }

    private int getIndexOfButton() {
        return players.indexOf(getPlayerByRole(RoleType.BUTTON));
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
        if (indexOfSmallBlind + 1 >= players.size()) {
            bigBlind = players.get(indexOfBigBlind);
        } else {
            indexOfBigBlind = indexOfSmallBlind + 1;
            bigBlind = players.get(indexOfBigBlind);
        }
        return bigBlind;
    }

    private List<Player> getPlayersInGame() {
        return this.players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME)
                .collect(Collectors.toList());
    }

    private void removeChips(Player player, long chips) {
        player.removeChips(chips);
    }

    private void addChips(Player player, long chips) {
        player.addChips(chips);
    }

    private void addBank(long count) {
        bank += count;
    }

    private void setAllActivePlayers() {
        this.players.forEach(player -> player.setAction(new Wait(gameName)));
        this.players.forEach(player -> player.setStateType(StateType.IN_GAME));
    }
}