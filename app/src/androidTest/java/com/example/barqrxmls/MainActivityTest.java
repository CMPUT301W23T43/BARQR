package com.example.barqrxmls;

//shows a weird heirarchy error regarding view.getID which doesn't let me test activity switching


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.ComponentName;

import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    ;
    CollectionReference usersRef = dataBase.collection("Users");
    CollectionReference codesRef = dataBase.collection("Codes");
    @Rule
    public ActivityScenarioRule<NewAccount> activityRule = new ActivityScenarioRule<>(NewAccount.class);

    @Test
    public void testViews() {
        onView(withId(R.id.usernameField));
        onView(withId(R.id.emailField));
        onView(withId(R.id.terms_and_services));
        onView(withId(R.id.submitButton));
    }


    @Test
    public void testButtonClickSwitchesActivity2() {
        onView(withId(R.id.usernameField)).check(matches(isDisplayed()));
        onView(withId(R.id.emailField));
        onView(withId(R.id.terms_and_services));

     onView(withId(R.id.usernameField)).perform(click()).perform(typeText("UserNameTest"));
        onView(withId(R.id.emailField)).perform(click()).perform(typeText("Email"));

        onView(withId(R.id.usernameField)).check(matches(withText("UserNameTest")));
        onView(withId(R.id.emailField)).check(matches(withText("Email")));

        onView(withId(R.id.terms_and_services)).perform(click());

        onView(withId(R.id.submitButton)).perform(click());

        if (usersRef.document("UserNameTest") != null) {
            onView(withId(R.id.accountButton));
            usersRef.document("UserNameTest").delete();

        }

    }
}











