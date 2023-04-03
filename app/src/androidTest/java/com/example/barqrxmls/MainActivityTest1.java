package com.example.barqrxmls;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.ComponentName;

import androidx.test.espresso.intent.rule.IntentsRule;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * tests MainActivity
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest1{

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    /**
     * sets up the current user
     */
    @BeforeClass
    public static void setup() {
        User test = new User("test","test","test");
        CurrentUser.getInstance().setUser(test);
    }

    /**
     * tests that views are correctly displayed
     */
    @Test
    public void testCorrectDisplay() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        CurrentUser c = CurrentUser.getInstance();
        c.setUser(test);
        c.addCode(code,"geolocation");
        //onView(withId(R.id.leaderBoardButton)).perform(click());
        //onView(withId(R.id.homeButton)).perform(click());
        //onView(withId(R.id.codeTotal)).check(matches(withText("1")));
        onView(withId(R.id.pointTotal)).check(matches(isDisplayed()));

    }

    /**
     * tests leaderboard button
     */
    @Test
    public void testSwitchToLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click());
        intended(hasComponent(LeaderBoard.class.getName()));
    }

    /**
     * tests account button
     */
    @Test
    public void testSwitchToAccount() {
        onView(withId(R.id.settingsButton)).perform(click());
        intended(hasComponent(Account.class.getName()));
       }

    /**
     * Test that the taskbar can switch to the New Code view
     */
    @Test
    public void testSwitchToNewCode() {
        onView(withId(R.id.newCodeButton)).perform(click());
        intended(hasComponent(CameraActivity.class.getName()));
    }

    /**
     * Test that the taskbar can switch to the Map view
     */
    @Test
    public void testSwitchToMap() {
        onView(withId(R.id.mapButton)).perform(click());
        intended(hasComponent(Map.class.getName()));
    }

    @AfterClass
    public static void DeleteUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("Users");
        if (usersRef.document("test").get().getResult().exists()) {
            usersRef.document("test").delete();
        }
    }

}




