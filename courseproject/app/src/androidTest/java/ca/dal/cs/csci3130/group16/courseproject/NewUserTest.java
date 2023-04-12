package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NewUserTest {
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

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ca.dal.cs.csci3130.group16.courseproject", appContext.getPackageName());
    }

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.editFirstName)).perform(typeText("John"));
        onView(withId(R.id.editLastName)).perform(typeText("Doe"));
        onView(withId(R.id.editEmail)).perform(typeText("john@email.com"));
        onView(withId(R.id.editPhoneNumber)).perform(typeText("9026575768"));
        onView(withId(R.id.editPassword)).perform(typeText("rn1234_45"));
        onView(withId(R.id.editConfirmPassword)).perform(typeText("rn1234_45"));
        onView(withId(R.id.CCNumber)).perform(typeText("1234567890123456"));
        onView(withId(R.id.CVVCode)).perform(typeText("676"));
        onView(withId(R.id.CCExpiryMonth)).perform(typeText("08"));
        onView(withId(R.id.CCExpiryYear)).perform(typeText("24"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editUsername)).perform(typeText("johndoe"));
    }

    @Test
    public void fieldsExist() {
        onView(withId(R.id.editFirstName)).check(matches(isDisplayed()));
        onView(withId(R.id.editLastName)).check(matches(isDisplayed()));
        onView(withId(R.id.editPhoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.editEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.CCNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.CVVCode)).check(matches(isDisplayed()));
        onView(withId(R.id.CCExpiryYear)).check(matches(isDisplayed()));
        onView(withId(R.id.CCExpiryMonth)).check(matches(isDisplayed()));
        onView(withId(R.id.editUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.editPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.editConfirmPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void checkForEmptyFields() {
        onView(withId(R.id.editFirstName)).perform(clearText());
        onView(withId(R.id.editPhoneNumber)).perform(clearText());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.FIELD_EMPTY_ERR)));
    }

    @Test
    public void checkCCNumWrongLength() {
        onView(withId(R.id.CCNumber)).perform(clearText());
        onView(withId(R.id.CCNumber)).perform(typeText("999"));
        Espresso.closeSoftKeyboard();
//        Espresso.pressBack();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.CCNUM_INVALID_ERR)));
    }

    @Test
    public void checkCCNumValidLength() {
        onView(withId(R.id.CCNumber)).perform(replaceText("0123456789123456"));
//        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.EMPTY_STRING)));
    }

    @Test
    public void checkPhoneWrongLength() {
        onView(withId(R.id.editPhoneNumber)).perform(replaceText("902"));
//        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.PHONE_ILLEGAL_FORMAT_ERR)));
    }

    @Test
    public void checkPhoneNotLocal() {
        onView(withId(R.id.editPhoneNumber)).perform(replaceText("4431231234"));
        Espresso.pressBack();
//        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.PHONE_NOT_CANADIAN_ERR)));
    }

    @Test
    public void checkPhoneValid() {
        onView(withId(R.id.editPhoneNumber)).perform(replaceText("9021231234"));
        Espresso.pressBack();
//        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.EMPTY_STRING)));
    }

    @Test
    public void checkUsernameWrongLength() {
        onView(withId(R.id.editUsername)).perform(replaceText("a"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.USERNAME_ILLEGAL_LENGTH_ERR)));

        onView(withId(R.id.editUsername)).perform(replaceText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.USERNAME_ILLEGAL_LENGTH_ERR)));

    }

    @Test
    public void checkUsernameIllegalChar() {
        onView(withId(R.id.editUsername)).perform(replaceText("ab!2#qweq"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.USERNAME_ILLEGAL_CHAR_ERR)));
    }

    @Test
    public void checkUsernameValid() {
        onView(withId(R.id.editUsername)).perform(replaceText("abcdef"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.EMPTY_STRING)));
    }

    @Test
    public void checkPasswordWrongLength() {
        onView(withId(R.id.editPassword)).perform(replaceText("a"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.PSSWD_ILLEGAL_FORMAT_ERR)));
    }

    @Test
    public void checkPasswordValid() {
        onView(withId(R.id.editPassword)).perform(replaceText("abcdefgh!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editConfirmPassword)).perform(replaceText("abcdefgh!"));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.EMPTY_STRING)));
    }

    @Test
    public void checkPasswordsDifferent() {
        onView(withId(R.id.editPassword)).perform(replaceText("abcdefgh!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editConfirmPassword)).perform(replaceText("xyzabc"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.CPSSWD_DIFFERENT_ERR)));
    }

    @Test
    public void checkPasswordsMatch() {
        onView(withId(R.id.editPassword)).perform(replaceText("abcdefgh!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editConfirmPassword)).perform(replaceText("abcdefgh!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.errorLabel)).check(matches(withText(R.string.EMPTY_STRING)));
    }

    @After
    public void tearDown() throws Exception {
    }
}