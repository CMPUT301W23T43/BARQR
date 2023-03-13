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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    final String TAG = "Sample";
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");
    CollectionReference codesRef = dataBase.collection("Codes");

    private ArrayList<Code> CodeDataList;
    private CodeArrayAdapter CodeAdapter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ListView CodesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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



        Code testCode1 = new Code("/usr/code1");
        Code testCode2 = new Code(";lkajsdf");
        Code testCode3 = new Code("Smithy");
        HashMap<String, String> codesHashMap = new HashMap<>();
        codesHashMap.put(testCode1.getHash(), "");
        codesHashMap.put(testCode2.getHash(), "");
        codesHashMap.put(testCode3.getHash(), "");
        DocumentReference currentUser = usersRef.document("CurrentUser");
        currentUser.update("codes", codesHashMap);

        codesRef.document(testCode1.getHash()).set(testCode1);
        codesRef.document(testCode2.getHash()).set(testCode2);
        codesRef.document(testCode3.getHash()).set(testCode3);

        System.out.println("Inside onCreate");
        codesRef = dataBase.collection("Codes");
        usersRef = dataBase.collection("Users");



//        mAuth = FirebaseAuth.getInstance();
        CodesList = findViewById(R.id.myCodesDisplay);
//        currentUser= mAuth.getCurrentUser();

        DocumentReference currentUserRef = usersRef.document("CurrentUser");
        final User[] users = new User[1];
        User currentUserTest;
        currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                System.out.println("Just before conversion");
                if (task.isSuccessful()) {
                    users[0] = task.getResult().toObject(User.class);
                    doStuff(users[0]);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed to convert to User");
            }
        });



    }

    public void doStuff(User currentUserTest) {
        System.out.println("After attempting to convert to User object" + currentUserTest);
        CodeDataList = new ArrayList<Code>();
        CodeAdapter = new CodeArrayAdapter(this, CodeDataList);
        CodesList.setAdapter(CodeAdapter);
        for (String key : currentUserTest.getCodes().keySet()) {
            codesRef.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Code userCode = task.getResult().toObject(Code.class);
                        CodeDataList.add(userCode);
                        CodeAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        CodeAdapter.notifyDataSetChanged();
        System.out.println("Coded Data List:" + CodeDataList);

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

                            Code CodeToDelete = CodeDataList.get(position);
                            currentUserTest.removeCode(CodeToDelete.getHash(), CodeToDelete.getPoints());
                            CodeDataList.remove(position);
                            CodeAdapter.notifyDataSetChanged();
                        }).
                        setNegativeButton("No", (dialog, which) -> {
                            CodeAdapter.notifyDataSetChanged();
                        }).
                        create();
                alertDialog.show();
                return true;
            }
        });

    }
}

//    public void onResume() {
//        super.onResume();
//        setContentView(R.layout.main_screen);
//        System.out.print("Inside onResume");
//        dataBase = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
//
//    }
//}
