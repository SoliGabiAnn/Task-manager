import java.util.*;

public class Lotto {
    public static void main(String[] args){

        int n = 6;
        ArrayList<Integer> arr1 = new ArrayList<>(n);

        for (int i = 1; i <= n; i++) {
            int randomNum = (int)(Math.random() * 49);
            int x = randomNum + 1;
            if (!arr1.contains(x)) {
                arr1.add(x);
            }else{
                i -= 1;
            }
        }
        System.out.println(arr1);
    }
}
