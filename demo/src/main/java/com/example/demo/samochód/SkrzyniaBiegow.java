package com.example.demo.samochód;

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
        if(spr.getstanSp() && aktualnyBieg < iloscBiegow) {
            aktualnyBieg++;
        }else {
            throw new SkrzyniaException("Nie można zwiększyć biegu - bieg już jest najwyższy!");
        }
    }
    public void zmniejszBieg() throws SkrzyniaException {
        if(spr.getstanSp() && aktualnyBieg > 0) {
            aktualnyBieg--;
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
