package samochód;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SkrzyniaBiegowTest {
    SkrzyniaBiegow sk;

    @Before
    public void setUp() throws Exception {
        sk = new SkrzyniaBiegow(6, "Sam", 5000, 4000, 10000, 2000);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void zwiekszBieg() {
        int przed = sk.getAktBieg();
        sk.zwiekszBieg();
        assertEquals(przed +1, sk.getAktBieg());
        sk.zwiekszBieg();
        sk.zwiekszBieg();
        sk.zwiekszBieg();
        sk.zwiekszBieg();
        sk.zwiekszBieg();
        assertEquals(6, sk.getAktBieg());
    }

    @Test
    public void zmniejszBieg() {
        int przed = sk.getAktBieg();
        sk.zmniejszBieg();
        assertEquals(przed-1, sk.getAktBieg());
        sk.zmniejszBieg();
        assertEquals(0, sk.getAktBieg());
    }

    @Test
    public void getAktBieg() {
        assertEquals(1, sk.getAktBieg());
    }

    @Test
    public void getAktPrzelozenie(){
        assertEquals(0, sk.getAktPrzelozenie());
    }

    @Test
    public void getWagaSkrzynia() {
        assertEquals(9000, sk.getWagaSkrzynia());
    }
}