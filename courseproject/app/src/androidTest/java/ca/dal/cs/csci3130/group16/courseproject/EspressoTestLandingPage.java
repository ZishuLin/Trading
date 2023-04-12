package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.provider.ContactsContract;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTestLandingPage
{
    @Rule
    public ActivityScenarioRule<LandingPage> myRule = new ActivityScenarioRule<>(LandingPage.class);

    @BeforeClass
    public static void setup ()
    {
        Intents.init();
    }

    @AfterClass
    public static void tearDown()
    {
        Intents.release();
        System.gc();
    }

    @Test
    public void employeeButtonSwitch()
    {
        onView(withId(R.id.EmployeeButton)).perform(click());
        intended(hasExtra("IsEmployee",true));
    }

    @Test
    public void employerButtonSwitch()
    {
        onView(withId(R.id.EmployerButton)).perform(click());
        intended(hasExtra("IsEmployee",false));
    }

}
