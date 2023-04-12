package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SubmitJobsEspressoTest {
    @Rule
    public ActivityTestRule<SubmitJob> myTestRule =
            new ActivityTestRule<SubmitJob>(SubmitJob.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    return new Intent(targetContext, SubmitJob.class);
                }
            };

    @Before
    public void setup() throws Exception {
        Intents.init();
        onView(withId(R.id.editJobType)).perform(click());
        onView(withText("Repairing a computer")).perform(click());

        onView(withId(R.id.editDescription)).perform(typeText("Repairing a computer"));

        onView(withId(R.id.editDate)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2024, 9, 3));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.editPlace)).perform(click());
        onView(
                allOf(withId(com.google.android.libraries.places.R.id.places_autocomplete_search_bar),
                        childAtPosition(
                                allOf(withId(com.google.android.libraries.places.R.id.places_autocomplete_search_bar_container),
                                        childAtPosition(
                                                withId(com.google.android.libraries.places.R.id.places_autocomplete_content),
                                                0)),
                                1),
                        isDisplayed())).perform(replaceText("dalhousie university"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(
                allOf(withId(com.google.android.libraries.places.R.id.places_autocomplete_list),
                        childAtPosition(
                                withId(com.google.android.libraries.places.R.id.places_autocomplete_content),
                                3))).perform(actionOnItemAtPosition(0, click()));

        Thread.sleep(500);
        onView(withId(R.id.editDuration)).perform(typeText("20"));

        onView(withId(R.id.editUrgency)).perform(click());
        onView(withText("Priority 2")).perform(click());
        onView(withId(R.id.editSalary)).perform(closeSoftKeyboard());
        onView(withId(R.id.editSalary)).perform(typeText("23"));
    }

    @Test
    public void fieldsExist() {
        onView(withId(R.id.editJobType)).check(matches(isDisplayed()));
        onView(withId(R.id.editDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.editDate)).check(matches(isDisplayed()));
        onView(withId(R.id.editDuration)).check(matches(isDisplayed()));
        onView(withId(R.id.editPlace)).check(matches(isDisplayed()));
        onView(withId(R.id.editUrgency)).check(matches(isDisplayed()));
        onView(withId(R.id.editSalary)).check(matches(isDisplayed()));
        onView(withId(R.id.submitBtnForNewJob)).check(matches(isDisplayed()));
    }

    @Test
    public void allCorrect() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editDescription)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.jobStatusLabel)).check(matches(withText("OK")));
    }

    @Test
    public void urgencyPopsUp() {
        onView(withId(R.id.editUrgency)).check(matches(withText("Priority 2")));
    }

    @Test
    public void jobTypeFragmentDisplayed() {
        onView(withId(R.id.editJobType)).check(matches(withText("Repairing a computer")));
    }

    @Test
    public void datePopUpDisplayed() {
        onView(withId(R.id.editDate)).check(matches(allOf(withText("SEP 3, 2024"),isDisplayed())));
    }

    @Test
    public void invalidDate() {
        onView(withId(R.id.editDate)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2022, 9, 3));
        onView(withId(android.R.id.button1)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.submitBtnForNewJob)).perform(click());
        onView(withId(R.id.jobStatusLabel)).check(matches(withText("Date must not be in the past")));
    }

    @Test
    public void invalidDuration() {
        onView(withId(R.id.editDuration)).perform(typeText("25"));
        Espresso.pressBack();
        onView(withId(R.id.submitBtnForNewJob)).perform(click());
        onView(withId(R.id.jobStatusLabel)).check(matches(withText("This app is intended for shorter tasks")));
    }

    @Test
    public void emptyDescription() {
        onView(withId(R.id.editDescription)).perform(clearText());
        Espresso.pressBack();
        onView(withId(R.id.submitBtnForNewJob)).perform(click());
        onView(withId(R.id.jobStatusLabel)).check(matches(withText("You must fill all fields")));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}


