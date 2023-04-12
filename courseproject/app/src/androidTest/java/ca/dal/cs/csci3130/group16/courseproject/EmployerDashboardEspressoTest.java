package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EmployerDashboardEspressoTest {
    @Rule
    public ActivityTestRule<EmployerDashboard> myTestRule =
            new ActivityTestRule<EmployerDashboard>(EmployerDashboard.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, EmployerDashboard.class);
                    //result.putExtra("user", new User());
                    return result;
                }
            };
    @Before
    public void setup() throws Exception {
        Intents.init();
    }

    @Test
    public void submitJobsButton_exists() {
        onView(withId(R.id.employerSubmitJobButton)).check(matches(isDisplayed()));
    }

    @Test
    public void viewJobsButton_exists() {
        onView(withId(R.id.employerViewJobsButton)).check(matches(isDisplayed()));
    }

    @Test
    public void exploreEmployeeBtn_exists() {
        onView(withId(R.id.exploreEmployeeBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void submitJobsButton_intent() {
        onView(withId(R.id.employerSubmitJobButton)).perform(click());
        intended(hasComponent(SubmitJob.class.getName()));
    }

    @Test
    public void viewJobsButton_intent() {
        onView(withId(R.id.employerViewJobsButton)).perform(click());
        intended(hasComponent(ViewJobs.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
