package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * tests the Account activity
 */
@LargeTest
public class AccountUITest {

    /**
     * sets up the current user
     */
    @BeforeClass
    public static void setup() {
        User test = new User("test","test","test");
        CurrentUser.getInstance().setUser(test);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();
    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    final CollectionReference usersRef = database.collection("Users");

    /**
     * checks that views are on screen
     */
    @Test
    public void CorrectlyDisplayed() {
        User test = new User("test", "test", "test");
        CurrentUser u = CurrentUser.getInstance();
        u.setUser(test);
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withText("My Account"));
        onView(withText("Highest Scoring QR Code:"));
        onView(withText("Lowest Scoring QR Code:"));
        onView(withText("Your highest scoring code is ranked "));
        onView(withId(R.id.usernameMyAccount)).check(matches(withText("test")));
        onView(withId(R.id.emailMyAccount)).check(matches(withText("test")));
    }

    /**
     * tests the close button
     */
    @Test
    public void testCloseButton() {
        User test = new User("test", "test", "test");
        CurrentUser u = CurrentUser.getInstance();
        u.setUser(test);
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.close_button)).check(matches(isClickable()));
        onView(withId(R.id.close_button)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

}
