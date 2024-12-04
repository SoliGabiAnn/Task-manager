package com.example.demo.samochód;

public class Samochód {
    private boolean stanWlaczenia = false;
    private String nrRej = "";
    private String model = "";
    private String marka = " ";
    private int maxSpeed;

    private SkrzyniaBiegow skrzynia;
    private Silnik silnik;
    private Pozycja aktualnapozycja = new Pozycja(0,0);

    public Samochód(int iloscBiegow, String nrRejest, String marka,String model, int maxSpeed, String nazwaSilnik, String nazwaSkrzynia, int wagaSilnik, int wagaSkrzynia, int wagaSprzeglo, int cenaSilnik, int cenaSkrzynia, int cenaSprzeglo) {
        silnik = new Silnik(nazwaSilnik, wagaSilnik, cenaSilnik);
        skrzynia = new SkrzyniaBiegow(iloscBiegow, nazwaSkrzynia, wagaSkrzynia, wagaSprzeglo, cenaSkrzynia, cenaSprzeglo);
        stanWlaczenia = false;
        this.nrRej = nrRejest;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.marka = marka;
    }
    public String getMarka(){
        return marka;
    }
    public String getModel(){
        return model;
    }
    public String getnrRej(){
        return nrRej;
    }
    public int getpozX(){
       return aktualnapozycja.getX();
    }
    public int getpozY(){
        return aktualnapozycja.getY();
    }
    public int getaktBieg(){
        return skrzynia.getAktBieg();
    }
    public void skrzyniaZwiekszB() throws SkrzyniaException, SamochódException, SilnikException {
        if(stanWlaczenia == true) {
            if(silnik.getObroty() >= 3000){
                skrzynia.zwiekszBieg();
            }else {
                throw new SilnikException("Za małe obroty - nie można zwiększyć biegu!");
            }
        }else{
            throw new SamochódException("Samochód wyłączony");
        }

    }
    public boolean stansprzegla(){
        return skrzynia.spr.getstanSp();
    }
    public void sprzegloWcisnij() throws SkrzyniaException, SprzegloException{
        skrzynia.spr.wcisnij();
    }
    public void sprzegloZwolnij() {
        skrzynia.spr.zwolnij();
    }
    public void skrzyniaZmniejszB() throws SkrzyniaException, SamochódException {
        if(stanWlaczenia) {
            skrzynia.zmniejszBieg();
        }else{
            throw new SamochódException("Samochód wyłączony");
        }
    }
    public void zwiekszObroty() throws SamochódException {
        if(stanWlaczenia) {
            silnik.zwiekszObroty();
        }else{
            throw new SamochódException("Samochód nie jest włączony - nie można zwiększyć obrotów");
        }

    }
    public void zmniejszObroty() throws SamochódException, SilnikException {
        if(stanWlaczenia){
            if(silnik.getObroty() >800){
                silnik.zmniejszObroty();
            }else{
                throw new SilnikException("Zbyt małe obroty - nie można bardziej zmniejszyć!");
            }
        }else{
            throw new SamochódException("Samochód nie jest włączony - nie można zmniejszyć obrotów!");
        }

    }
    public int aktualneObroty(){
        return silnik.getObroty();
    }
    public int maxObroty(){
        return silnik.getMaxObroty();
    }
    public void uruchomSilnik() throws SamochódException, SprzegloException {
        if(skrzynia.spr.getstanSp()) {
            if(stanWlaczenia) {
                silnik.uruchom();
            }else{
                throw new SamochódException("Samochód nie jest włączony");
            }
        }else{
            throw new SprzegloException("Sprzegło nie jest wciśnęte");
        }
    }
    public void zatrzymajSilnik(){
        silnik.zatrzymaj();
    }

    public void wlacz(){
        if(skrzynia.spr.getstanSp()){
            stanWlaczenia = true;
        }else{
            throw new SprzegloException("Sprzeglo nie jest wściśnięte - nie można uruchomić samochodu oraz silnika");
        }
    }
    public void wylacz(){
        stanWlaczenia = false;
    }
    public boolean isStanWlaczenia(){
        return stanWlaczenia;
    }
    public void jedzDo(Pozycja cel) throws SkrzyniaException {
        stanWlaczenia = true;
        skrzynia.zwiekszBieg();
        skrzynia.zwiekszBieg();

        skrzynia.zwiekszBieg();

        skrzynia.zwiekszBieg();

        skrzynia.zwiekszBieg();

        skrzynia.zwiekszBieg();
        skrzynia.zwiekszBieg();


        aktualnapozycja = cel;
        stanWlaczenia = false;
    }
    public int getWagaa(){
        int wagaSuma = skrzynia.getWagaSkrzynia() + silnik.getWagaSilnik();
        return wagaSuma;
    }
    public int AktPredkosc(){
        return  0;
    }



}
