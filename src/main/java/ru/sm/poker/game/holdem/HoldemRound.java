package ru.sm.poker.game.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.RoleType;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.BroadCastService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RequiredArgsConstructor
@Getter
public class HoldemRound implements Round {
    private final List<CardType> allCards = Arrays.stream(CardType.values()).collect(Collectors.toList());
    private final List<Player> players;
    private final String gameName;
    private final SecureRandom random = new SecureRandom();
    private final List<CardType> flop = new ArrayList<>();
    private final BroadCastService broadCastService;
    private long bank = 0;
    private Player activePlayer;
    private CardType tern;
    private CardType river;
    private Bet lastBet;
    private long bigBlindBet = 2;
    private long smallBlindBet = 1;


    @Override
    public void startRound() {
        setSettings();
        sendBank();
        setActionsPreflop();
        sendFlop();
        setActionPostFlop();
        sendTern();
        setActionPostFlop();
        sendRiver();
        setActionPostFlop();
    }


    private void setSettings(){
        dealCards();
        setFlop();
        setTern();
        setRiver();
        setButton();
        setSmallBlind();
        System.out.println(getBank());
        setBigBlind();
        setLastBet(bigBlindBet);
    }

    @Override
    public void stopRound() {

    }

    @Override
    public RoundSettings getRoundSettings() {
        return RoundSettings.builder()
                .flop(getFlop())
                .tern(getTern())
                .river(getRiver())
                .activePlayer(getActivePlayer())
                .button(getPlayerByRole(RoleType.BUTTON))
                .bank(getBank())
                .smallBlind(getPlayerByRole(RoleType.SMALL_BLIND))
                .bigBlind(getPlayerByRole(RoleType.BIG_BLIND))
                .players(getAllPlayers())
                .build();
    }


    private void setActionPostFlop() {
        final List<Player> playerInGame = getPlayerInGame();
        setAllWait(playerInGame);
        setActions(sortPostFlop(playerInGame));
    }

    private List<Player> getPlayerInGame() {
        return players
                .stream()
                .filter(player -> player.getAction() instanceof Call || player.getAction() instanceof Raise)
                .collect(Collectors.toList());
    }

    private List<Player> sortPostFlop(List<Player> players) {
        List<Player> sortedList = new ArrayList<>();
        final Optional<Player> smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND);
        smallBlind.ifPresent(sortedList::add);
        final Optional<Player> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
        bigBlind.ifPresent(sortedList::add);
        return sortedList;
    }


    private void sendFlop() {
        this.broadCastService.sendToAll(getFlop());
    }

    private void sendTern() {
        this.broadCastService.sendToAll(getTern());
    }

    private void sendRiver() {
        this.broadCastService.sendToAll(getTern());
    }

    private void sendBank() {
        this.broadCastService.sendBankToAll(getBank());
    }

    private void setAllWait(List<Player> players) {
        players.forEach(player -> player.setAction(new Wait("test")));
    }

    private void setActionsPreflop() {
        setActions(actionPreflopList());
    }

    private void setActions(List<Player> players) {
        for (Player player : players) {
            setActivePlayer(player);
            System.out.println("Ожидаем");
            broadCastService.sendToAll(players);
            waitPlayerAction(player);
            sendBank();
            removeActivePlayer(player);
        }
    }

    private void setActivePlayer(Player player) {
        this.activePlayer = player;
        this.activePlayer.setActive(true);
    }

    private void removeActivePlayer(Player player) {
        this.activePlayer.setActive(false);
    }

    private void setLastBet(long count) {
        this.lastBet = new Bet(count, getGameName());

    }

    private void waitPlayerAction(Player player) {
        while (true) {
            if (player.getAction()
                    .getClass() != Wait.class) {
                parseAction(player);
                break;
            }
        }
    }

    private void parseAction(Player player) {
        final Action action = player.getAction();
        if (action instanceof Call) {
            call(player, (Call) action);
        } else if (action instanceof Fold) {
            fold(player, (Fold) action);
        } else if (action instanceof Raise) {
            raise(player, (Raise) action);
        }
    }

    private void raise(Player player, Raise raise) {
        if (raise.getCount() < lastBet.getCount() * 2) {
            return;
        }
        removeChipsPlayerAndAddToBank(player, raise.getCount());
        setLastBet(raise.getCount());
    }

    private void fold(Player player, Fold fold) {
        player.setAction(fold);
    }

    private void call(Player player, Call call) {
        if (call.getCount() != lastBet.getCount()) {
            return;
        }
        removeChipsPlayerAndAddToBank(player, call.getCount());
    }


    private List<Player> actionPreflopList() {
        final List<Player> sortedPlayers = new ArrayList<>();
        final Player bigBlind = getPlayerByRole(RoleType.BIG_BLIND);
        final Player smallBlind = getPlayerByRole(RoleType.SMALL_BLIND);
        final Player button = getPlayerByRole(RoleType.BUTTON);
        final List<Player> afterBigBlind = takeAllAfterBigBlind();
        final List<Player> beforeButton = takeAllBeforeButton();
        sortedPlayers.addAll(afterBigBlind);
        sortedPlayers.addAll(beforeButton);
        sortedPlayers.add(button);
        sortedPlayers.add(smallBlind);
        sortedPlayers.add(bigBlind);
        return sortedPlayers
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }


    private List<Player> takeAllBeforeButton() {
        List<Player> players = getPlayers();
        List<Player> beforeButtonList = new ArrayList<>();
        for (Player player : players) {
            if (player.getRoleType() != RoleType.SMALL_BLIND
                    && player.getRoleType() != RoleType.BIG_BLIND && player.getRoleType() != RoleType.BUTTON)
                beforeButtonList.add(player);
            if (player.getRoleType() == RoleType.BUTTON) {
                break;
            }
        }
        return beforeButtonList;
    }

    private List<Player> takeAllAfterBigBlind() {
        List<Player> players = getPlayers();
        List<Player> afterBigBlindList = new ArrayList<>();
        boolean startCollect = false;
        for (Player player : players) {
            if (player.getRoleType() == RoleType.BIG_BLIND) {
                startCollect = true;
                continue;
            }
            if (startCollect
                    && player.getRoleType() != RoleType.BUTTON
                    && player.getRoleType() != RoleType.SMALL_BLIND) {
                afterBigBlindList.add(player);
            }
        }
        return afterBigBlindList;
    }


    private void setSmallBlind() {
        clearRole(RoleType.SMALL_BLIND);
        final int indexOfButton = getIndexOfButton();
        int indexOfSmallBlind = 0;
        Player smallBlind;
        if (indexOfButton + 1 >= getPlayers().size()) {
            smallBlind = getPlayers().get(indexOfSmallBlind);
        } else {
            indexOfSmallBlind = indexOfButton + 1;
            smallBlind = getPlayers().get(indexOfSmallBlind);
        }
        smallBlind.setRole(RoleType.SMALL_BLIND);
        removeChipsPlayerAndAddToBank(smallBlind, smallBlindBet);
    }

    private void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final int indexOfSmallBlind = getIndexOfSmallBlind();
        int indexOfBigBlind = 0;
        Player bigBlind;
        if (indexOfSmallBlind + 1 >= getPlayers().size()) {
            bigBlind = getPlayers().get(indexOfBigBlind);
        } else {
            indexOfBigBlind = indexOfSmallBlind + 1;
            bigBlind = getPlayers().get(indexOfBigBlind);
        }
        bigBlind.setRole(RoleType.BIG_BLIND);
        removeChipsPlayerAndAddToBank(bigBlind, bigBlindBet);
    }

    private int getIndexOfSmallBlind() {
        return getPlayers().indexOf(getPlayerByRole(RoleType.SMALL_BLIND));
    }

    private int getIndexOfBlindBlind() {
        return getPlayers().indexOf(getPlayerByRole(RoleType.BIG_BLIND));
    }

    private int getIndexOfButton() {
        return getPlayers().indexOf(getPlayerByRole(RoleType.BUTTON));
    }

    Player getPlayerByRole(RoleType roleType) {
        return this
                .getPlayers()
                .stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(format("Cannot find role:%s", roleType)));
    }

    Optional<Player> getPlayerByRole(List<Player> players, RoleType roleType) {
        return players
                .stream()
                .filter(player -> player.getRoleType() == roleType)
                .findFirst();
    }

    private void clearRole(RoleType roleType) {
        final Optional<Player> playerByRole = getPlayers()
                .stream()
                .filter(
                        player -> player.getRoleType() == roleType)
                .findAny();
        playerByRole.ifPresent(Player::removeRole);
    }

    private void setButton() {
        final List<Player> players = getPlayers();
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

    public List<Player> getAllPlayers() {
        return getPlayers();
    }

    private void setFlop() {
        for (int i = 0; i < 3; i++) {
            this.flop.add(getRandomCard());
        }
    }

    private void setTern() {
        this.tern = getRandomCard();
    }

    private void setRiver() {
        this.tern = getRandomCard();
    }

    private void dealCards() {
        getPlayers().forEach(player -> player.addCards(new CardType[]{getRandomCard(), getRandomCard()}));
    }

    private CardType getRandomCard() {
        final int randomIntCard = getRandomIntCard();
        final CardType cardType = allCards.get(randomIntCard);
        removeCard(randomIntCard);
        return cardType;
    }

    private void removeCard(int randomIntCard) {
        allCards.remove(randomIntCard);
    }

    private int getRandomIntCard() {
        return random.nextInt(allCards.size());
    }

    private int getRandomPlayer() {
        return random.nextInt(getPlayers().size());
    }

    private void removeChipsPlayerAndAddToBank(Player player, long chips) {
        removeChips(player, chips);
        addBank(chips);
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

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Bet getLastBet() {
        return lastBet;
    }


}
