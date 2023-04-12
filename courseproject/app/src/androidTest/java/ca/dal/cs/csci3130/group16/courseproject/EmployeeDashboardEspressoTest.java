package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EmployeeDashboardEspressoTest {
    @Rule
    public ActivityTestRule<EmployeeDashboard> myTestRule =
            new ActivityTestRule<EmployeeDashboard>(EmployeeDashboard.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, EmployeeDashboard.class);
                    //result.putExtra("user", new User());
                    return result;
                }
            };

    @Before
    public void setup() throws Exception {
        Intents.init();
    }

    @Test
    public void exploreEmployeeBtn_exists() {
        onView(withId(R.id.exploreEmployerBtn)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
