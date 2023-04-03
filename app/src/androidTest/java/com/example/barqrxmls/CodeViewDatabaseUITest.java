package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isNotClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CodeViewDatabaseUITest {
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), CodeViewDatabase.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("code",new Code("test"));
        intent.putExtras(bundle);
    }

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Rule
    public ActivityScenarioRule<NewCode> newCode = new ActivityScenarioRule<>(intent);

    /**
     * tests that all the correct information is on the screen
     */
    @Test
    public void checkOnScreen() {
        Code code = new Code("test");
        onView(withId(R.id.nameField)).check(matches(withText(code.getName())));
        onView(withId(R.id.commentField));
        onView(withId(R.id.code_geolocation));
        onView(withId(R.id.pointsField)).check(matches(withText(code.getPoints().toString())));
    }


    /**
     * tests user's inability to add a comment
     */
    @Test
    public void checkEditComment() {
        onView(withId(R.id.commentField)).check(matches(isNotClickable()));

    }

    /**
     * tests the close button
     */
    @Test
    public void checkCloseButton() {
        onView(withId(R.id.closeButton)).check(matches(isClickable()));

    }

}
