package com.example.barqrxmls;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchQRCodeGeoUITest {

    @BeforeClass
    public static void setup() {
        User test = new User("test","test","test");
        CurrentUser.getInstance().setUser(test);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    public IntentsRule intentsRule = new IntentsRule();
    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    final CollectionReference usersRef = database.collection("Users");

    @Test
    public void clickSearchTest(){
        onView(withId(R.id.codeSearchButton)).perform(click());
        onView(withId(R.id.go_Back)).check(matches(isDisplayed()));
    }

    @Test
    public void returnHomeTest(){
        onView(withId(R.id.codeSearchButton)).perform(click());
        onView(withId(R.id.go_Back)).perform(click());
        onView(withId(R.id.codeSearchButton)).check(matches(isDisplayed()));

    }

    @Test
    public void typeSearchTest(){
        onView(withId(R.id.codeSearchButton)).perform(click());
        onView(withId(R.id.cityCountry)).perform(click()).perform(typeText("edmonton"));
        onView(withId(R.id.cityCountry)).check(matches(withText("edmonton")));

    }

    @Test
    public void searchButtonClickableTest(){
        onView(withId(R.id.codeSearchButton)).perform(click());
        onView(withId(R.id.Search)).check(matches(isClickable()));

    }

}

