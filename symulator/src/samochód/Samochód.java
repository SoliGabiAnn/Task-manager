package samochód;

public class Samochód {
    private boolean stanWlaczenia = false;
    private String nrRej = "";
    private String model = "";
    private int maxSpeed;

    private SkrzyniaBiegow skrzynia;
    private Silnik silnik;
    private Pozycja aktualnapozycja = new Pozycja(0,0);

    public Samochód(int iloscBiegow, String nrRejest, String model, int maxSpeed, String nazwa, int wagaSilnik, int wagaSkrzynia, int wagaSprzeglo, int cenaSilnik, int cenaSkrzynia, int cenaSprzeglo) {
        silnik = new Silnik(nazwa, wagaSilnik, cenaSilnik);
        skrzynia = new SkrzyniaBiegow(iloscBiegow, nazwa, wagaSkrzynia, wagaSprzeglo, cenaSkrzynia, cenaSprzeglo);
        stanWlaczenia = false;
        this.nrRej = nrRejest;
        this.model = model;
        this.maxSpeed = maxSpeed;
    }

    public void wlacz(){
        stanWlaczenia = true;
    }
    public void wylacz(){
        stanWlaczenia = false;
    }
    public void jedzDo(Pozycja cel) {
        stanWlaczenia = true;
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
    public Pozycja getPozycja() {
        return aktualnapozycja;
    }



}
