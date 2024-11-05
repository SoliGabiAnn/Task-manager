package samochód;

public class Silnik{
    private final int maxObroty = 7000;
    private int obroty = 0;

//    Komponent parametry = new Komponent();

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
        obroty -= 100;
    }
}
