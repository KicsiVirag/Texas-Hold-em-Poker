package poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cards {
    //52 lap
    //piros kör
    //piros káró
    //fekete pikk
    //fekete treff

    //király,dáma,jumbo,10,9,8,7,6,5,4,3,2,1,ász

    public enum Type{
        KOR,KARO,PIKK,TREFF
    }

    private Type type;

    public enum Value{
        KETTO(2), HAROM(3), NEGY(4), OT(5), HAT(6), HET(7),
        NYOLC(8), KILENC(9), TIZ(10), JUMBO(11), DAMA(12), KIRALY(13), ASZ(14);

        private final int score;

        // Konstruktor
        Value(int score) {
            this.score = score;
        }

        // Getter az érték lekéréséhez
        public int getScore() {
            return score;
        }
    }

    private Value value;

    public Cards(Type type, Value value){
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Value getValue() {
        return value;
    }

    public static List<Cards> createFulldeck(){
        List<Cards> deck = new ArrayList<>();
        for(Type t : Type.values()){
            for(Value v : Value.values()){
                deck.add(new Cards(t,v));
            }
        }
        return deck;
    }

    //Fisher–Yates keverési algoritmus
    public static void Shuffle(List<Cards> deck){
        Random random = new Random();
        for(int i = 0; i < 52; i++){
            int j = random.nextInt(i+1);
            Cards temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    public static List<Cards> TwoCard(List<Cards> deck){
        List<Cards> twocard = new ArrayList<>();
        for (int i = 0; i<2; i++){
            twocard.add(deck.remove(deck.size() - 1));
        }
        return twocard;
    }

    public static List<Cards> CommunityCards(List<Cards> deck){
        List<Cards> communityCards = new ArrayList<>();
        for (int i = 0; i<5; i++){
            communityCards.add(deck.remove(deck.size() - 1));
        }
        return communityCards;
    }

    @Override
    public String toString() {
        return type + " " + value;
    }
}
