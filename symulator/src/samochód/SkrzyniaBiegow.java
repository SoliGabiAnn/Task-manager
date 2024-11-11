package samochód;

public class SkrzyniaBiegow extends Komponent {
    private int aktualnyBieg = 1;       //0 - jest na luzie, 1-5 biegi, 6 wsteczny
    private int iloscBiegow;
    private int aktualnePrzelozenie;

    Sprzeglo spr;

    SkrzyniaBiegow(int iloscBiegow, String nazwa, int wagaSkrzynia, int wagaSprzeglo, int cenaSkrzynia, int cenaSprzeglo) {
        super(nazwa, wagaSkrzynia, cenaSkrzynia);
        this.iloscBiegow = iloscBiegow;
        this.spr = new Sprzeglo(nazwa, wagaSprzeglo, cenaSprzeglo);

    }

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
    public int getWagaSkrzynia(){
        return getWaga() + spr.getWaga();
    }

}
