package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.Action;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerCombinationDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.service.CombinationService;
import ru.sm.poker.service.WinnerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.HistoryUtil.*;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

@Service
@RequiredArgsConstructor
public class SimpleWinnerService implements WinnerService {
    private final CombinationService combinationService;

    @Override
    public void sendPrizes(HoldemRoundSettingsDTO holdemRoundSettings) {
        final List<PlayerDTO> playersInGame = getPlayersInGame(holdemRoundSettings.getPlayers());
        if (holdemRoundSettings.isOnePlayerLeft()) {
            playersInGame.get(0).addChips(holdemRoundSettings.getBank());
            return;
        }
        final List<PlayerCombinationDTO> winners = findWinners(holdemRoundSettings);
        calculate(winners, holdemRoundSettings);
    }

    private void calculate(List<PlayerCombinationDTO> winners, HoldemRoundSettingsDTO holdemRoundSettings) {
        if (!checkIfNeedReturnToAll(winners, holdemRoundSettings)) {
            for (PlayerCombinationDTO winnerFromLoop : winners) {
                long winnerBets = sumBets(holdemRoundSettings.getFullHistory().get(winners.get(0).getPlayer()));
                long generatedBank = winnerBets;

                final List<PlayerDTO> playersInGame = getPlayersInGame(holdemRoundSettings.getPlayers()).stream()
                        .filter(playerDTO -> !playerDTO.equals(winnerFromLoop.getPlayer()))
                        .collect(Collectors.toList());


                for (PlayerDTO player : playersInGame) {
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

    private boolean checkIfNeedReturnToAll(List<PlayerCombinationDTO> winners, HoldemRoundSettingsDTO holdemRoundSettings) {
        final List<Action> allWinnersActions = winners.stream()
                .flatMap(player -> holdemRoundSettings.getFullHistory().get(player.getPlayer()).stream())
                .collect(Collectors.toList());

        final long allWinnersBets = sumBets(allWinnersActions);
        if (allWinnersBets == holdemRoundSettings.getBank()) {
            winners.forEach(playerCombinationDTO -> returnChips(holdemRoundSettings, playerCombinationDTO));
            return true;
        }
        return false;
    }


    private void returnChips(HoldemRoundSettingsDTO holdemRoundSettings, PlayerCombinationDTO playerCombinationDTO) {
        playerCombinationDTO.getPlayer().addChips(
                sumBets(holdemRoundSettings.getFullHistory().get(playerCombinationDTO.getPlayer()))
        );
    }

    private List<PlayerCombinationDTO> findWinners(HoldemRoundSettingsDTO holdemRoundSettings) {
        final List<CardType> flop = holdemRoundSettings.getFlop();
        final CardType tern = holdemRoundSettings.getTern();
        final CardType river = holdemRoundSettings.getRiver();
        final List<PlayerCombinationDTO> combinations = new ArrayList<>();


        getPlayersInGame(holdemRoundSettings.getPlayers()).forEach(player -> {
            final List<CardType> playerCards = player.getCards();
            final List<CardType> cards = new ArrayList<>(playerCards);
            cards.addAll(flop);
            cards.add(tern);
            cards.add(river);
            combinations.add(PlayerCombinationDTO.builder()
                    .combination(combinationService.findCombination(cards))
                    .player(player)
                    .build());
        });

        return combinationService.findWinners(combinations);

    }

}
