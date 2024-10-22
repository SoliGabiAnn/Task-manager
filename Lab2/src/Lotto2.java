import java.util.*;

public class Lotto2 {
    public static void main(String[] args) {

        ArrayList<Integer> list = new ArrayList<>();

        System.out.println("Podaj 6 liczb w zakresie od 1 do 49");

        if(args.length == 6) {
            for(int i = 0; i< args.length; i++) {
                int number = Integer.parseInt(args[i]);
                if((number < 50) & (number > 0 )){
                    if(!list.contains(number)) {
                        list.add(number);
                        System.out.println(args[i]);
                    }else{
                        System.out.println("Twoja liczba się powtórzyła. Podaj inny argument");
                    }
                }else{
                    System.out.println("Podany argument nie mieści się w przedziale 1-49. Podaj inną liczbę");
                }
            }
            System.out.println("Twoje liczby: " + list);
        }else {
            System.out.println("Ilość liczb się nie zgadza. Podaj ich 6!");
        }

        int n = 6;
        int loop = 0;

        ArrayList<Integer> arr1 = new ArrayList<>(n);

        for (int i = 1; i <= n; i++) {
            int randomNum = (int)(Math.random() * 49);
            int x = randomNum + 1;
            if(arr1.size() != 6){
               while(list.contains(x) ) {
                    if (!arr1.contains(x)) {
                        arr1.add(x);
                        System.out.println(x);

                    }else{
                        i -= 1;
                    }
                }
            }

        }
    }
}

