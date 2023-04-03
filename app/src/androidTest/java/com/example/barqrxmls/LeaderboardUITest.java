package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the switch to leaderboard and that it is running how we intend.
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LeaderboardUITest {

    @BeforeClass
    public static void setup() {
        User test = new User("test","test","test");
        CurrentUser.getInstance().setUser(test);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    /**
     * This test checks that the leaderboard activity is launched properly from main.
     */
    @Test
    public void testSwitchToLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));

    }


    /**
     * This test checks that the leaderboard list is displayed upon the launch of the activity.
     */
    @Test
    public void testLeaderListDisplay() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.leaderBoardList)).check(matches(isDisplayed()));
    }

    /**
     * This test checks that the two search buttons are displayed upon the launch of the activity.
     */

    @Test
    public void testButtonsDisplay() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.playerSearchButton)).check(matches(isDisplayed()));
    }

    /**
     * This test checks that the user ranking is displayed upon the launch of the activity.
     */

    @Test
    public void testUserRankDisplay() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.playerRanking)).check(matches(isDisplayed()));
    }

    /**
     * This test checks that a user can return to the home screen from the leaderboard activty
     * by using the home button on the taskbar.
     */

    @Test
    public void testLeaveLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.homeButton)).perform(click()).check(matches(isDisplayed()));

    }


}
