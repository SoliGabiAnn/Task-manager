package com.example.demo.samochód;

public class Silnik extends Komponent{
    private final int maxObroty = 7000;
    private int obroty = 0;

    Silnik(String nazwa, int wagaSilnik, int cenaSilnik){
        super(nazwa, wagaSilnik, cenaSilnik);
    }


    public void uruchom(){
        obroty = 800;
    }
    public void zatrzymaj(){
        obroty = 0;
    }
    public void zwiekszObroty(){
        obroty += 100;
    }
    public void zmniejszObroty(){
        if(obroty < 0){
            obroty = 0;
        }else{
            obroty -= 100;
        }
    }
    public int getWagaSilnik(){
        return getWaga();
    }
    public int getObroty(){
        return obroty;
    }
    public int getMaxObroty(){
        return maxObroty;
    }
}
