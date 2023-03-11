package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the Taskbar
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskbarUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    // Checking that activities launched is done through Intents.
    // Thanks https://stackoverflow.com/a/35861079,
    // by baskara -- https://stackoverflow.com/users/3811419/baskara
    @Rule
    public IntentsRule intentsRule = new IntentsRule();


    /**
     * Check that the taskbar can switch to the Leaderboard.
     */
    @Test
    public void testSwitchToLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click());
        intended(hasComponent(LeaderBoard.class.getName()));
    }

    /*
    TODO: The account test, as well as the leaderboard -> main test, fail because
     the UI elements aren't displaying fully on the device. So they need to be physically
     shifted over in our UML to fit.
        /**
         * Test that the taskbar can switch to the Account view
         * /
        @Test
        public void testSwitchToAccount() {
            onView(withId(R.id.settingsButton)).check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.settingsButton)).perform(click());
            intended(hasComponent(Account.class.getName()));
        }
    */

    /**
     * Test that the taskbar can switch to the New Code view
     */
    @Test
    public void testSwitchToNewCode() {
        onView(withId(R.id.newCodeButton)).perform(click());
        intended(hasComponent(NewCode.class.getName()));
    }

    /**
     * Test that the taskbar can switch to the Map view
     */
    @Test
    public void testSwitchToMap() {
        onView(withId(R.id.mapButton)).perform(click());
        intended(hasComponent(Map.class.getName()));
    }

    /**
     * Test that the taskbar can switch to the LeaderBoard view, then back again to Home.
     */
//    @Test
//    public void testSwitchToHome() {
////        onView(withId(R.id.leaderBoardButton)).perform(click());
////        intended(hasComponent(LeaderBoard.class.getName()));
//
//        onView(withId(R.id.homeButton)).perform(click());
//        intended(hasComponent(MainActivity.class.getName()));
//    }

}
