package com.example.barqrxmls;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest1{

    @Rule
    public ActivityScenarioRule<NewAccount> activityRule = new ActivityScenarioRule<>(NewAccount.class);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Test
    public void testSwitchToMainActivity() {
        onView(withId(R.id.submitButton)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonClickSwitchesActivity2() {
        onView(withId(R.id.usernameField)).check(matches(isDisplayed()));
        onView(withId(R.id.emailField));
        onView(withId(R.id.terms_and_services));
    }
}


///**
// * Tests the switch to leaderboard and that it is running how we intend.
// *
// */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class LeaderboardUITest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);
//
//    @Rule
//    public IntentsRule intentsRule = new IntentsRule();
//
//    @Test
//    public void testSwitchToLeaderboard() {
//        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
//
//    }
//
//    @Test
//    public void checkLeaderList() {
//        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
//        onView(withId(R.id.leaderBoardList)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void checkButtons() {
//        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
//        onView(withId(R.id.playerSearchButton)).check(matches(isDisplayed()));
//        onView(withId(R.id.codeSearchButton)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void checkUserRank() {
//        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
//        onView(withId(R.id.playerRanking)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void checkLeaveLeaderboard() {
//        onView(withId(R.id.leaderBoardButton)).perform(click()).check(matches(isDisplayed()));
//        onView(withId(R.id.homeButton)).perform(click()).check(matches(isDisplayed()));
//
//    }









