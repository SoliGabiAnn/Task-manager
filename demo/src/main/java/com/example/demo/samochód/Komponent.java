package com.example.demo.samochód;

public class Komponent {
    private String nazwa = "";
    private int waga = 0;
    private int cena = 0;

    public Komponent(String nazwa, int waga, int cena) {
        this.nazwa = nazwa;
        this.waga = waga;
        this.cena = cena;
    }

    public String getNazwa(){
        return nazwa;
    }
    public int  getWaga(){
        return waga;
    }
    public int getCena(){
        return cena;
    }

}
