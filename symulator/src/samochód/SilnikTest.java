package samochód;

import static org.junit.Assert.*;

public class SilnikTest {
    Silnik s;
    @org.junit.Before
    public void setUp() throws Exception {
        s = new Silnik("Sam", 5000, 5999);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void uruchom() {
        s.uruchom();
        assertEquals(800, s.getObroty());
    }

    @org.junit.Test
    public void zatrzymaj() {
        s.zatrzymaj();
        assertEquals(0, s.getObroty());
    }

    @org.junit.Test
    public void zwiekszObroty() {
        int przed = s.getObroty();
        s.zwiekszObroty();
        assertEquals(przed + 100, s.getObroty());
    }

    @org.junit.Test
    public void zmniejszObroty() {
        s.zwiekszObroty();
        int przed = s.getObroty();
        s.zmniejszObroty();
        assertEquals(przed-100, s.getObroty());
    }

    @org.junit.Test
    public void getWagaSilnik() {
        assertEquals(5000, s.getWagaSilnik());
    }
}