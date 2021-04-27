package ru.smn.combination.strategy;

import ru.smn.combination.data.CardType;
import ru.smn.combination.data.Combination;
import ru.smn.combination.data.CombinationType;

import java.util.List;

import static ru.smn.combination.utils.CardUtils.*;

public class FlushSearchStrategy implements SearchStrategy {

    @Override
    public Combination find(List<CardType> cards) {
        final List<CardType> spadeFlush = findFlushBySuit(cards, CardType.SuitType.SPADE);

        if (!spadeFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortCardsByDesc(spadeFlush),
                    findBiggerCard(spadeFlush).getPowerAsInt()
            );
        }

        final List<CardType> heartFlush = findFlushBySuit(cards, CardType.SuitType.HEART);

        if (!heartFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortCardsByDesc(heartFlush),
                    findBiggerCard(heartFlush).getPowerAsInt()
            );
        }

        final List<CardType> clubFlush = findFlushBySuit(cards, CardType.SuitType.CLUB);

        if (!clubFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortCardsByDesc(clubFlush),
                    findBiggerCard(clubFlush).getPowerAsInt()
            );
        }

        final List<CardType> diamondFlush = findFlushBySuit(cards, CardType.SuitType.DIAMOND);

        if (!diamondFlush.isEmpty()) {
            return Combination.of(
                    CombinationType.FLUSH,
                    sortCardsByDesc(diamondFlush),
                    findBiggerCard(diamondFlush).getPowerAsInt()
            );
        }

        return Combination.empty();
    }
}
