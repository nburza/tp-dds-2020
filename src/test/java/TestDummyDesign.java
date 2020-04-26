import org.junit.Assert;
import org.junit.Test;

public class TestDummyDesign {

    @Test
    public void testIntegrante1() { Assert.assertEquals(DummyDesign.integrante1(),1);}

    @Test
    public void testIntegrante2(){
        Assert.assertEquals(DummyDesign.integrante2(),2);
    }

    @Test
    public void testIntegrante3() { Assert.assertEquals(DummyDesign.integrante3(),3); }

    @Test
    public void testIntegrante4() { Assert.assertEquals(DummyDesign.integrante4(),4); }
}