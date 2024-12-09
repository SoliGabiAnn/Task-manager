import java.util.ArrayList;
import java.util.Random;

public class Lotto {

    public static ArrayList<Object> lotto() {
        int n = 6;
        Random generator = new Random();
        var arr1 = new ArrayList<>(n);

        for(int i=0; i<=n; i++) {
            int randomNumber = generator.nextInt(50);
            int readyRandomNumber = randomNumber+1;
            if (!arr1.contains(readyRandomNumber)) {
                arr1.add(readyRandomNumber);
            }else{
                i -= 1;
            }
        }
        return arr1;
    }

    public static void main(String[] args){
        ArrayList<Object> lotto = lotto();
        for(Object o : lotto){
            System.out.println(o);
        }
    }
}
