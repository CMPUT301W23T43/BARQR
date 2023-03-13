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

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Test
    public void testSwitchToLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));

    }

    @Test
    public void checkLeaderList() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.leaderBoardList)).check(matches(isDisplayed()));
    }

    @Test
    public void checkButtons() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.playerSearchButton)).check(matches(isDisplayed()));
        onView(withId(R.id.codeSearchButton)).check(matches(isDisplayed()));
    }

    @Test
    public void checkUserRank() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.playerRanking)).check(matches(isDisplayed()));
    }

    @Test
    public void checkLeaveLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.homeButton)).perform(click()).check(matches(isDisplayed()));

    }


}
