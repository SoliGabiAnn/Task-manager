package com.example.demo.samochód;
import com.example.demo.HelloController;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Samochod extends Thread {
    private boolean stanWlaczenia = false;
    private String nrRej = "";
    private String model = "";
    private String marka = " ";
    private int maxSpeed;

    private final SkrzyniaBiegow skrzynia;
    private final Silnik silnik;
    private Pozycja aktualnapozycja = new Pozycja(0, 0);
    private HelloController controller;
    private Pozycja cel;
    private List<Listener> listeners = new ArrayList<>();



    public Samochod(int iloscBiegow, int maxObroty, String nrRejest, String marka, String model, int maxSpeed, String nazwaSilnik, String nazwaSkrzynia, String nazwaSprzeglo, int wagaSilnik, int wagaSkrzynia, int wagaSprzeglo, int cenaSilnik, int cenaSkrzynia, int cenaSprzeglo) {
        silnik = new Silnik(nazwaSilnik, wagaSilnik, cenaSilnik, maxObroty);
        skrzynia = new SkrzyniaBiegow(iloscBiegow, nazwaSkrzynia, nazwaSprzeglo, wagaSkrzynia, wagaSprzeglo, cenaSkrzynia, cenaSprzeglo);
        stanWlaczenia = false;
        this.nrRej = nrRejest;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.marka = marka;
    }

//    @Override
//    public void run() {
//        double deltat = 0.1;
//        while (true) {
//            if (cel != null) {
//                double odleglosc = Math.sqrt(Math.pow(cel.getX() - aktualnapozycja.getX(), 2) + Math.pow(cel.getX() - aktualnapozycja.getY(), 2));
//                double dx = getPredkosc() * deltat * (cel.getX() - aktualnapozycja.getX()) / odleglosc;
//                double dy = getPredkosc() * deltat * (cel.getY() - aktualnapozycja.getY()) / odleglosc;
//            }
//        }
//    }

    public void wlacz() {
        if (skrzynia.getSprzeglo().getstanSp()) {
            stanWlaczenia = true;
            silnik.uruchom();
        } else {
            throw new SprzegloException("Sprzeglo nie jest wściśnięte - nie można uruchomić samochodu oraz silnika");
        }
    }

    public void wylacz() {
        if (skrzynia.getSprzeglo().getstanSp()) {
            stanWlaczenia = false;
            silnik.zatrzymaj();
        } else {
            throw new SprzegloException("Sprzeglo nie jest wściśnięte - nie można wyłączyć samochodu oraz silnika");
        }
    }

    public void skrzyniaZwiekszB() throws SkrzyniaException, SamochodException, SilnikException {
        if (stanWlaczenia) {
            if (silnik.getObroty() >= 3000) {
                skrzynia.zwiekszBieg();
            } else {
                throw new SilnikException("Za małe obroty - nie można zwiększyć biegu!");
            }
        } else {
            throw new SamochodException("Samochód wyłączony");
        }
    }

    public void skrzyniaZmniejszB() throws SkrzyniaException, SamochodException, SilnikException {
        if (stanWlaczenia) {
            if (silnik.getObroty() <= 1500) {
                skrzynia.zmniejszBieg();
            } else {
                throw new SilnikException("Za duże obroty - zmniejsz je, żeby zmniejszyć bieg");
            }
        } else {
            throw new SamochodException("Samochód wyłączony");
        }
    }
    public void sprzegloWcisnij() throws SkrzyniaException, SprzegloException {
        skrzynia.getSprzeglo().wcisnij();
    }

    public void sprzegloZwolnij() {
        skrzynia.getSprzeglo().zwolnij();
    }

    public boolean stansprzegla() {
        return skrzynia.getSprzeglo().getstanSp();
    }

    public void zwiekszObroty() throws SamochodException {
        if (stanWlaczenia) {
            if(!stansprzegla()){
                silnik.zwiekszObroty();
            }else{
                throw new SprzegloException("Sprzęgło jest wciśnięte");
            }
        } else {
            throw new SamochodException("Samochód nie jest włączony - nie można zwiększyć obrotów");
        }
    }

    public void zmniejszObroty() throws SamochodException, SilnikException {
        if (stanWlaczenia) {
            if(!stansprzegla()){
                if (silnik.getObroty() > 800) {
                    silnik.zmniejszObroty();
                } else {
                    throw new SilnikException("Zbyt małe obroty - nie można bardziej zmniejszyć!");
                }
            }else{
                throw new SprzegloException("Sprzęgło jest wciśnięte");
            }

        } else {
            throw new SamochodException("Samochód nie jest włączony - nie można zmniejszyć obrotów!");
        }

    }

    public void uruchomSilnik() throws SamochodException, SprzegloException {
        if (skrzynia.getSprzeglo().getstanSp()) {
            if (stanWlaczenia) {
                silnik.uruchom();
            } else {
                throw new SamochodException("Samochód nie jest włączony");
            }
        } else {
            throw new SprzegloException("Sprzegło nie jest wciśnęte");
        }
    }

    public void zatrzymajSilnik() throws SamochodException, SprzegloException {
        if (skrzynia.getSprzeglo().getstanSp()) {
            if (!stanWlaczenia) {
                silnik.zatrzymaj();
            } else {
                throw new SamochodException("Samochód jest włączony");
            }
        } else {
            throw new SprzegloException("Sprzegło nie jest wciśnęte");
        }
    }

    public int aktualneObroty() {
        return silnik.getObroty();
    }

    public int maxObroty() {
        return silnik.getMaxObroty();
    }

    public boolean stanSilnika() {
        return silnik.getStanSilnika();
    }

    public boolean isStanWlaczenia() {
        return stanWlaczenia;
    }

    public void jedzDo(Pozycja cel) throws SkrzyniaException {
        aktualnapozycja = cel;
    }

    public int getWagaSamochodu() {
        return skrzynia.getWagaSkrzynia() + silnik.getWagaSilnik();
    }

    public int getPredkosc() {
        return 0;
    }

    public void setController(HelloController controller) {
        this.controller = controller;
    }

    public SkrzyniaBiegow getSkrzynia() {
        return skrzynia;
    }

    public Silnik getSilnik() {
        return silnik;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getnrRej() {
        return nrRej;
    }

    public int getpozX() {
        return (int) aktualnapozycja.getX();
    }

    public int getpozY() {
        return (int) aktualnapozycja.getY();
    }

    public int getaktBieg() {
        return skrzynia.getAktBieg();
    }

    public void run(){
        double deltat = 0.1;

        while (true) {
            if (cel != null) {
                if(abs(cel.getX()-getpozX())>5 && abs(cel.getY()-getpozY())>5){
                    double odleglosc = Math.sqrt(Math.pow(cel.getX() - getpozX(), 2) +
                            Math.pow(cel.getY() - getpozY(), 2));
                    double dx = this.getPredkosc() * deltat * (cel.getY() - getpozY()) /
                            odleglosc;
                    double dy = this.getPredkosc() * deltat * (cel.getY() - aktualnapozycja.getY()) /
                            odleglosc;


                    aktualnapozycja.setX((int) (aktualnapozycja.getX() + dx));
                    aktualnapozycja.setY((int) (aktualnapozycja.getY() + dy));
                    this.notifyListeners();
                }
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }
}