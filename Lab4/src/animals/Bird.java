package animals;
import java.util.Random;

public class Bird extends Animal{
    Random generator = new Random();
    public int hasfur  = generator.nextInt(2);
    {
        this.iloscNog = 2;
    }
    public String getName() {
        return "bird";
    }
    public void fly() {

        System.out.println("I can fly!");
    }

}
