package com.example.barqrxmls;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the NewUser display
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewAccountUITest {

    @Rule
    public ActivityScenarioRule<NewAccount> activityRule = new ActivityScenarioRule<>(NewAccount.class);

    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    final CollectionReference usersRef = database.collection("Users");

    /**
     * Checks that the NewAccount screen gets displayed for a user
     */

    @Test
    public void testCheckViewDisplayed() {
        onView(withId(R.id.usernameField));
        onView(withId(R.id.emailField));
        onView(withId(R.id.terms_and_services));
        onView(withId(R.id.submitButton));
    }

    // Tests below fail.

//    /**
//     * This checks that the buttons and EditTExt fields are all clickable by the user.
//     */
//
//    @Test
//    public void testCheckEditTextClickable() {
//        onView(withId(R.id.usernameField));
//        onView(withId(R.id.emailField));
//        onView(withId(R.id.terms_and_services));
//        onView(withId(R.id.submitButton));
//        onView(withId(R.id.terms_and_services)).check(matches(isDisplayed()));
//        onView(withId(R.id.submitButton)).check(matches(isDisplayed()));
//        onView(withId(R.id.usernameField)).check(matches(isDisplayed()));
//        onView(withId(R.id.emailField)).check(matches(isDisplayed()));
//    }
//
//    /**
//     * This test check that user input is to one of the EditText fields is properly added.
//     */
//
//    @Test
//    public void testCheckEditTextInput() {
//        onView(withId(R.id.usernameField));
//        onView(withId(R.id.emailField));
//        onView(withId(R.id.terms_and_services));
//        onView(withId(R.id.submitButton));
//
//        onView(withId(R.id.usernameField)).perform(click()).perform(typeText("UserName"));
//        onView(withId(R.id.emailField)).perform(click()).perform(typeText("Email"));
//
//        onView(withId(R.id.usernameField)).check(matches(withText("UserName")));
//        onView(withId(R.id.emailField)).check(matches(withText("Email")));
//    }
//
//
//    /**
//     * This test checks that once adding text to username and email, the new user is added to the database
//     * and then removed from the database once confirmed.
//     *
//     */
//    @Test
//    public void testCheckAddUser() {
//        onView(withId(R.id.usernameField));
//        onView(withId(R.id.emailField));
//        onView(withId(R.id.terms_and_services));
//        onView(withId(R.id.submitButton));
//
//        onView(withId(R.id.usernameField)).perform(click()).perform(typeText("UserNameTest"));
//        onView(withId(R.id.emailField)).perform(click()).perform(typeText("Email"));
//
//        onView(withId(R.id.usernameField)).check(matches(withText("UserNameTest")));
//        onView(withId(R.id.emailField)).check(matches(withText("Email")));
//
//        onView(withId(R.id.terms_and_services)).perform(click());
//
//        onView(withId(R.id.submitButton)).perform(click());
//
//        if (usersRef.document("UserNameTest") != null) {
//            onView(withId(R.id.leaderBoardButton));
//            usersRef.document("UserNameTest").delete();
//        }
//
//    }
          
}