package com.example.barqrxmls;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Taskbar class, which is responsible for creating the Listeners that will
 * be called when an OnClickListener is required for a Taskbar UI button.
 */
public class Taskbar {
    HashMap<String, OnClickListener> switchActivityMap;
    Context callerContext;

    /**
     * Construct a default taskbar
     *
     * This constructor creates a default taskbar for the current layout of the app.
     * Internally, it calls the second constructor with a default list of arguments.
     * @param context The caller's context, required for view switching to work.
     */
    public Taskbar(Context context) {
        this(   context,
                new ArrayList<>(Arrays.asList(MainActivity.class, LeaderBoard.class, Map.class, NewCode.class, Account.class)),
                new ArrayList<>(Arrays.asList("MainActivity", "LeaderBoard", "Map", "NewCode", "Account"))
        );
    }

    // Class generic creation style from StackOverflow (<? extends ...> and <T extends ...>)
    // Author Joshua Gainey https://stackoverflow.com/users/11390398/joshua-gainey
    // Source:https://stackoverflow.com/a/55824729
    /**
     * Construct a hashmap of ActivityName, OnClickListener entries.
     *
     * Create a hash map where the keys are Strings corresponding to the name of the class to switch
     * to, and the values are OnClickListeners that switch to that activity.
     *
     * @param context The context of the caller activity.
     * @param classList List of classes, such as MainActivity.class or HomeActivity.class
     * @param activityNames List of strings corresponding 1:1 to class list.
     */
    public Taskbar(Context context, ArrayList<Class<? extends AppCompatActivity>> classList, ArrayList<String> activityNames) {
        this.callerContext = context;
        assert(classList.size() == activityNames.size());
        HashMap<String, OnClickListener> listenerHashMap = new HashMap<>();
        for (int i = 0; i < classList.size(); i++) {
            listenerHashMap.put(activityNames.get(i), createListener(classList.get(i)));
        }
        switchActivityMap = listenerHashMap;
    }


    /**
     * Create an OnClickListener that swaps from the given Context to the target activity.
     *
     * @param target The target activity to switch to. Must be a Class of type T.
     * @return OnClickListener with behaviour of switching to target activity.
     * @param <T> A class that extends AppCompatActivity.
     */
    private <T extends AppCompatActivity> OnClickListener createListener(Class<T> target) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(callerContext, target);
                callerContext.startActivity(homeIntent);
            }
        };
    }

    public HashMap<String, OnClickListener> getSwitchActivityMap() {
        return switchActivityMap;
    }
}