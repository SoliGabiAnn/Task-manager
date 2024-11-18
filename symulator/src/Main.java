import samochód.*;

public class Main {
    public static void main(String[] args) throws SkrzyniaException {
        Samochód car = new Samochód(6, "111", "Reunalt", 250, "Sam", "Elo",7000, 5000, 50000, 4000, 5000, 5000);
        System.out.println(car.getWagaa());
        car.jedzDo(new Pozycja(100,100));
    }
}