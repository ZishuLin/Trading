package ca.dal.cs.csci3130.group16.courseproject;

// These imports are adapted from CSCI 3130 Assignment 2 code, by Masud Rahman

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Espresso tests for CC validation and CC type checking
 */

@RunWith(AndroidJUnit4.class)
public class CCEspressoTest {

    @Rule
    public ActivityTestRule<NewUser> myTestRule =
            new ActivityTestRule<NewUser>(NewUser.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, NewUser.class);
                    result.putExtra("IsEmployee", true);
                    return result;
                }
            };
    public IntentsRule myIntentRule = new IntentsRule();


    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        Intents.release();
        System.gc();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ca.dal.cs.csci3130.group16.courseproject", appContext.getPackageName());
    }

    /**
     * Initial test to see if the fields are all visible and they start with an empty string
     */
    @Test
    public void checkIfFieldsAreVisible() {
        onView(withId(R.id.CCNumber)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.CVVCode)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.CCExpiryMonth)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.CCExpiryYear)).check(matches(withText(R.string.EMPTY_STRING)));
    }

    /**
     * Test to see if input is limited to the max number of characters for a field
     * CC number max 16 characters
     * CVV code max 3 characters
     * Expiry month and year max 2 characters
     */
    @Test
    public void checkCharacterLimit() {
        onView(withId(R.id.CCNumber)).perform(typeText("12345678901234567890"));
        onView(withId(R.id.CCNumber)).check(matches(withText("1234567890123456")));

        onView(withId(R.id.CVVCode)).perform(typeText("123456"));
        onView(withId(R.id.CVVCode)).check(matches(withText("123")));

        onView(withId(R.id.CCExpiryMonth)).perform(typeText("123456"));
        onView(withId(R.id.CCExpiryMonth)).check(matches(withText("12")));

        onView(withId(R.id.CCExpiryYear)).perform(typeText("123456"));
        onView(withId(R.id.CCExpiryYear)).check(matches(withText("12")));
    }

    /**
     * Test to see if CC type is correctly determined and displayed after entering CC number
     * and changing focus from the CC number field
     */
    @Test
    public void checkCCType() {
        onView(withId(R.id.CCTypeText)).check(matches(withText(R.string.EMPTY_STRING)));

        onView(withId(R.id.CCNumber)).perform(typeText("4321"));
        onView(withId(R.id.CVVCode)).perform(typeText("123"));
        onView(withId(R.id.CCTypeText)).check(matches(withText(R.string.CC_VISA)));
        onView(withId(R.id.CCNumber)).perform(clearText());

        onView(withId(R.id.CCNumber)).perform(typeText("5321"));
        onView(withId(R.id.CVVCode)).perform(typeText("123"));
        onView(withId(R.id.CCTypeText)).check(matches(withText(R.string.CC_MASTER)));
        onView(withId(R.id.CCNumber)).perform(clearText());

        onView(withId(R.id.CCNumber)).perform(typeText("3321"));
        onView(withId(R.id.CVVCode)).perform(typeText("123"));
        onView(withId(R.id.CCTypeText)).check(matches(withText(R.string.CC_AMEX)));
        onView(withId(R.id.CCNumber)).perform(clearText());

        onView(withId(R.id.CCNumber)).perform(typeText("1"));
        onView(withId(R.id.CVVCode)).perform(typeText("123"));
        onView(withId(R.id.CCTypeText)).check(matches(withText(R.string.EMPTY_STRING)));
    }

}
