package com.example.demo.samochód;

public class Silnik extends Komponent{
    private int maxObroty = 0;
    private int obroty = 0;
    private boolean stanSilnika = false;

    public Silnik(String nazwa, int wagaSilnik, int cenaSilnik, int maxObroty) {
        super(nazwa, wagaSilnik, cenaSilnik);
        this.maxObroty = maxObroty;
    }


    public void uruchom(){
        obroty = 800;
        stanSilnika = true;
    }
    public void zatrzymaj(){
        obroty = 0;
        stanSilnika = false;
    }
    public void zwiekszObroty(){
        if( obroty < maxObroty ){
            obroty += 100;
        } else {
            throw new SilnikException("Osiągnięto już maksymalne obroty");
        }
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
    public boolean getStanSilnika(){
        return stanSilnika;
    }
}
