package ca.dal.cs.csci3130.group16.courseproject;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class loginTest {

    @Rule
    public ActivityScenarioRule<login> myRule = new ActivityScenarioRule<>(login.class);

    @BeforeClass
    public static void setup (){
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        Intents.release();
        System.gc();
    }
    @Test
    public void loginpage(){
        onView(withId(R.id.editEmailLogin)).perform(typeText("email@email.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editPasswordLogin)).perform(typeText("zishul"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginButtonLogin)).perform(click());
        Espresso.closeSoftKeyboard();
        intended(hasComponent(login.class.getName()));
    }

}
