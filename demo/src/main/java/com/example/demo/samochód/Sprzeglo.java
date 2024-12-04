package com.example.demo.samochód;

public class Sprzeglo extends Komponent{
    private boolean stanSprzegla = false; // nie jest wciśnięte

    Sprzeglo(String nazwa, int wagaSprzeglo, int cenaSprzeglo){
        super(nazwa, wagaSprzeglo, cenaSprzeglo);
    }

    public boolean wcisnij(){
        stanSprzegla = true;
        return stanSprzegla;
    }
    public void zwolnij(){
        stanSprzegla = false;
    }
    public boolean getstanSp(){
        return stanSprzegla;
    }
}
