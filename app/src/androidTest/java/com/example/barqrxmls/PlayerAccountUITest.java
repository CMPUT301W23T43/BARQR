package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * tests the PlayerAccount Activity
 */
public class PlayerAccountUITest {

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
        intent = new Intent(ApplicationProvider.getApplicationContext(), PlayerAccount.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", "demophone");
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<PlayerAccount> activityRule = new ActivityScenarioRule<>(intent);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    /**
     * tests that all views are correctly displayed
     */
    @Test
    public void correctDisplays() {
        onView(withId(R.id.playerName)).check(matches(withText("demophone")));
        onView(withId(R.id.codeTotal));
        onView(withId(R.id.pointTotal));
    }

    /**
     * tests the close button
     */
    @Test
    public void closeButton() {
        onView(withId(R.id.closeButton)).check(matches(isClickable()));
    }

    /**
     * tests the leaderboard button
     */
    @Test
    public void testSwitchToLeaderboard() {
        onView(withId(R.id.leaderBoardButton)).perform(click());
        intended(hasComponent(LeaderBoard.class.getName()));
    }

    /**
     * tests the account button
     */
    @Test
    public void testSwitchToAccount() {
        onView(withId(R.id.settingsButton)).perform(click());
        intended(hasComponent(Account.class.getName()));
    }
    /**
     * Test that the taskbar can switch to the Map view
     */
    @Test
    public void testSwitchToMap() {
        onView(withId(R.id.mapButton)).perform(click());
        intended(hasComponent(Map.class.getName()));
    }
}
