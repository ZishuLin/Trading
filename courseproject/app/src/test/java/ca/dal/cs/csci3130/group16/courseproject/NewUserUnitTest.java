package ca.dal.cs.csci3130.group16.courseproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ca.dal.cs.csci3130.group16.courseproject.NewUser.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewUserUnitTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        System.gc();
    }

    @Test
    public void check_hasEmailFormat_valid() {
        assertTrue(hasEmailFormat("user@example.com"));
    }

    @Test
    public void check_hasEmailFormat_no_dot() {
        assertFalse(hasEmailFormat("user@examplecom"));
    }

    @Test
    public void check_hasEmailFormat_no_at() {
        assertFalse(hasEmailFormat("userexample.com"));
    }

    @Test
    public void check_hasCanadianAreaCode_valid() {
        int[] canadianCodes = {
                204, 226, 236, 249, 250, 289, 306, 343, 365, 367, 368, 403, 416, 418, 431,
                437, 438, 450, 474, 506, 514, 519, 548, 579, 581, 587, 604, 613, 639, 647,
                672, 683, 705, 709, 742, 753, 778, 780, 782, 807, 819, 825, 867, 873, 902,
                905, 263, 354, 382, 428, 468, 584, 879
        };
        for (int canadianCode : canadianCodes) {
            String s = canadianCode + "1234567";
            assertTrue(hasCanadianAreaCode(s));
        }
    }

    @Test
    public void check_hasCanadianAreaCode_invalid() {
        assertFalse(hasCanadianAreaCode("4431234567"));
    }

    @Test
    public void check_isValidPhoneLength_valid() {
        assertTrue(isValidPhoneLength("0123456789"));
    }

    @Test
    public void check_isValidPhoneLength_short() {
        assertFalse(isValidPhoneLength("012345678"));
    }

    @Test
    public void check_isValidPhoneLength_long() {
        assertFalse(isValidPhoneLength("01234567890"));
    }

    @Test
    public void check_isUnameValidLength_shortValid() {
        assertTrue(isUnameValidLength("abcdef"));
    }

    @Test
    public void check_isUnameValidLength_longValid() {
        assertTrue(isUnameValidLength("abcdefghijklmnopqrstuvwxyz0123"));
    }

    @Test
    public void check_isUnameValidLength_short() {
        assertFalse(isUnameValidLength("abcde"));
    }

    @Test
    public void check_isUnameValidLength_long() {
        assertFalse(isUnameValidLength("abcdefghijklmnopqrstuvwxyz01234"));
    }

    @Test
    public void check_isUnameValidChars_valid() {
        assertTrue(isUnameValidChars("abcde-ABCDE-0123"));
    }

    @Test
    public void check_isUnameValidChars_invalid_space() {
        assertFalse(isUnameValidChars("1 2"));
    }

    @Test
    public void check_isUnameValidChars_invalid_exclaim() {
        assertFalse(isUnameValidChars("1!2"));
    }

    @Test
    public void check_isValidPsswdLength_valid() {
        assertTrue(isValidPsswdLength("012345"));
    }

    @Test
    public void check_isValidPsswdLength_short() {
        assertFalse(isValidPsswdLength("01234"));
    }

    @Test
    public void check_hasSpecialChar_valid() {
        assertTrue(hasSpecialChar("#a!"));
    }

    @Test
    public void check_hasSpecialChar_invalid() {
        assertFalse(hasSpecialChar("abcde"));
    }
}
