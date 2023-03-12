package com.example.barqrxmls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


        /**
         * Account Button implementation
         * @author Kannan Khosla, Tyler Pollom
         * @version 2
         * @return opens PlayerAccount which is linked to player_account_.xml
         */

        Button Account= (Button) findViewById(R.id.accountButton);
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerAccount.class);
                startActivity(intent);
            }
        });





        // The code below this comment is simply for testing that the database 
        HashMap<String, Object> userInfo = new HashMap<>();
        HashMap<String, Object> codesInfo = new HashMap<>();

        codesInfo.put("Code Name", "First Code");
        codesInfo.put("Hash Value", "abfa85a433a");
        codesInfo.put("Points Value", 3.14159);

        userInfo.put("Name", "Jane Doe");
        userInfo.put("Password", "123");
        userInfo.put("Codes Scanned", Arrays.asList());

        usersRef
                .document("User1")
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data not added"+e);
                    }
                });

        codesRef
                .document("TestCode")
                .set(codesInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data not added"+e);
                    }
                });


        }


}
