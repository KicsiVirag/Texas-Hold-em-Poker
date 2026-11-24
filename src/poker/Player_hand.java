package poker;

import java.util.ArrayList;
import java.util.List;

public class Player_hand {
    //token amivel tud játszani
    //kezében van 2 lap
    //actions:
    //Dobás (fold): kilép a leosztásból.
    //Megadás (call): kiegyenlíti a legnagyobb tétet.
    //Emelés (raise): megemeli a tétet.
    //Check (passz): Ha előtte senki nem emelt, passzolhat.
    //All-in: Az összes zsetont beteszi – akár blöffből, akár erős kézzel.
    public enum Actions {
        FOLD, CALL, RAISE, CHECK, ALL_IN
    }

    private List<Cards> twocard;
    private int money;
    private Actions act;

    public Player_hand(List<Cards> twocard, int money) {
        this.twocard = twocard;
        this.money = money;
        this.act = null;
    }

    public String Move(Actions act, int bet, int currentBetToCall) {
        switch (act) {
            case FOLD:
                this.act = Actions.FOLD;
                return "FOLD";

            case CHECK:
                if (bet > 0) {
                    throw new IllegalStateException("Nem passzolhatsz (CHECK), mert előtted tétet tettek! Hívás összege: " + currentBetToCall);
                }
                this.act = Actions.CHECK;
                return "CHECK";

            case CALL:
                if (bet <= 0) {
                    throw new IllegalStateException("Nincs mit megadni (a tét 0), használj CHECK-et!");
                }
                if (bet > this.money) {
                    throw new IllegalStateException("Nincs elég pénzed a teljes megadáshoz! Egyenleg: " + this.money + ", tét: " + currentBetToCall);
                }
                this.act = Actions.CALL;
                this.money -= bet; // Mindig a kötelező tartandó tétet vonjuk le, mondjuk ha valaki 20at a másik 40 emel akkor csak 20at kell adnunk
                return "CALL";

            case RAISE://ha valami 20, majd másik 50 emel akkor a 20asnak csak 30at kell emelnie
                if (bet <= 0) {
                    throw new IllegalArgumentException("Az emelés összegének pozitívnak kell lennie!");
                }
                if (bet <= currentBetToCall) {
                    throw new IllegalArgumentException("Az emelésnek (" + bet + ") nagyobbnak kell lennie, mint a jelenlegi tét (" + currentBetToCall + ")!");
                }
                if (bet > this.money) {
                    throw new IllegalStateException("Túl nagy emelés, nincs elég fedezeted! Egyenleged: " + this.money);
                }
                this.act = Actions.RAISE;
                this.money -= bet;
                return "RAISE";

            case ALL_IN://ha valakinek 100 valakinek 40 allin akkor nem lesz egyenló (side pot)
                if (this.money <= 0) {
                    throw new IllegalStateException("Már nincs zsetonod az All-inhez!");
                }
                this.act = Actions.ALL_IN;
                int allInAmount = this.money;
                this.money = 0;
                return "ALL_IN";

            default:
                return "";
        }
    }

    public void addMoney(int amount){
        if(amount<0){
            throw new IllegalStateException("Az összeg nem lehet negatív!");
        }
        this.money += amount;
    }

    public void removeMoney(int amount){
        this.money -= amount;
    }

    public void fold(){
        this.act = Actions.FOLD;
    }


    public int getMoney() {
        return money;
    }

    public List<Cards> getTwocard() {
        return twocard;
    }
    public void setTwocard(List<Cards> cards) {
        this.twocard = new ArrayList<>(cards);
    }

}
