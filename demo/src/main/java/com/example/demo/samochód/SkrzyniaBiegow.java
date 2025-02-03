package com.example.demo.samochód;

import java.util.concurrent.ExecutionException;

public class SkrzyniaBiegow extends Komponent {
    private int aktualnyBieg = 1;       //0 - jest na luzie, 1-5 biegi, 6 wsteczny
    private final int iloscBiegow;
    private int aktualnePrzelozenie = 0;

    private Sprzeglo spr;

    public SkrzyniaBiegow(int iloscBiegow, String nazwa, String nazwaSprzeglo, int wagaSkrzynia, int wagaSprzeglo, int cenaSkrzynia, int cenaSprzeglo) {
        super(nazwa, wagaSkrzynia, cenaSkrzynia);
        this.iloscBiegow = iloscBiegow;
        this.spr = new Sprzeglo(nazwaSprzeglo, wagaSprzeglo, cenaSprzeglo);

    }

    public SkrzyniaBiegow(int iloscBiegow, String nazwa, int wagaSkrzynia, int cenaSkrzynia) {
        super(nazwa, wagaSkrzynia, cenaSkrzynia);
        this.iloscBiegow = iloscBiegow;
    }

    public void zwiekszBieg() throws SkrzyniaException, SprzegloException {
        if (spr.getstanSp()) {
            if (aktualnyBieg < iloscBiegow) {
                aktualnyBieg++;
            } else {
                throw new SkrzyniaException("Nie można zwiększyć biegu - bieg już jest najwyższy!");
            }
        } else {
            throw new SprzegloException("Sprzęgło nie jest wciśnięte - nie można zwiększyć biegu");
        }
    }

    public void zmniejszBieg() throws SkrzyniaException {
        if (spr.getstanSp()) {
            if (aktualnyBieg > 0) {
                aktualnyBieg--;
            } else {
                throw new SkrzyniaException("Nie można zmniejszyć biegu - bieg jest już neutralny!");
            }
        } else {
            throw new SprzegloException("Sprzegło nie jest wciśnięte - nie można zmniejszyć biegu");
        }

    }

    public void wyzerujBieg() {
        aktualnyBieg = 0;
    }

    public int getAktBieg() {
        return aktualnyBieg;
    }

    public int getAktPrzelozenie() {
        return aktualnePrzelozenie;
    }

    //    public int getWagaSkrzynia() {
//        return (this.getWaga() + spr.getWaga());
//    }
    public int getWagaSkrzynia() {
        return this.getWaga() + spr.getWaga();
    }

    public int getIloscBiegow() {
        return iloscBiegow;
    }

    public Sprzeglo getSprzeglo() {
        return spr;
    }

}