package samochód;

public class SkrzyniaException extends Exception{
    public void zwiekszBieg() throws Exception{
        throw new SkrzyniaException("Za duży bieg");
    }
}
