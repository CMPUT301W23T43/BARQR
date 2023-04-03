package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.CursorMatchers.withRowString;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import android.widget.AdapterView;

import static kotlin.jvm.internal.Intrinsics.checkNotNull;

import android.util.Log;

import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.Current;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * tests the PlayerSearch Activity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PlayerSearchUITest {

    /**
     * sets up the current user
     */
    @BeforeClass
    public static void setup() {
        User test = new User("test","test","test");
        CurrentUser.getInstance().setUser(test);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();
    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    final CollectionReference usersRef = database.collection("Users");

    /**
     * checks that snack pops up when nothing is entered in the searchbar
     */
    @Test
    public void testEnterNothing() {
        User test = new User("test", "test", "test");
        CurrentUser u = CurrentUser.getInstance();
        u.setUser(test);
        onView(withId(R.id.playerSearchButton)).perform(click());
        intended(hasComponent(PlayerSearch.class.getName()));
        onView(withId(R.id.search_bar)).check(matches(isClickable()));
        onView(withId(R.id.search_button)).check(matches(isClickable()));
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.mySnackId)).check(matches(isDisplayed()));
    }

    /**
     * checks if search correctly returns a list of players
     */
    @Test
    public void testSearch() {
        User test = new User("test", "test", "test");
        CurrentUser u = CurrentUser.getInstance();
        u.setUser(test);
        onView(withId(R.id.playerSearchButton)).perform(click());
        intended(hasComponent(PlayerSearch.class.getName()));
        onView(withId(R.id.search_bar)).check(matches(isClickable()));
        onView(withId(R.id.search_button)).check(matches(isClickable()));
        onView(withId(R.id.search_bar)).perform(typeText("a"));
        onView(withId(R.id.search_button)).perform(click());
        onView(withText("anjiphone"));
    }

    /**
     * tests the close button
     */
    @Test
    public void testCloseButton() {
        User test = new User("test", "test", "test");
        CurrentUser u = CurrentUser.getInstance();
        u.setUser(test);
        onView(withId(R.id.playerSearchButton)).perform(click());
        intended(hasComponent(PlayerSearch.class.getName()));
        onView(withId(R.id.closeButton)).check(matches(isClickable()));
        onView(withId(R.id.closeButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }


}
