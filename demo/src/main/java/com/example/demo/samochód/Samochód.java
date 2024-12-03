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
    public void skrzyniaZwiekszB() throws SkrzyniaException, SamochódException {
        if( stanWlaczenia == true) {
            skrzynia.zwiekszBieg();
        }else{
            throw new SamochódException("Samochód wyłączony");
        }

    }
    public void skrzyniaZmniejszB() throws SkrzyniaException, SamochódException {
        if( stanWlaczenia == true) {
            skrzynia.zmniejszBieg();
        }else{
            throw new SamochódException("Samochód wyłączony");
        }

    }
    public void wlacz(){
        stanWlaczenia = true;
    }
    public void wylacz(){
        stanWlaczenia = false;
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
