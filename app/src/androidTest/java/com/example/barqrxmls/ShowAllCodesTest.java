package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowAllCodesTest {

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
     * This test checks that a user can switch to the all codes display from the leaderboard.
     */
    @Test
    public void testSwitchToCodeDisplay() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.playerSearchButton)).perform(click());
        onView(withId(R.id.codesList)).check(matches(isDisplayed()));
    }

    /**
     * This test checks that the user can switch back to the leaderboard successfully after going
     * to view the all codes page.
     */
    @Test
    public void testSwitchBacktoLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.playerSearchButton)).perform(click());
        onView(withId(R.id.codesList)).check(matches(isDisplayed()));
        onView(withId(R.id.returnToLeaderboard)).perform(click());
        onView(withId(R.id.playerSearchButton)).check(matches(isDisplayed()));

    }
}
