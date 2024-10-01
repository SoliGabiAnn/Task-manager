import java.util.Arrays;
import java.util.Random;

class losowanie{
    public static void main(String[] args){
        Random rzut = new Random();
        int [] r;
        for(int i = 0; i < 100; i++){
             r[i] = rzut.nextInt(100)%2;
        }
        System.out.print(Arrays.stream(r).count();
    }
}