import java.util.*;

public class Lotto2 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Random rand = new Random();

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

        int loops = 0;
        boolean matched = false;

        while (!matched) {
            ArrayList<Integer> drawnNumbers = new ArrayList<>();
            int result = 0;

            while (drawnNumbers.size() < 6) {
                int num = rand.nextInt(49) + 1;
                if (!drawnNumbers.contains(num)) {
                    drawnNumbers.add(num);
                }
            }

            for (int i = 0; i < 6; i++) {
                if (arr2.contains(drawnNumbers.get(i))) {
                    result++;
                }
            }

            if (result == 6) {
                matched = true;
            }
            loops++;
        }

        long end = System.currentTimeMillis();
        long time = end - start;

        System.out.println("Czas programu: " + time + " ms");
        System.out.println(loops + " prób");
    }
}

