import animals.*;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        zoo.cage();
        zoo.zoo_whatin();
        System.out.println("Ilość nóg: " + zoo.iloscwszystkichnog());
        System.out.println("Ilość zwierząt, które mają futro: " + zoo.zwierzetaFutro1());

    }
}