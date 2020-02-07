package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.*;
import ru.sm.poker.game.RoundSettingsManager;

import java.security.SecureRandom;
import java.util.*;

@RequiredArgsConstructor
public final class HoldemRoundSettingsManager implements RoundSettingsManager {

    private final SecureRandom random = new SecureRandom();
    private final List<CardType> allCards = CardType.getAllCardsAsList();
    private final List<PlayerDTO> players;
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
                .fullHistory(new HashMap<>())
                .isAfk(false)
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .players(players)
                .stageType(StageType.PREFLOP)
                .stageHistory(setBlindsHistory())
                .lastBet(bigBlindBet)
                .build();
    }

    public HoldemRoundSettingsDTO getPostFlopSettings(long bank, Map<PlayerDTO, List<CountAction>> prevHistory) {
        setAllActivePlayersTest();
        return HoldemRoundSettingsDTO
                .builder()
                .flop(flop)
                .gameName(gameName)
                .bank(bank)
                .smallBlindBet(smallBlindBet)
                .fullHistory(new HashMap<>(prevHistory))
                .isAfk(false)
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .players(players)
                .stageHistory(new HashMap<>())
                .lastBet(0L)
                .stageType(StageType.FLOP)
                .build();
    }

    public HoldemRoundSettingsDTO getPostFlopSettingsWithTern(long bank, Map<PlayerDTO, List<CountAction>> prevHistory) {
        setAllActivePlayersTest();
        return HoldemRoundSettingsDTO
                .builder()
                .flop(flop)
                .tern(tern)
                .gameName(gameName)
                .bank(bank)
                .smallBlindBet(smallBlindBet)
                .isAfk(false)
                .fullHistory(new HashMap<>(prevHistory))
                .bigBlindBet(bigBlindBet)
                .button(getPlayerByRole(RoleType.BUTTON).orElseThrow())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND).orElseThrow())
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND).orElseThrow())
                .stageType(StageType.TERN)
                .stageHistory(new HashMap<>())
                .lastBet(0L)
                .players(players)
                .build();
    }


    public HoldemRoundSettingsDTO getPostFlopSettingsWithRiver(long bank, Map<PlayerDTO, List<CountAction>> prevHistory) {
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
                .fullHistory(new HashMap<>(prevHistory))
                .stageHistory(new HashMap<>())
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
        final List<PlayerDTO> playerDTOS = this.players;
        final Optional<PlayerDTO> foundButton = playerDTOS
                .stream()
                .filter(PlayerDTO::isButton)
                .findAny();

        if (foundButton.isEmpty()) {
            playerDTOS.get(getRandomPlayer()).setButton();
            return;
        }

        final PlayerDTO button = foundButton.get();
        button.removeRole();
        int indexOfButton = playerDTOS.indexOf(button);

        if (indexOfButton + 1 < playerDTOS.size()) {
            playerDTOS.get(indexOfButton + 1).setButton();
            return;
        }

        indexOfButton = 0;
        playerDTOS.get(indexOfButton).setButton();

    }

    private int getRandomPlayer() {
        return random.nextInt(players.size());
    }

    private void setSmallBlind() {
        clearRole(RoleType.SMALL_BLIND);
        final int indexOfButton = getIndexOfButton();
        int indexOfSmallBlind = 0;
        PlayerDTO smallBlind;
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
        PlayerDTO bigBlind = getPlayer(indexOfSmallBlind);
        bigBlind.setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(bigBlind, bigBlindBet);
    }

    private void clearRole(RoleType roleType) {
        final Optional<PlayerDTO> playerByRole = players.stream()
                .filter(player -> player.getRoleType() == roleType)
                .findAny();

        playerByRole.ifPresent(PlayerDTO::removeRole);
    }

    private void removeChipsFromPlayer(PlayerDTO playerDTO, long chips) {
        removeChips(playerDTO, chips);
    }

    private Optional<PlayerDTO> getPlayerByRole(RoleType roleType) {
        return this.players.stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

    private Map<PlayerDTO, List<CountAction>> setBlindsHistory() {
        final Map<PlayerDTO, List<CountAction>> history = new HashMap<>();
        final Optional<PlayerDTO> optionalSmallBlind = getPlayerByRole(RoleType.SMALL_BLIND);
        final Optional<PlayerDTO> optionalBigBlind = getPlayerByRole(RoleType.BIG_BLIND);

        if (optionalSmallBlind.isEmpty() || optionalBigBlind.isEmpty()) {
            throw new RuntimeException("global error, cannot find blinds");
        }

        final PlayerDTO bigBlind = optionalBigBlind.get();
        final PlayerDTO smallBlind = optionalSmallBlind.get();
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

    private PlayerDTO getPlayer(int indexOfSmallBlind) {
        int indexOfBigBlind = 0;
        PlayerDTO bigBlind;
        if (indexOfSmallBlind + 1 < players.size()) {
            indexOfBigBlind = indexOfSmallBlind + 1;
        }
        bigBlind = players.get(indexOfBigBlind);
        return bigBlind;
    }

    private void removeChips(PlayerDTO playerDTO, long chips) {
        playerDTO.removeChips(chips);
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
