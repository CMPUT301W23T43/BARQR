package com.example.barqrxmls;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.test.core.app.ActivityScenario;
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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<NewAccount> intentsTestRule =
            new IntentsTestRule<>(NewAccount.class);

    @Test
    public void testButtonClickSwitchesActivity() {
        onView(withId(R.id.usernameField));
        onView(withId(R.id.emailField));
        onView(withId(R.id.terms_and_services));
        onView(withId(R.id.submitButton));
    }


    @Test
    public void testButtonClickSwitchesActivity2() {
        onView(withId(R.id.usernameField));
        onView(withId(R.id.emailField));
        onView(withId(R.id.terms_and_services));
        onView(withId(R.id.submitButton));

    }

        //onView(withId(R.id.submitButton)).perform(click());


//        Intent name = new Intent();
//        Intent email = new Intent();
//        String Username = "kannan";
//        String UserEmail = "kannan@gmail.com";
//        name.putExtra("Username", Username);
//        email.putExtra("UserEmail", UserEmail);
//        ActivityResult result = new ActivityResult(Activity.RESULT_OK, name);
//        ActivityResult result2 = new ActivityResult(Activity.RESULT_OK, email);
//        intending(hasComponent(NewAccount.class.getName())).respondWith(result);
//        intending(hasComponent(NewAccount.class.getName())).respondWith(result2);


        // Click the button





}











