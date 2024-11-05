package samochód;

public class Samochód {
    private boolean stanWlaczenia = false;
    private String nrRej = "";
    private String model = "";
    private int maxSpeed = 300;

    private Silnik silnik = new Silnik();
    private SkrzyniaBiegow skrzynia = new SkrzyniaBiegow();
    private Pozycja aktualnapozycja = new Pozycja();

    public void wlacz(){
        stanWlaczenia = true;
    }
    public void wylacz(){
        stanWlaczenia = false;
    }
    public void jedzDo(Pozycja cel) {
        aktualnapozycja = cel;
    }
    public int getWagaa(){
        int wagaSam = skrzynia.parametry.getWaga();
        return wagaSam;
    }
    public int AktPredkosc(){
        return  0;
    }
    public Pozycja getPozycja() {
        return aktualnapozycja;
    }



}
