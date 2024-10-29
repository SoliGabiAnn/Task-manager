package tasks;
import animals.*;

import java.util.Random;

public class Zoo {
    public Animal[] cage() {
        int rozmiar = 100;
        Animal [] cages = new Animal[rozmiar];
        Random generator = new Random();

        for(int i = 0; i < rozmiar; i++){
            int liczba = generator.nextInt(4);
            if(liczba == 0){
                cages[i] = new Bird();
            }
            if(liczba == 1){
                cages[i] = new Parrot();
            }
            if(liczba == 2){
                cages[i] = new Reptiles();
            }
            if(liczba == 3){
                cages[i] = new Snake();
            }
        }
        return cages;
    }

    public void zoo_whatin(){
        Animal[] cages = cage();
        for(int i = 1; i <= cages.length; i++){
            System.out.println(i + "." + cages[i-1].getName());
        }
    }
    public int iloscwszystkichnog(){
        Animal[] cages = cage();
        int wszystkie = 0;
        for(int i = 0; i < cages.length; i++){
            wszystkie += cages[i].iloscNog;
        }
        return wszystkie;
    }
//    public int zwierzetaFutro(){
//        Animal[] cages = cage();
//        int ilosc = 0;
//        for(int i = 0; i < cages.length; i++){
//            if(cages[i] instanceof Bird || cages[i] instanceof Parrot){
//                if()
//                ilosc++;
//            }
//        }
//        return ilosc;
//    }

}
