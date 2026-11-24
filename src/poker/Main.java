package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static poker.Player_hand.Actions.*;
import static poker.Cards.Type.*;
import static poker.Cards.Value.*;
import static poker.GameManager.*;
//ide mindenképpen kelleni fog olyan is amikor bedobja minden ki a lapot és feleznek--> nincs megcsinálva
//hálózat
//grafika

public class Main {
    public static void main(String[] args) {

            GameManager game = new GameManager();

            // 5 jatekos hozzaadasa
            for (int i = 0; i < 5; i++) {
                game.addPlayer();
            }

            System.out.println("===== 5 JATEKOS TESZT =====");

            // Jatekosok kezdolapjai es penze
            printPlayers(game, 5);

            // =========================
            // PRE-FLOP
            // =========================

            System.out.println("\n--- PRE-FLOP ---");

            raise(game, 0, 20);
            call(game, 1);
            fold(game, 2);
            raise(game, 3, 40);
            call(game, 4);
            call(game, 0);
            call(game, 1);

            System.out.println("Kassza: " + game.getPot());

            // =========================
            // FLOP
            // =========================

            game.dealFlop();

            System.out.println("\n--- FLOP ---");
            System.out.println("Asztal: " + game.getCommunityCard());

            check(game, 0);
            check(game, 1);
            check(game, 3);
            check(game, 4);

            System.out.println("Kassza: " + game.getPot());

            // =========================
            // TURN
            // =========================

            game.dealTurn();

            System.out.println("\n--- TURN ---");
            System.out.println("Asztal: " + game.getCommunityCard());

            check(game, 0);
            check(game, 1);
            check(game, 3);
            check(game, 4);

            // =========================
            // RIVER
            // =========================

            game.dealRiver();

            System.out.println("\n--- RIVER ---");
            System.out.println("Asztal: " + game.getCommunityCard());

            check(game, 0);
            check(game, 1);
            check(game, 3);
            check(game, 4);

            // =========================
            // SHOWDOWN
            // =========================

            System.out.println("\n--- SHOWDOWN ---");

            for (int i = 0; i < 5; i++) {
                System.out.println(
                        "P" + (i + 1)
                                + " kombinacio: "
                                + game.getHandStrenght(i)
                );
            }

            System.out.println("Kassza: " + game.getPot());

            game.setHandFinished();

            System.out.println("\n--- EREDMENY ---");
            printPlayers(game, 5);
            System.out.println("Kassza: " + game.getPot());
        }

        private static void printPlayers(
                GameManager game, int count) {

            for (int i = 0; i < count; i++) {
                System.out.println(
                        "P" + (i + 1)
                                + " lapjai: " + game.getPlayerCards(i)
                                + " | Penze: " + game.getPlayerMoney(i)
                );
            }
        }

        private static void raise(
                GameManager game, int index, int amount) {

            int before = game.getPlayerMoney(index);

            String result = game.playerMove(index, RAISE, amount);

            int paid = before - game.getPlayerMoney(index);

            System.out.println(
                    "P" + (index + 1)
                            + ": " + result
                            + " -> " + amount + "-ra emelt"
                            + " (befizetett: " + paid + ")"
                            + " | Penze: " + game.getPlayerMoney(index)
            );
        }

        private static void call(
                GameManager game, int index) {

            int before = game.getPlayerMoney(index);

            String result = game.playerMove(index, CALL, 0);

            int paid = before - game.getPlayerMoney(index);

            System.out.println(
                    "P" + (index + 1)
                            + ": " + result
                            + " -> " + paid + " zseton"
                            + " | Penze: " + game.getPlayerMoney(index)
            );
        }

        private static void check(
                GameManager game, int index) {

            System.out.println(
                    "P" + (index + 1)
                            + ": " + game.playerMove(index, CHECK, 0)
            );
        }

        private static void fold(
                GameManager game, int index) {

            System.out.println(
                    "P" + (index + 1)
                            + ": " + game.playerMove(index, FOLD, 0)
            );
        }
    }




       /* List<Cards> deck = Cards.createFulldeck();
        Cards.Shuffle(deck);

        int bet = 0;
        int currentBetToCall = 0;
        int pot = 0;
        List<Cards> communityCards = new ArrayList<>();
        Rules rules = new Rules();

        // Kombinációk sorrendje az indexek alapján (gyengébbtől az erősebbig)
        List<String> rankHierarchy = Arrays.asList(
                "High Card", "One Pair", "Two Pair", "Three of a Kind",
                "Straight", "Flush", "Full House", "Four of a Kind",
                "Straight Flush", "Royal Flush"
        );

        Player_hand player1 = new Player_hand(Cards.TwoCard(deck), 100);
        Player_hand player2 = new Player_hand(Cards.TwoCard(deck), 100);

        // ==========================================
        // 1. KÖR: PRE-FLOP
        // ==========================================
        System.out.println("=== 1. KÖR: PRE-FLOP ===");
        System.out.println("P1 lapjai: " + player1.getTwocard() + " (" + player1.getMoney() + " zseton)");
        System.out.println("P2 lapjai: " + player2.getTwocard() + " (" + player2.getMoney() + " zseton)\n");

        System.out.println("P1: " + player1.Move(CHECK, 0, currentBetToCall));

        bet = 20;
        System.out.println("P2: " + player2.Move(RAISE, bet, currentBetToCall));
        currentBetToCall = bet;
        pot += bet;

        System.out.println("P1: " + player1.Move(CALL, currentBetToCall, currentBetToCall));
        pot += currentBetToCall;

        // ==========================================
        // 2. KÖR: FLOP
        // ==========================================
        System.out.println("\n=== 2. KÖR: FLOP ===");
        for (int i = 0; i < 3; i++) {
            communityCards.add(deck.remove(deck.size() - 1));
        }
        System.out.println("Asztal (3 lap): " + communityCards);

        currentBetToCall = 0;
        System.out.println("P1: " + player1.Move(CHECK, 0, currentBetToCall));

        bet = 20;
        System.out.println("P2: " + player2.Move(RAISE, bet, currentBetToCall));
        currentBetToCall = bet;
        pot += bet;

        System.out.println("P1: " + player1.Move(CALL, currentBetToCall, currentBetToCall));
        pot += currentBetToCall;

        // ==========================================
        // 3. KÖR: TURN
        // ==========================================
        System.out.println("\n=== 3. KÖR: TURN ===");
        communityCards.add(deck.remove(deck.size() - 1));
        System.out.println("Asztal (4 lap): " + communityCards);

        currentBetToCall = 0;
        System.out.println("P1: " + player1.Move(CHECK, 0, currentBetToCall));
        System.out.println("P2: " + player2.Move(CHECK, 0, currentBetToCall));

        // ==========================================
        // 4. KÖR: RIVER
        // ==========================================
        System.out.println("\n=== 4. KÖR: RIVER ===");
        communityCards.add(deck.remove(deck.size() - 1));
        System.out.println("Asztal (5 lap): " + communityCards);

        currentBetToCall = 0;
        System.out.println("P1: " + player1.Move(CHECK, 0, currentBetToCall));

        int allInAmount = player2.getMoney();
        System.out.println("P2: " + player2.Move(ALL_IN, allInAmount, currentBetToCall));
        currentBetToCall = allInAmount;
        pot += allInAmount;

        System.out.println("P1: " + player1.Move(CALL, currentBetToCall, currentBetToCall));
        pot += currentBetToCall;

        // ==========================================
        // 5. KÖR: SHOWDOWN
        // ==========================================
        System.out.println("\n=== 5. KÖR: SHOWDOWN ===");
        System.out.println("Asztal lapjai: " + communityCards);

        String p1Rank = rules.Strength(player1.getTwocard(), communityCards);
        String p2Rank = rules.Strength(player2.getTwocard(), communityCards);

        System.out.println("P1 lapjai: " + player1.getTwocard() + " -> Kombináció: " + p1Rank);
        System.out.println("P2 lapjai: " + player2.getTwocard() + " -> Kombináció: " + p2Rank);

        int p1Score = rankHierarchy.indexOf(p1Rank);
        int p2Score = rankHierarchy.indexOf(p2Rank);

        if (p1Score > p2Score) {
            System.out.println(">>> GYŐZTES: Player 1! Nyereménye: " + pot + " zseton.");
        } else if (p2Score > p1Score) {
            System.out.println(">>> GYŐZTES: Player 2! Nyereménye: " + pot + " zseton.");
        } else {
            System.out.println(">>> DÖNTETLEN (" + p1Rank + ")! Kassza felezve.");
        }

        // =========================================================================
        // 6. EXTRA TESZTEK: MIND A 10 KOMBINÁCIÓ FIX KÁRTYÁKKAL
        // =========================================================================
        System.out.println("\n==================================================");
        System.out.println("          RULES (KEZEK ERŐSSÉGE) TESZTEK          ");
        System.out.println("==================================================");

        testHand(rules, "Royal Flush teszt",
                Arrays.asList(new Cards(PIKK, ASZ), new Cards(PIKK, KIRALY)),
                Arrays.asList(new Cards(PIKK, DAMA), new Cards(PIKK, JUMBO), new Cards(PIKK, TIZ), new Cards(KOR, KETTO), new Cards(KARO, HAROM)));

        testHand(rules, "Straight Flush teszt",
                Arrays.asList(new Cards(KOR, NYOLC), new Cards(KOR, HET)),
                Arrays.asList(new Cards(KOR, HAT), new Cards(KOR, OT), new Cards(KOR, NEGY), new Cards(PIKK, KIRALY), new Cards(TREFF, KETTO)));

        testHand(rules, "Four of a Kind teszt",
                Arrays.asList(new Cards(PIKK, JUMBO), new Cards(KOR, JUMBO)),
                Arrays.asList(new Cards(KARO, JUMBO), new Cards(TREFF, JUMBO), new Cards(KOR, OT), new Cards(PIKK, KETTO), new Cards(TREFF, KILENC)));

        testHand(rules, "Full House teszt",
                Arrays.asList(new Cards(PIKK, TIZ), new Cards(KOR, TIZ)),
                Arrays.asList(new Cards(KARO, TIZ), new Cards(TREFF, HET), new Cards(KOR, HET), new Cards(PIKK, KETTO), new Cards(TREFF, HAROM)));

        testHand(rules, "Flush teszt",
                Arrays.asList(new Cards(KARO, ASZ), new Cards(KARO, KILENC)),
                Arrays.asList(new Cards(KARO, HET), new Cards(KARO, OT), new Cards(KARO, KETTO), new Cards(PIKK, KIRALY), new Cards(TREFF, NEGY)));

        testHand(rules, "Straight teszt",
                Arrays.asList(new Cards(PIKK, KILENC), new Cards(KOR, NYOLC)),
                Arrays.asList(new Cards(KARO, HET), new Cards(TREFF, HAT), new Cards(PIKK, OT), new Cards(KOR, KETTO), new Cards(TREFF, ASZ)));

        testHand(rules, "Three of a Kind teszt",
                Arrays.asList(new Cards(PIKK, KIRALY), new Cards(KOR, KIRALY)),
                Arrays.asList(new Cards(KARO, KIRALY), new Cards(TREFF, NYOLC), new Cards(PIKK, OT), new Cards(KOR, KETTO), new Cards(TREFF, JUMBO)));

        testHand(rules, "Two Pair teszt",
                Arrays.asList(new Cards(PIKK, DAMA), new Cards(KOR, DAMA)),
                Arrays.asList(new Cards(KARO, NEGY), new Cards(TREFF, NEGY), new Cards(PIKK, ASZ), new Cards(KOR, HET), new Cards(TREFF, KETTO)));

        testHand(rules, "One Pair teszt",
                Arrays.asList(new Cards(PIKK, ASZ), new Cards(KOR, ASZ)),
                Arrays.asList(new Cards(KARO, KILENC), new Cards(TREFF, HET), new Cards(PIKK, OT), new Cards(KOR, HAROM), new Cards(TREFF, KETTO)));

        testHand(rules, "High Card teszt",
                Arrays.asList(new Cards(PIKK, ASZ), new Cards(KOR, JUMBO)),
                Arrays.asList(new Cards(KARO, KILENC), new Cards(TREFF, HET), new Cards(PIKK, OT), new Cards(KOR, HAROM), new Cards(TREFF, KETTO)));
    }

    private static void testHand(Rules rules, String testName, List<Cards> hole, List<Cards> board) {
        String result = rules.Strength(hole, board);
        System.out.printf("%-22s -> Eredmény: %s%n", testName, result);
    }
    }
}*/