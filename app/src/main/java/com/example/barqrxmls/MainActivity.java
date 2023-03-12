package com.example.barqrxmls;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    final String TAG = "Sample";
    FirebaseFirestore dataBase;
    CollectionReference usersRef;
    CollectionReference codesRef;

    private ArrayList<Code> CodeDataList;
    private CodeArrayAdapter CodeAdapter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ListView CodesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataBase = FirebaseFirestore.getInstance();
        codesRef = dataBase.collection("Codes");
        usersRef = dataBase.collection("Users");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Taskbar taskbar = new Taskbar(MainActivity.this);
        // setting screen changes from taskbar
        // <Praveenkumar, Gary> (<Nov. 9, 2016>) <How to switch between screens?> (<4>) [<source code>] https://stackoverflow.com/questions/7991393/how-to-switch-between-screens

        /**
         * Home Button implementation
         * @author Noah Jeans
         * @version 1
         * @return opens MainActivity which is linked to main_screen.xml
         */
        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(taskbar.getSwitchActivityMap().get("MainActivity"));

        /**
         * LeaderBoard Button implementation
         * @author Noah Jeans
         * @version 1
         * @return opens LeaderBoard which is linked to leaderboard_screen.xml
         */
        ImageButton leaderboard = (ImageButton) findViewById(R.id.leaderBoardButton);
        leaderboard.setOnClickListener(taskbar.getSwitchActivityMap().get("LeaderBoard"));

        /**
         * NewCode Button implementation
         * @author Noah Jeans, Tyler Pollom
         * @version 2
         * @return opens NewCode which is linked to barqr_code.xml
         */
        ImageButton newCode = (ImageButton) findViewById(R.id.newCodeButton);
        newCode.setOnClickListener(taskbar.getSwitchActivityMap().get("NewCode"));

        /**
         * Map Button implementation
         * @author Noah Jeans, Tyler Pollom
         * @version 2
         * @return opens Map which is linked to map.xml
         */
        ImageButton map = (ImageButton) findViewById(R.id.mapButton);
        map.setOnClickListener(taskbar.getSwitchActivityMap().get("Map"));

        /**
         * Account Button implementation
         * @author Noah Jeans, Tyler Pollom
         * @version 2
         * @return opens Account which is linked to account_screen.xml
         */
        ImageButton account = (ImageButton) findViewById(R.id.settingsButton);
        account.setOnClickListener(taskbar.getSwitchActivityMap().get("Account"));



        }



    public void onResume(Bundle savedInstanceState) {
        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        codesRef = dataBase.collection("Codes");
        usersRef = dataBase.collection("Users");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        CodesList = findViewById(R.id.myCodesDisplay);
//        currentUser= mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // Get the current user's ID
//            String currentUserId = currentUser.getUid();
//            if (currentUser != null) {
//                // Get the current user's ID
//                String currentUserEmail = currentUser.getEmail();

        // Create a new User object with the current user's ID and email


                User user = new User(currentUser.toString(), currentUserId.toString(), currentUserEmail.toString());


                CodeDataList = new ArrayList<Code>();
                for (String key : user.getCodes().keySet()) {
                    CodeDataList.add(new Code(key));
                }
                CodeAdapter = new CodeArrayAdapter(this, CodeDataList);
                CodesList.setAdapter(CodeAdapter);


                /**
                 Sets up an OnItemLongClickListener for the CodesList AdapterView in the PlayerAccount activity.
                 When a long click is detected on an item in the CodesList, an AlertDialog is displayed to confirm
                 if the user wants to delete the selected code. If the user confirms deletion, the corresponding code
                 is removed from the current user's codes collection in Firestore.
                 @param parent The AdapterView containing the list of codes.
                 @param view The View representing the selected code in the list.
                 @param position The position of the selected code in the list.
                 @param id The ID of the selected code.
                 @return true if the long click event is consumed, false otherwise.
                 */
                CodesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).
                                setTitle("Delete Code").
                                setMessage("Are you sure you want to delete this Code?").
                                setPositiveButton("Yes", (dialog, which) -> {
                                    String CodeToDelete = (String) parent.getItemAtPosition(position);
                                    Code CodeDeleted = new Code(CodeToDelete);
                                    user.removeCode(CodeToDelete, CodeDeleted.getPoints());
                                }).
                                setNegativeButton("No", (dialog, which) -> {
                                }).
                                create();
                        alertDialog.show();
                        return true;
                    }
                });

}
        }
    }
}
