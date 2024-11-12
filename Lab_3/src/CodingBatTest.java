import static org.junit.Assert.*;

public class CodingBatTest {
    CodingBat c;

    @org.junit.Before
    public void setUp() throws Exception {
        c = new CodingBat();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void haveThree() {
        assertTrue(c.haveThree(new int[]{3, 1, 3, 1, 3}));
        assertFalse(c.haveThree(new int[]{3, 1, 3, 3}));
        assertFalse(c.haveThree(new int[]{3, 4, 3, 3, 4}));
    }
    @org.junit.Test
    public void nearHundred(){
       assertTrue(c.nearHundred(93));
       assertFalse(c.nearHundred(89));
       assertFalse(c.nearHundred(121));
    }
    @org.junit.Test
    public void has23(){
        assertTrue(c.has23(new int[]{2, 5}));
        assertFalse(c.has23(new int[]{4, 5}));
        assertFalse(c.has23(new int[]{7, 7}));
    }
    @org.junit.Test
    public void middleWay(){
        assertArrayEquals(new int []{2, 5}, c.middleWay(new int []{1, 2, 3}, new int []{4, 5, 6}));
        assertArrayEquals(new int[]{7,8}, c.middleWay(new int []{7, 7, 7}, new int []{3, 8, 0}));

    }

}