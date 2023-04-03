package com.example.barqrxmls;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * tests the code screen
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewCodeUITest {

    /**
     * sets up the current user
     */
    @BeforeClass
    public static void setup() {
        User test = new User("test","test","test");
        CurrentUser.getInstance().setUser(test);
    }

    // how to use an intent with ActivityScenarioRule
    // website: StackOverflow https://stackoverflow.com/questions/54179560/how-to-putextra-data-using-newest-activityscenariorule-activityscenarioespress
    // author: Jose Leles https://stackoverflow.com/users/11301807/jose-leles
    // edited by: anotherdave https://stackoverflow.com/users/1474421/anotherdave
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), NewCode.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("code",new Code("test"));
        intent.putExtras(bundle);
    }
    //@Rule
    //public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Rule
    public ActivityScenarioRule<NewCode> newCode = new ActivityScenarioRule<>(intent);

    /**
     * tests that all the correct information is on the screen
     */
    @Test
    public void checkOnScreen() {
        User test = new User("test","test","test");
        CurrentUser c = CurrentUser.getInstance();
        c.setUser(test);
        Code code = new Code("test");
        c.addCode(code,"geolocation");
        c.addComment(code.getHash(),"comment");
        onView(withId(R.id.nameField)).check(matches(withText(code.getName())));
        onView(withId(R.id.commentField));
        onView(withId(R.id.code_geolocation));
        onView(withId(R.id.pointsField)).check(matches(withText(code.getPoints().toString())));
  }


    /**
     * tests user's ability to add a comment
     */
    @Test
    public void checkEditComment() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        CurrentUser c = CurrentUser.getInstance();
        c.setUser(test);
        c.addCode(code,"geolocation");
        c.addComment(code.getHash(),"comment");
        onView(withId(R.id.commentField)).perform(click()).perform(typeText("newComment"));
        onView(withId(R.id.commentField)).check(matches(withText("newComment")));
    }

    /**
     * tests the close button
     */
    @Test
    public void checkCloseButton() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        CurrentUser c = CurrentUser.getInstance();
        c.setUser(test);
        onView(withId(R.id.closeButton)).check(matches(isClickable()));

    }


    /**
     * tests the delete code button
     */
    // how to test dialog
    // website: StackOverflow https://stackoverflow.com/questions/21045509/check-if-a-dialog-is-displayed-with-espresso
   // author: denys https://stackoverflow.com/users/2980933/denys
    @Test
    public void deleteButton() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        CurrentUser c = CurrentUser.getInstance();
        c.setUser(test);
        onView(withId(R.id.deleteCodeButton)).check(matches(isClickable()));
        onView(withId((R.id.deleteCodeButton))).perform(click());
        onView(withText("Are you sure you want to delete this Code?"));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.deleteCodeButton)).check(matches(isClickable()));
        onView(withId((R.id.deleteCodeButton))).perform(click());
        onView(withId(android.R.id.button1)).check(matches(isClickable()));
    }

}
