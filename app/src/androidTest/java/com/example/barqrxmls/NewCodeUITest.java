package com.example.barqrxmls;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.Current;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
public class NewCodeUITest {
    public Code code = new Code("test");
    public Intent intent = new Intent().putExtra("code",code);
    public Bundle bundle = intent.getExtras();
    CurrentUser c = CurrentUser.getInstance();

    // Fuck intent tests :)

    @Rule
    public ActivityScenarioRule<NewCode> activityRule = new ActivityScenarioRule<>(NewCode.class,bundle);
    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Test
    public void checkOnScreen() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        c.setUser(test);
        c.addCode(code,"geolocation");
        c.addComment(code.getHash(),"comment");
        onView(withId(R.id.nameField)).check(matches(withText(code.getName())));
        onView(withId(R.id.commentField)).check(matches(withText(CurrentUser.getInstance().getComment(code.getHash()))));
        onView(withId(R.id.code_geolocation)).check(matches(withText(CurrentUser.getInstance().getGeoLocation(code.getHash()))));
        onView(withId(R.id.pointsField)).check(matches(withText(code.getPoints().toString())));
    }

    @Test
    public void checkEditComment() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        c.setUser(test);
        c.addCode(code,"geolocation");
        c.addComment(code.getHash(),"comment");
        intending(toPackage("com.android.MainActivity")).respondWith(null);
        onView(withId(R.id.commentField)).perform(click()).perform(typeText("newComment"));
        onView(withId(R.id.closeButton)).perform(click());
        assert(CurrentUser.getInstance().getComment(code.getHash())).equals("newComment");
    }

    @Test
    public void checkCloseButton() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        c.setUser(test);
        c.addCode(code,"geolocation");
        c.addComment(code.getHash(),"comment");
        onView(withId(R.id.closeButton)).check(matches(isClickable()));
        onView(withId(R.id.closeButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }


    // how to test dialog
    // website: StackOverflow https://stackoverflow.com/questions/21045509/check-if-a-dialog-is-displayed-with-espresso
    // author: denys https://stackoverflow.com/users/2980933/denys
    @Test
    public void deleteButton() {
        Code code = new Code("test");
        User test = new User("test","test","test");
        c.setUser(test);
        c.addCode(code,"geolocation");
        c.addComment(code.getHash(),"comment");
        onView(withId(R.id.deleteCodeButton)).check(matches(isClickable()));
        onView(withId((R.id.deleteCodeButton))).perform(click());
        onView(withText("Are you sure you want to delete this Code?"));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.deleteCodeButton)).check(matches(isClickable()));
        onView(withId((R.id.deleteCodeButton))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        boolean check = CurrentUser.getInstance().hasComment(code.getHash());
        assert(!check);
    }

    @AfterClass
    public static void deleteUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("Users");
        if (usersRef.document("test").get().getResult().exists()) {
            usersRef.document("test").delete();
        }
    }

}
