package samochód;

public class Sprzeglo extends SkrzyniaBiegow{
    private boolean stanSprzegla = false; // nie jest wciśnięte

    Komponent komponent = new Komponent("Sprzeglo", 10, 2000);

    public boolean wcisnij(){
        stanSprzegla = true;
        return stanSprzegla;
    }
    public boolean zwolnij(){
        stanSprzegla = false;
        return stanSprzegla;
    }
}
