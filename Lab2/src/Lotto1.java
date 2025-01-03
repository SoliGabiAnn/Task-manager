import java.util.*;

public class Lotto1 {
    public static void main(String[] args) {

        ArrayList<Integer> arr2 = new ArrayList<>();

        System.out.println("Podaj 6 liczb w zakresie od 1 do 49");

        if (args.length == 6) {
            for (int i = 0; i < args.length; i++) {
                int number = Integer.parseInt(args[i]);
                if ((number < 50) & (number > 0)) {
                    if (!arr2.contains(number)) {
                        arr2.add(number);
                    } else {
                        System.out.println("Twoja liczba się powtórzyła. Podaj inny argument");
                    }
                } else {
                    System.out.println("Podany argument nie mieści się w przedziale 1-49. Podaj inną liczbę");
                }
            }
            System.out.println("Twoje liczby: " + arr2);
        } else {
            System.out.println("Ilość liczb się nie zgadza. Podaj ich 6!");
        }

        int n = 6;

        ArrayList<Integer> arr1 = new ArrayList<>(n);

        for (int i = 1; i <= n; i++) {
            int randomNum = (int) (Math.random() * 49);
            int x = randomNum + 1;
            System.err.println(x);
            if (!arr1.contains(x)) {
                arr1.add(x);
            } else {
                i -= 1;
            }
        }

        int counting_numbers = 0;

        System.out.println("Wylosowane liczby: " + arr1);
        System.err.println("arr2 " + arr2);
        for (int i = 0; i < n; i++) {
            if (arr1.contains(arr2.get(i))) {
                counting_numbers++;
            }
        }
        System.out.printf("Liczba wystąpień: " + counting_numbers);
    }
}

