package ca.dal.cs.csci3130.group16.courseproject;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class LocationFinder_EspressoTest {

    @Rule
    public ActivityScenarioRule<EmployeeDashboard> activityTestRule = new ActivityScenarioRule<>(EmployeeDashboard.class);
    @Test
    public void testEmployeeDashboardLocationText() throws InterruptedException {
        // Wait for 7 seconds
        Thread.sleep(7000);

        // Check if the text in the TextView is "Halifax, Nova Scotia"
        onView(withId(R.id.currentLocation))
                .check(matches(withText("Halifax, Nova Scotia")));
    }
}
