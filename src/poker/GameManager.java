package poker;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;

import static poker.Player_hand.Actions.*;

public class GameManager {
    //játékmenet
    //győztes meghatározása után adjuk össze a nyereményt az emberhez


    private List<Cards> deck;
    private List<Cards> communityCards;
    private Rules rules;

    private int bet;
    private int currentBetToCall;
    private int pot;
    private int stage = 0;

    private int MAX_Players = 10;
    private int MIN_Player = 2;
    private List<Player_hand> players = new ArrayList<>();
    private List<Integer> playersBet = new ArrayList<>();
    private int currentPLayer = 0;

    private List<Boolean> floded = new ArrayList<>();
    private List<Boolean> acted = new ArrayList<>();
    private boolean roundClosed = false;
    private boolean handFinished = false;


    public GameManager() {
        deck = Cards.createFulldeck();
        players = new ArrayList<Player_hand>();
        communityCards = new ArrayList<>();
        bet = 0;
        currentBetToCall = 0;
        pot = 0;
        rules = new Rules();
        Cards.Shuffle(deck);
        stage = 1;
    }

    public void addPlayer() {
        if (players.size() > MAX_Players) {
            throw new IllegalStateException("Max 10 játékos lehet egyszerre");
        }
        if (roundClosed || handFinished) {
            throw new IllegalStateException("Most nem léphetsz be a játékba");
        }
        players.add(new Player_hand(Cards.TwoCard(deck), 100));
        playersBet.add(0);
        floded.add(false);
        acted.add(false);
    }

    public List<Cards> dealCards() {
        return Cards.TwoCard(deck);
    }

    public void dealFlop() {
        if (stage == 1) {
            for (int i = 0; i < 3; i++) {
                communityCards.add(deck.remove(0));
            }
        }
        stage++;
        for (int i = 0;i<players.size();i++){
            playersBet.set(i, 0);
            acted.set(i, false);
        }
        currentBetToCall=0;
        currentPLayer=0;
        roundClosed=false;
    }

    public void dealTurn() {
        if (stage == 2) {
            communityCards.add(deck.remove(0));
        }
        stage++;
        for (int i = 0;i<players.size();i++){
            playersBet.set(i, 0);
            acted.set(i, false);
        }
        currentBetToCall=0;
        currentPLayer=0;
        roundClosed=false;
    }

    public void dealRiver() {
        if (stage == 3) {
            communityCards.add(deck.remove(0));
        }
        stage++;
        for (int i = 0;i<players.size();i++){
            playersBet.set(i, 0);
            acted.set(i, false);
        }
        currentBetToCall=0;
        currentPLayer=0;
        roundClosed=false;
    }

    public int getPot() {
        return pot;
    }

    public List<Cards> getCommunityCard() {
        return new ArrayList<>(communityCards);
    }

    public int getHandStrenght(int playerIndex) {
        if (communityCards.size() != 5) {
            throw new IllegalStateException("Nincs meg az 5 lap a közös pakliban!");
        }
        return rules.Strength(players.get(playerIndex).getTwocard(), communityCards);
    }

    public String playerMove(int playerIndex, Player_hand.Actions actions, int amount) {
        if (players.size() < 2) {
            throw new IllegalStateException("Nincs elég játékos");
        }
        if (roundClosed || handFinished) {
            throw new IllegalStateException("Játékos nem léphet");
        }
        if (playerIndex != currentPLayer) {
            throw new IllegalStateException("Nem ez a játékos következik");
        }
        Player_hand player = players.get(playerIndex);

        int toCall = currentBetToCall - playersBet.get(playerIndex);
        int toPay = 0;

        switch (actions) {
            case CHECK:
            case CALL:
                toPay = toCall;
                break;

            case RAISE://ha valaki 20, majd másik 50 emel akkor a 20asnak csak 30at kell emelnie
                toPay = amount - playersBet.get(playerIndex);
                break;

            case ALL_IN://ha valakinek 100 valakinek 40 allin akkor nem lesz egyenló (side pot)
                toPay = toCall;
                break;
            case FOLD:
                toPay=0;
                break;
            default:
                return "Nem támogatott lépés";
        }

        int moneyBefore = player.getMoney();

        String result = player.Move(actions, toPay, currentBetToCall);

        int paid = moneyBefore - player.getMoney();

        pot += paid;
        playersBet.set(playerIndex, playersBet.get(playerIndex)+paid);

        if(actions == RAISE){
            currentBetToCall = amount;

            for (int i = 0; i<players.size();i++){
                if(i != playerIndex && !floded.get(i)){
                    acted.set(i, false);
                }
            }
        }

        if (actions == FOLD) {
            floded.set(playerIndex, true);
        }
        else{
            acted.set(playerIndex,true);
        }

        acted.set(playerIndex, true);


        int active=0;
        for (boolean fold: floded){
            if (!fold){
                active++;
            }
        }
        if(active==1){
            handFinished=true;
            roundClosed=true;
            return result;
        }

        roundClosed=true;

        for (int i =0;i<players.size();i++){
            if(!floded.get(i) && (!acted.get(i)||playersBet.get(i) !=currentBetToCall)){
                roundClosed=false;
                break;
            }
        }

        if(roundClosed){
            currentPLayer =0;
        }
        else {
            do {
                currentPLayer = (currentPLayer+1)%players.size();
            }while(floded.get(currentPLayer));
        }

        return result;
    }
    public void setHandFinished(){
        int bestStrength=0;
        int bestIndex=0;
        List<Integer> winners= new ArrayList<>();
        for (int i = 0; i<players.size();i++){
            if(floded.get(i)){
                continue;
            }
            int strenght = rules.Strength(players.get(i).getTwocard(), communityCards);
            if (strenght>bestStrength){
                bestStrength=strenght;
                winners.clear();
                winners.add(i);
            }else if(strenght == bestStrength){
                winners.add(i);
            }
        }
        int prize = pot/ winners.size();
        for (int winner:winners){
            players.get(winner).addMoney(prize);
        }
        pot =0;
        handFinished=true;
        roundClosed=true;
    }

    public int getPlayerMoney(int playerIndex){
        return players.get(playerIndex).getMoney();
    }
    public List<Cards> getPlayerCards(int playerIndex){
        return players.get(playerIndex).getTwocard();
    }
    public void newHand(){
        deck = Cards.createFulldeck();
        Cards.Shuffle(deck);

        communityCards.clear();

        for (int i =0;i<players.size();i++){
            playersBet.set(i,0);
            floded.set(i,false);
            acted.set(i,false);
            players.get(i).setTwocard(Cards.TwoCard(deck));
        }
        currentBetToCall=0;
        currentPLayer=0;
        pot=0;
        stage=1;

        roundClosed=false;
        handFinished=false;
    }
}
