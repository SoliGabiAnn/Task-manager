import java.util.*;

public class Lotto1 {
    public static void main(String[] args) {
        System.out.println("Length of your list = " + args.length);
        ArrayList<Integer> list = new ArrayList<>();

        if(args.length == 6) {

            for(int i = 0; i< args.length; i++) {
                int number = Integer.parseInt(args[i]);
                if((number < 50) & (number > 0 )){
                    if(!list.contains(number)) {
                        list.add(number);
                        System.out.println(args[i]);
                    }
                }else{
                    System.out.println("Podaj inny argument");
                }
            }
        }else {
            System.out.println("Ilość liczb się nie zgadza. Podaj ich 6!");
        }


        System.out.println("Twoje liczby: " + list);


//        for(int i = 0; i< args.length; i++) {
//            if(args[i] < 50 & args[i] > 0 ){
//                System.out.println(args[i]);
//            }
////            System.out.println(String.format("Command Line Argument %d is %s", i, args[i]));
//        }
//    }

    }
}


