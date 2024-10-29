package animals;

public abstract class Animal {
    public int iloscNog;
    public void live() {
        System.out.println("I can live.");
    }
    public String getName() {
        return "animal";
    }
    public int getIloscNog(){
        return iloscNog;
    }

}
