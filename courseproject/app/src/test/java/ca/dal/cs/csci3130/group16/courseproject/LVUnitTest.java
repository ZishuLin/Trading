package ca.dal.cs.csci3130.group16.courseproject;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LVUnitTest {

    static Validation validation;

    @Test
    public void testValidCredentials() {
        assertTrue(Validation.validateCredentials("Faisal195", "12345678"));
    }

    @Test
    public void testInvalidCredentials() {
        assertFalse(Validation.validateCredentials("Invalid", "Password"));
    }
}
