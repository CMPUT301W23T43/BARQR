package com.example.barqrxmls;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
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
public class PlayerScansUITest {
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), PlayerScans.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("code",new Code("test"));
        intent.putExtras(bundle);
    }

    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Rule
    public ActivityScenarioRule<NewCode> newCode = new ActivityScenarioRule<>(intent);

    /**
     * checks that playerscans correctly displays list of users that have scanned a given code
     */
    @Test
    public void checkUserOnScreen() {
        onView(withText("test"));
    }

    /**
     * tests the close button
     */
    @Test
    public void checkCloseButton() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        CurrentUser c = CurrentUser.getInstance();
        c.setUser(test);
        onView(withId(R.id.closeButton)).check(matches(isClickable()));

    }

}
