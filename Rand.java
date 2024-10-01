import java.util.Random;

public class Rand {
    public static void main(String args[])
    {
        Random rand = new Random();
        int n;
        int zeroCounter = 0;
        int oneCounter = 0;
        for (int i = 0; i < 100; i++){
            n = rand.nextInt(2);
            if (n == 0){
                zeroCounter++;
            }
            if (n == 1){
                oneCounter++;
            }
        }
        System.out.println("Ilość zer: " + zeroCounter);
        System.out.println("Ilość jedynek: " + oneCounter);
    }
}
