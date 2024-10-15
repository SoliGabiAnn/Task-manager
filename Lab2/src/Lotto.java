import java.util.*;
package com.journaldev.examples;


public class Lotto {
    public static void main(String[] args){

        int n = 6;
        ArrayList<Integer> arr1 = new ArrayList<Integer>(n);

        for (int i = 1; i <= n; i++) {
            int randomNum = (int)(Math.random() * 49);
            int x = randomNum + 1;
            if (arr1.contains(x) == false) {
                arr1.add(x);

            }else{
                i -= 1;
            }
        }
        System.out.println("Length of your list = "+args.length);

        for(int i = 0; i< args.length; i++) {
            System.out.println(String.format("Command Line Argument %d is %s", i, args[i]));
        }
    }
}
