package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LVEspressoTest {

    @Rule
    public ActivityScenarioRule<Validation> activityTestRule = new ActivityScenarioRule<>(Validation.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ca.dal.cs.csci3130.group16.courseproject", appContext.getPackageName());
    }

    @Test
    public void testValidCredentials() {
        // Type valid credentials into the username and password fields
        onView(withId(R.id.username_edit_text)).perform(typeText("Faisal195"));
        onView(withId(R.id.password_edit_text)).perform(typeText("12345678"));
        Espresso.closeSoftKeyboard();
        // Click the login button
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void testInvalidCredentials() {
        // Type invalid credentials into the username and password fields
        onView(withId(R.id.username_edit_text)).perform(typeText("Invalid"));
        onView(withId(R.id.password_edit_text)).perform(typeText("Password"));
        Espresso.closeSoftKeyboard();
        // Click the login button
        onView(withId(R.id.login_button)).perform(click());
    }
}