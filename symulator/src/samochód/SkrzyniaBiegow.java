package samochód;

public class SkrzyniaBiegow {
    private int aktualnyBieg = 1;       //0 - jest na luzie, 1-5 biegi, 6 wsteczny
    private int iloscBiegow = 6;
    private int aktualnePrzelozenie;

    Sprzeglo spr = new Sprzeglo();
    Komponent parametry = new Komponent("Skrzynia biegów", 60, 6000);

    public void zwiekszBieg(){
        if(spr.wcisnij() && aktualnyBieg <= iloscBiegow) {
            aktualnyBieg++;
        }else{
            System.out.println("Nie można zwiększyć biegu");
        }
    }
    public void zmniejszBieg(){
        if(spr.wcisnij() && aktualnyBieg >= 0) {
            aktualnyBieg--;
        }else{
            System.out.println("Nie można zmniejszyć biegu");
        }
    }
    public int getAktBieg(){
        return aktualnyBieg;
    }
    public int getAktPrzelozenie(){
        return aktualnePrzelozenie;
    }

}
