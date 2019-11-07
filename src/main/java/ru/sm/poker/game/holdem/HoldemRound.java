package ru.sm.poker.game.holdem;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.PlayerType;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.Bet;
import ru.sm.poker.model.action.Wait;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class HoldemRound extends Round {

    private final List<CardType> allCards = Arrays.stream(CardType.values()).collect(Collectors.toList());
    private final ExecutorService executors = Executors.newFixedThreadPool(getPlayers().size());
    private final Random random = new Random();
    private final List<CardType> flop = new ArrayList<>();
    private final long bigBlindBet;
    private final long smallBlindBet;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private long bank = 0;
    private Player activePlayer;
    private CardType tern;
    private CardType river;
    private Bet lastBet;

    HoldemRound(List<Player> players,
                SimpMessagingTemplate simpMessagingTemplate, long smallBlindBet,
                long bigBlindBet) {
        super(players);
        this.bigBlindBet = bigBlindBet;
        this.smallBlindBet = smallBlindBet;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    protected void startRound() {
        dealCards();
        setFlop();
        setTern();
        setRiver();
        setButton();
        setSmallBlind();
        setBigBlind();
        setLastBet(bigBlindBet);
        setActionsPreflop();
    }

    private void setActionsPreflop() {
        final List<Player> players = actionPreflopList();
        for (Player player : players) {
            this.activePlayer = player;
            simpMessagingTemplate.convertAndSend("/poker/game/start", getPlayers());
            waitPlayerAction(player);
        }
    }

    private void setLastBet(long count){
        this.lastBet = new Bet(count);
    }

    private void waitPlayerAction(Player player){
        while (true) {
            if (player.getAction().getClass() != Wait.class) {
                final Action action = player.getAction();
                if (action instanceof Bet) {
                    final Bet bet = (Bet) action;
                    removeChipsPlayerAndAddToBank(player, bet.getCount());
                }
                player.setAction(null);
                break;
            }
        }
    }



    private List<Player> actionPreflopList() {
        final List<Player> sortedPlayers = new ArrayList<>();
        final Player bigBlind = getPlayerByRole(PlayerType.BIG_BLIND);
        final Player smallBlind = getPlayerByRole(PlayerType.SMALL_BLIND);
        final Player button = getPlayerByRole(PlayerType.BUTTON);
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
            if (player.getPlayerType() != PlayerType.SMALL_BLIND
                    && player.getPlayerType() != PlayerType.BIG_BLIND && player.getPlayerType() != PlayerType.BUTTON)
                beforeButtonList.add(player);
            if (player.getPlayerType() == PlayerType.BUTTON) {
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
            if (player.getPlayerType() == PlayerType.BIG_BLIND) {
                startCollect = true;
                continue;
            }
            if (startCollect
                    && player.getPlayerType() != PlayerType.BUTTON
                    && player.getPlayerType() != PlayerType.SMALL_BLIND) {
                afterBigBlindList.add(player);
            }
        }
        return afterBigBlindList;
    }


    private void setSmallBlind() {
        clearRole(PlayerType.SMALL_BLIND);
        final int indexOfButton = getIndexOfButton();
        int indexOfSmallBlind = 0;
        Player smallBlind;
        if (indexOfButton + 1 >= getPlayers().size()) {
            smallBlind = getPlayers().get(indexOfSmallBlind);
        } else {
            indexOfSmallBlind = indexOfButton + 1;
            smallBlind = getPlayers().get(indexOfSmallBlind);
        }
        smallBlind.setRole(PlayerType.SMALL_BLIND);
        removeChipsPlayerAndAddToBank(smallBlind, smallBlindBet);
    }

    private void setBigBlind() {
        clearRole(PlayerType.BIG_BLIND);
        final int indexOfSmallBlind = getIndexOfSmallBlind();
        int indexOfBigBlind = 0;
        Player bigBlind;
        if (indexOfSmallBlind + 1 >= getPlayers().size()) {
            bigBlind = getPlayers().get(indexOfBigBlind);
        } else {
            indexOfBigBlind = indexOfSmallBlind + 1;
            bigBlind = getPlayers().get(indexOfBigBlind);
        }
        bigBlind.setRole(PlayerType.BIG_BLIND);
        removeChipsPlayerAndAddToBank(bigBlind, bigBlindBet);
    }

    private int getIndexOfSmallBlind() {
        return getPlayers().indexOf(getPlayerByRole(PlayerType.SMALL_BLIND));
    }

    private int getIndexOfBlindBlind() {
        return getPlayers().indexOf(getPlayerByRole(PlayerType.BIG_BLIND));
    }

    private int getIndexOfButton() {
        return getPlayers().indexOf(getPlayerByRole(PlayerType.BUTTON));
    }

    private Player getPlayerByRole(PlayerType playerType) {
        return this
                .getPlayers()
                .stream()
                .filter(player -> player.getPlayerType() == playerType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(format("Cannot find role:%s", playerType)));
    }

    private void clearRole(PlayerType playerType) {
        final Optional<Player> playerByRole = getPlayers()
                .stream()
                .filter(
                        player -> player.getPlayerType() == playerType)
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
