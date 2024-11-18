package samochód;

import java.util.concurrent.ExecutionException;

public class SkrzyniaBiegow extends Komponent {
    private int aktualnyBieg = 1;       //0 - jest na luzie, 1-5 biegi, 6 wsteczny
    private int iloscBiegow;
    private int aktualnePrzelozenie = 0;

    Sprzeglo spr;

    SkrzyniaBiegow(int iloscBiegow, String nazwa, int wagaSkrzynia, int wagaSprzeglo, int cenaSkrzynia, int cenaSprzeglo) {
        super(nazwa, wagaSkrzynia, cenaSkrzynia);
        this.iloscBiegow = iloscBiegow;
        this.spr = new Sprzeglo(nazwa, wagaSprzeglo, cenaSprzeglo);

    }

    public void zwiekszBieg() throws SkrzyniaException{
        if(spr.wcisnij() && aktualnyBieg < iloscBiegow) {
            try{
                aktualnyBieg++;
                spr.zwolnij();
            }catch(Exception e){
                throw new SkrzyniaException("Nie można zwiększyć biegu - bieg jest już najniższy!");
            }
            finally {
                spr.zwolnij();
            }
        }
    }
    public void zmniejszBieg() throws SkrzyniaException {
        if(spr.wcisnij() && aktualnyBieg > 0) {
            try{
                aktualnyBieg--;
            }catch(Exception e){}
            finally {
                spr.zwolnij();
            }
        }else{
            throw new SkrzyniaException("Nie można zmniejszyć biegu - bieg jest już neutralny!");
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
