package ru.smn.poker.game;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.*;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.*;
import ru.smn.poker.service.HandIdGenerator;
import ru.smn.poker.service.common.TableService;
import ru.smn.poker.util.PlayerUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullTableSettingManager implements TableSettingsManager {
    private final Random random;
    private final List<PlayerEntity> players;
    private final GameSettings gameSettings;
    private final TableSettings tableSettings;
    private final HandIdGenerator handIdGenerator;

    private StageType stageType = StageType.PREFLOP;
    private List<CardType> allCards;
    private boolean needRestore;
    private final TableService tableService;

    public FullTableSettingManager(
            Random random,
            List<PlayerEntity> players,
            GameSettings gameSettings,
            TableSettings tableSettings,
            HandIdGenerator handIdGenerator,
            TableService tableService
    ) {
        this.random = random;
        this.players = players;
        this.gameSettings = gameSettings;
        this.tableSettings = tableSettings;
        this.handIdGenerator = handIdGenerator;
        this.tableService = tableService;
        //TODO
        this.needRestore = tableSettings.getFullHistory() != null && !tableSettings.getFullHistory().isEmpty();
        if (this.needRestore) {
            stageType = tableSettings.getStageType();
        }
    }

    @Override
    public TableSettings getSettings() {
        if (needRestore) {
            needRestore = false;
            return tableSettings;
        }
        final TableSettings currentSettings = stageType.getCurrentSettings(this);
        this.stageType = stageType.getNextStage();
        return currentSettings;
    }

    @Override
    public TableSettings getPreflopSettings() {
        init();
        this.tableSettings.setFullHistory(getBlindsHistory());
        this.tableSettings.setStageHistory(getBlindsHistory());
        this.tableSettings.setStageType(StageType.PREFLOP);
        this.tableSettings.setTableName(gameSettings.getTableName());
        this.tableSettings.setBank(gameSettings.getStartBigBlindBet() + gameSettings.getStartSmallBlindBet());
        this.tableSettings.setSmallBlindBet(gameSettings.getStartSmallBlindBet());
        this.tableSettings.setBigBlindBet(gameSettings.getStartBigBlindBet());
        this.tableSettings.setAfk(false);

        final PlayerEntity bigBlind = getPlayerByRole(RoleType.BIG_BLIND).orElseThrow();
        final PlayerEntity smallBlind = getPlayerByRole(RoleType.SMALL_BLIND).orElse(null);
        final PlayerEntity button = getPlayerByRole(RoleType.BUTTON).orElseThrow(null);

        this.tableSettings.setBigBlind(bigBlind);
        this.tableSettings.setSmallBlind(smallBlind);
        this.tableSettings.setButton(button);

        this.tableSettings.setPlayers(players);
        this.tableSettings.setLastBet(gameSettings.getStartBigBlindBet());
        this.tableSettings.setTableId(gameSettings.getTableId());
        this.tableSettings.setFinished(false);
        this.tableSettings.setHandId(handIdGenerator.generate(gameSettings.getTableId()));

        this.tableService.updateHand(this.tableSettings);
        this.tableService.saveCards(players);

        addBlindAction(smallBlind == null ? button : smallBlind, new SmallBlindBet(gameSettings.getStartSmallBlindBet()));
        addBlindAction(bigBlind, new BigBlindBet(gameSettings.getStartBigBlindBet()));

        return this.tableSettings;
    }

    private void addBlindAction(PlayerEntity player, Action action) {
        player.addAction(
                action,
                tableSettings.getHandId()
        );
        this.tableService.updateInfo(player);
    }

    @Override
    public TableSettings getFlopSettings() {
        setWaitForAllPlayers();
        this.tableSettings.setFlop(getFlop());
        this.tableSettings.setStageHistory(new HashMap<>());
        this.tableSettings.setLastBet(0L);
        this.tableSettings.setStageType(StageType.FLOP);
        this.tableService.updateHand(this.tableSettings);
        return this.tableSettings;
    }

    @Override
    public TableSettings getTernSettings() {
        setWaitForAllPlayers();
        this.tableSettings.setTern(getRandomCard());
        this.tableSettings.setStageHistory(new HashMap<>());
        this.tableSettings.setLastBet(0L);
        this.tableSettings.setStageType(StageType.TERN);
        this.tableService.updateHand(this.tableSettings);
        return tableSettings;
    }

    @Override
    public TableSettings getRiverSettings() {
        setWaitForAllPlayers();
        this.tableSettings.setRiver(getRandomCard());
        this.tableSettings.setStageHistory(new HashMap<>());
        this.tableSettings.setLastBet(0L);
        this.tableSettings.setStageType(StageType.RIVER);
        this.tableService.updateHand(this.tableSettings);
        return tableSettings;
    }

    private void dealCards() {
        PlayerUtil.getPlayerWhichMayPlay(players).forEach(
                player -> player.getTableSettings().addCards(
                        Arrays.asList(
                                CardEntity.builder()
                                        .cardType(getRandomCard())
                                        .settings(player.getTableSettings())
                                        .player(player)
                                        .build(),
                                CardEntity.builder()
                                        .cardType(getRandomCard())
                                        .settings(player.getTableSettings())
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

    private List<CardType> getFlop() {
        return Stream.generate(this::getRandomCard)
                .limit(3)
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
        player.getTableSettings().removeChips(chips);
    }

    private void setAllActivePlayers() {
        this.players.forEach(player -> {
            if (player.getTableSettings().hasNotSpecialRoles()) {
                player.getTableSettings().setRole(RoleType.ORDINARY);
            }
            player.getTableSettings().setAction(new Wait());
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
        smallBlind.getTableSettings().setRole(RoleType.SMALL_BLIND);
        removeChipsFromPlayer(smallBlind, gameSettings.getStartSmallBlindBet());
    }

    protected void setButton() {
        final List<PlayerEntity> players = PlayerUtil.getPlayerWhichMayPlay(this.players);
        final Optional<PlayerEntity> foundButton = players
                .stream()
                .filter(playerEntity -> playerEntity.getTableSettings().isButton())
                .findAny();

        if (foundButton.isEmpty()) {
            players.get(getRandomPlayer()).getTableSettings().setButton();
            return;
        }

        final PlayerEntity button = foundButton.get();
        button.getTableSettings().removeRole();
        int indexOfButton = players.indexOf(button);

        if (indexOfButton + 1 < players.size()) {
            players.get(indexOfButton + 1).getTableSettings().setButton();
            return;
        }

        indexOfButton = 0;
        players.get(indexOfButton).getTableSettings().setButton();

    }

    protected void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final int indexOfSmallBlind = getIndexOfSmallBlind();
        PlayerEntity bigBlind = getPlayer(indexOfSmallBlind);
        bigBlind.getTableSettings().setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(bigBlind, gameSettings.getStartBigBlindBet());
    }

    protected void clearRole(RoleType roleType) {
        final Optional<PlayerEntity> playerByRole = players.stream()
                .filter(player -> player.getTableSettings().getRoleType() == roleType)
                .findAny();

        playerByRole.ifPresent(playerEntity -> playerEntity.getTableSettings().removeRole());
    }

    protected void removeChipsFromPlayer(PlayerEntity player, long chips) {
        removeChips(player, chips);
    }

    protected Optional<PlayerEntity> getPlayerByRole(RoleType roleType) {
        return this.players.stream()
                .filter(player -> player.getTableSettings().getRoleType() == roleType)
                .findFirst();
    }

    protected Map<PlayerEntity, List<Action>> getBlindsHistory() {
        final Map<PlayerEntity, List<Action>> history = new HashMap<>();
        final Optional<PlayerEntity> optionalSmallBlind = getPlayerByRole(RoleType.SMALL_BLIND);
        final Optional<PlayerEntity> optionalBigBlind = getPlayerByRole(RoleType.BIG_BLIND);

        if (optionalSmallBlind.isEmpty() || optionalBigBlind.isEmpty()) {
            throw new RuntimeException("global error, cannot find blinds");
        }

        final PlayerEntity bigBlind = optionalBigBlind.get();
        final PlayerEntity smallBlind = optionalSmallBlind.get();
        final List<Action> forBigBlind = new ArrayList<>();
        forBigBlind.add(new Call(gameSettings.getStartBigBlindBet()));
        final List<Action> forSmallBlind = new ArrayList<>();
        forSmallBlind.add(new Call(gameSettings.getStartSmallBlindBet()));
        history.put(bigBlind, forBigBlind);
        history.put(smallBlind, forSmallBlind);
        return history;
    }

    private void init() {
        resetTableSettings();
        resetAllCards();
        dealCards();
        setButton();
        setSmallBlind();
        setBigBlind();
        setAllActivePlayers();
    }


    private void resetAllCards() {
        this.allCards = CardType.getAllCardsAsList();
    }

    private void resetTableSettings() {
        this.tableSettings.reset();
    }

    protected List<PlayerEntity> getPlayers() {
        return players;
    }

    protected long getBigBlindBet() {
        return gameSettings.getStartBigBlindBet();
    }

    protected long getSmallBlindBet() {
        return gameSettings.getStartSmallBlindBet();
    }

    protected void setWaitForAllPlayers() {
        this.players.forEach(player -> {
            if (player.getAction() != null && player.getTableSettings().getRoleType() != null) {
                if (!(player.getAction() instanceof Fold) && player.getAction().getActionType() != ActionType.ALLIN && player.getTableSettings().getStateType() == StateType.IN_GAME) {
                    player.getTableSettings().setAction(new Wait());
                }
            }
        });
    }
}
