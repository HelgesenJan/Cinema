package Kontroll;

import junit.framework.TestCase;
import org.junit.Test;

public class BrukerTest extends TestCase {


    /**
     * Tester et par brukere og sjekker om pin er riktig
     */
    @Test
    public void testRiktigPin()  {


        Bruker bruker1 = new Bruker("knut", "1234", false);
        Bruker bruker2 = new Bruker("tunk", "4321", true);

        assertEquals(true, bruker1.riktigPin("1234"));
        assertEquals(false, bruker1.riktigPin("4321"));
        assertEquals(false, bruker1.riktigPin("agdgdf"));
        assertEquals(false, bruker1.erPlanlegger());

        assertEquals(true, bruker2.riktigPin("4321"));
        assertEquals(false, bruker2.riktigPin("1234"));
        assertEquals(false, bruker2.riktigPin("fdddfg"));
        assertEquals(true, bruker2.erPlanlegger());


    }

}