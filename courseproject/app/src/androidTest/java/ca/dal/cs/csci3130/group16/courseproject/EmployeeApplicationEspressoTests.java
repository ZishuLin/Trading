package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;

/**
 * This class tests application requirements in the vacuum of TESTApplication. It checks to make
 * sure that a user can press the apply button
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class EmployeeApplicationEspressoTests {
    @Rule
    public ActivityTestRule<TESTApplication> myTestRule =
            new ActivityTestRule<TESTApplication>(TESTApplication.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, TESTApplication.class);
                    return result;
                }
            };
    @Before
    public void setup() throws Exception {
        Intents.init();
    }

    /*
    The first two tests make sure that when the user first sees the job posting it looks correct
     */
    @Test
    public void applyButton_exists() {
        onView(withId(R.id.applyButton)).check(matches(isDisplayed()));
    }

    @Test
    public void appliedTest_doesNotExist() {
        onView(withId(R.id.appliedText)).check(matches(not(isDisplayed())));
    }

    /*
    This test ensures that when the employee clicks the "Apply" button it disappears and a textView
    saying that the application was successful is shown.
     */
    @Test
    public void clickApplyButton() {
        onView(withId(R.id.applyButton)).perform(click());
        onView(withId(R.id.applyButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.appliedText)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
