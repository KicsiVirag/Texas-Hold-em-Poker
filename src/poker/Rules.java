package poker;

import java.util.*;
//nem tesz különbséget az ász és a 2es pár között
public class Rules {

    public int Strength(List<Cards> playerhand, List<Cards> communityCards){
        List<Cards> allCards = new ArrayList<>();
        allCards.addAll(playerhand);
        allCards.addAll(communityCards);

        allCards.sort(Comparator.comparing(c -> c.getValue().getScore()));

        Map<Cards.Value, Integer> valueMap = new HashMap<>();
        Map<Cards.Type, Integer> typeMap = new HashMap<>();

        /*for (var i : allCards){
            if(valueMap.containsKey(i.getValue())){
                valueMap.put(i.getValue(), valueMap.get(i.getValue()) + 1);
            }else{
                valueMap.put(i.getValue(), 1);
            }
        }*/
        for (var card : allCards) {
            valueMap.merge(card.getValue(), 1, Integer::sum);
            typeMap.merge(card.getType(), 1, Integer::sum);
        }

        boolean hasFlush = false;
        Cards.Type typeFlush = null;
        for (var count : typeMap.entrySet()) {
            if (count.getValue() >= 5) {
                hasFlush = true;
                typeFlush = count.getKey();
                break;
            }
        }

        List<Cards> flushCards = new ArrayList<>();
        if (hasFlush) {
            for (Cards c : allCards) {
                if (c.getType() == typeFlush) {
                    flushCards.add(c);
                }
            }
        }

        int pairs = 0;
        int three = 0;
        boolean hasFour = false;

        for (int count : valueMap.values()) {
            if (count == 4) hasFour = true;
            else if (count == 3) three++;
            else if (count == 2) pairs++;
        }
        // 1. Royal Flush – A, K, Q, J, 10 azonos színben (legmagasabb sor értéke 14)
        boolean isRoyalFlush = hasFlush && (getStraightHighCard(flushCards) == 14);

        // 2. Straight Flush – Öt egymást követő egyszínű lap
        boolean isStraightFlush = hasFlush && (getStraightHighCard(flushCards) > 0);

        // 3. Four of a Kind (Póker) – Négy azonos értékű lap
        boolean isFourOfAKind = hasFour;

        // 4. Full House – Három egyforma + egy pár (vagy két külön drill a 7 lapból)
        boolean isFullHouse = (three >= 1 && pairs >= 1) || (three >= 2);

        // 5. Flush – Öt egyszínű lap
        boolean isFlush = hasFlush;

        // 6. Straight (Sor) – Öt egymást követő lap vegyes színekben
        boolean isStraight = getStraightHighCard(allCards) > 0;

        // 7. Three of a Kind (Drill) – Három azonos értékű lap
        boolean isThreeOfAKind = (three >= 1);

        // 8. Two Pair (Két pár) – Legalább két külön pár
        boolean isTwoPair = (pairs >= 2);

        // 9. One Pair (Egy pár) – Pontosan egy pár
        boolean isOnePair = (pairs == 1);

        // 10. High Card (Magas lap) – Alapértelmezett, ha semmi más nincs
        boolean isHighCard = true;

        if (isRoyalFlush) return 10; //"Royal Flush";
        if (isStraightFlush) return 9; //"Straight Flush";
        if (isFourOfAKind) return 8;//"Four of a Kind";
        if (isFullHouse) return 7;//"Full House";
        if (isFlush) return 6;//"Flush";
        if (isStraight) return 5;//"Straight";
        if (isThreeOfAKind) return 4;//"Three of a Kind";
        if (isTwoPair) return 3;//"Two Pair";
        if (isOnePair) return 2;//"One Pair";

        return 1;//"High Card";

    }
    private int getStraightHighCard(List<Cards> cards) {
        Set<Integer> scores = new HashSet<>();
        for (Cards c : cards) {
            scores.add(c.getValue().getScore());
        }
        // Az Ász (14) 1-esként is működik az A-2-3-4-5 sorban
        if (scores.contains(14)) {
            scores.add(1);
        }

        for (int top = 14; top >= 5; top--) {
            if (scores.contains(top) &&
                    scores.contains(top - 1) &&
                    scores.contains(top - 2) &&
                    scores.contains(top - 3) &&
                    scores.contains(top - 4)) {
                return top; // Megvan a sor, visszaadjuk a csúcslapját
            }
        }
        return 0; // Nincs sor
    }
}
