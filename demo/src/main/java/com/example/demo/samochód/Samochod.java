package com.example.demo.samochód;

import com.example.demo.HelloController;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Samochod extends Thread {
    private boolean stanWlaczenia = false;
    private final String nrRej;
    private final String model;
    private final String marka;
    private final int maxSpeed;
    private final double waga;

    private final SkrzyniaBiegow skrzynia;
    private final Silnik silnik;
    private Pozycja aktualnapozycja = new Pozycja(0, 0);
    private HelloController controller;
    private Pozycja cel;
    private final List<Listener> listeners = new ArrayList<>();

    private final boolean isRunning = true;

    private static final double SPEED_FACTOR = 0.1;


    public Samochod(int iloscBiegow, int maxObroty, String nrRejest, String marka, String model, int maxSpeed, double waga,String nazwaSilnik, String nazwaSkrzynia, String nazwaSprzeglo, int wagaSilnik, int wagaSkrzynia, int wagaSprzeglo, int cenaSilnik, int cenaSkrzynia, int cenaSprzeglo) {
        silnik = new Silnik(nazwaSilnik, wagaSilnik, cenaSilnik, maxObroty);
        skrzynia = new SkrzyniaBiegow(iloscBiegow, nazwaSkrzynia, nazwaSprzeglo, wagaSkrzynia, wagaSprzeglo, cenaSkrzynia, cenaSprzeglo);
        stanWlaczenia = false;
        this.nrRej = nrRejest;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.marka = marka;
        this.waga = waga;
        this.start();
    }


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
            skrzynia.wyzerujBieg();
        } else {
            throw new SprzegloException("Sprzeglo nie jest wściśnięte - nie można wyłączyć samochodu oraz silnika");
        }
    }

    public void skrzyniaZwiekszB() throws SkrzyniaException, SamochodException, SilnikException {
        if (stanWlaczenia) {
            if (silnik.getObroty() >= 2500) {
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
            if (!stansprzegla()) {
                silnik.zwiekszObroty();
            } else {
                throw new SprzegloException("Sprzęgło jest wciśnięte");
            }
        } else {
            throw new SamochodException("Samochód nie jest włączony - nie można zwiększyć obrotów");
        }
    }

    public void zmniejszObroty() throws SamochodException, SilnikException {
        if (stanWlaczenia) {
            if (!stansprzegla()) {
                if (silnik.getObroty() > 800) {
                    silnik.zmniejszObroty();
                } else {
                    throw new SilnikException("Zbyt małe obroty - nie można bardziej zmniejszyć!");
                }
            } else {
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

    public void jedzDo(Pozycja nowaPozycja) throws SkrzyniaException {
        // Set the target instead of immediately updating position
        this.cel = nowaPozycja;
    }


    public double getWagaSamochodu() {
        return silnik.getWaga()+skrzynia.getWaga()+this.waga;
    }

    public int getPredkosc() {
        if (!stanWlaczenia || skrzynia.getAktBieg() == 0) {
            return 0;
        }
        return (silnik.getObroty() / 400) * skrzynia.getAktBieg() * 5;
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

    @Override
    public void run() {
        while (isRunning) {
            if (cel != null && stanWlaczenia) {
                double currentX = aktualnapozycja.getX();
                double currentY = aktualnapozycja.getY();
                double targetX = cel.getX();
                double targetY = cel.getY();

                double dx = targetX - currentX;
                double dy = targetY - currentY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 10.0) {
                    double speed = getPredkosc() * SPEED_FACTOR;

                    speed = Math.max(speed, 0.5);

                    double moveX = (dx / distance) * speed;
                    double moveY = (dy / distance) * speed;

                    aktualnapozycja.setX( (currentX + moveX));
                    aktualnapozycja.setY( (currentY + moveY));

                    if (controller != null) {
                        Platform.runLater(() -> controller.refresh());
                    }
                } else {
                    aktualnapozycja.setX( targetX);
                    aktualnapozycja.setY( targetY);
                    cel = null;

                    if (controller != null) {
                        Platform.runLater(() -> controller.refresh());
                    }
                }
                this.notifyListeners();
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
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