package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.game.holdem.HoldemRound;
import ru.smn.poker.service.CombinationService;
import ru.smn.poker.service.WinnerService;
import ru.smn.poker.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.*;

@Service
@RequiredArgsConstructor
public class SimpleWinnerService implements WinnerService {
    private final CombinationService combinationService;
    private final GameService gameService;

    @Override
    public void sendPrizes(HoldemRoundSettings holdemRoundSettings) {
        final List<Player> playersInGame = PlayerUtil.getPlayersInGame(holdemRoundSettings.getPlayers());
        if (holdemRoundSettings.isOnePlayerLeft()) {
            playersInGame.get(0).addChips(holdemRoundSettings.getBank());
            return;
        }
        final List<PlayerCombination> winners = findWinners(holdemRoundSettings);
        calculate(winners, holdemRoundSettings);
        gameService.update(holdemRoundSettings);
    }


    private void calculate(List<PlayerCombination> winners, HoldemRoundSettings holdemRoundSettings) {
        if (!checkIfNeedReturnToAll(winners, holdemRoundSettings)) {
            for (PlayerCombination winnerFromLoop : winners) {
                long winnerBets = sumBets(holdemRoundSettings.getFullHistory().get(winners.get(0).getPlayer()));
                long generatedBank = winnerBets;

                final List<Player> playersInGame = PlayerUtil.getPlayersInGame(holdemRoundSettings.getPlayers()).stream()
                        .filter(playerDTO -> !playerDTO.equals(winnerFromLoop.getPlayer()))
                        .collect(Collectors.toList());


                for (Player player : playersInGame) {
                    final List<Action> otherPlayerActions = holdemRoundSettings.getFullHistory().get(player);
                    long otherPlayer = sumBets(otherPlayerActions);
                    if (winnerBets > otherPlayer) {
                        generatedBank += otherPlayer;
                    } else {
                        generatedBank += winnerBets;
                        long returnChips = Math.abs(otherPlayer - winnerBets);
                        player.addChips(returnChips);
                    }
                }
                winnerFromLoop.getPlayer().addChips(generatedBank);
            }
        }
    }

    private boolean checkIfNeedReturnToAll(List<PlayerCombination> winners, HoldemRoundSettings holdemRoundSettings) {
        final List<Action> allWinnersActions = winners.stream()
                .flatMap(player -> holdemRoundSettings.getFullHistory().get(player.getPlayer()).stream())
                .collect(Collectors.toList());

        final long allWinnersBets = sumBets(allWinnersActions);
        if (allWinnersBets == holdemRoundSettings.getBank()) {
            winners.forEach(playerCombination -> returnChips(holdemRoundSettings, playerCombination));
            return true;
        }
        return false;
    }


    private void returnChips(HoldemRoundSettings holdemRoundSettings, PlayerCombination playerCombination) {
        playerCombination.getPlayer().addChips(
                sumBets(holdemRoundSettings.getFullHistory().get(playerCombination.getPlayer()))
        );
    }

    private List<PlayerCombination> findWinners(HoldemRoundSettings holdemRoundSettings) {
        final List<CardType> flop = holdemRoundSettings.getFlop();
        final CardType tern = holdemRoundSettings.getTern();
        final CardType river = holdemRoundSettings.getRiver();
        final List<PlayerCombination> combinations = new ArrayList<>();


        PlayerUtil.getPlayersInGame(holdemRoundSettings.getPlayers()).forEach(player -> {
            final List<CardType> playerCards = player.getCards();
            final List<CardType> cards = new ArrayList<>(playerCards);
            cards.addAll(flop);
            cards.add(tern);
            cards.add(river);
            combinations.add(PlayerCombination.builder()
                    .combination(combinationService.findCombination(cards))
                    .player(player)
                    .build());
        });

        return combinationService.findWinners(combinations);

    }

}
