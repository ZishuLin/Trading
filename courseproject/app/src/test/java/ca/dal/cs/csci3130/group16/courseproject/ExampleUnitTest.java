package ca.dal.cs.csci3130.group16.courseproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Calendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void date_isCorrect() {
        assertFalse(SubmitJobValidator.rawIsBadDate(new Calendar.Builder().setDate(2025, 2, 2).build().getTime()));
    }

    @Test
    public void date_isWrong() {
        assertTrue(SubmitJobValidator.rawIsBadDate(new Calendar.Builder().set(Calendar.YEAR, 2010).build().getTime()));
    }

    @Test
    public void duration_isCorrect() {
        assertFalse(SubmitJobValidator.rawIsBadDuration(24));
    }

    @Test
    public void duration_isWrong() {
        assertTrue(SubmitJobValidator.rawIsBadDuration(25));
    }
}