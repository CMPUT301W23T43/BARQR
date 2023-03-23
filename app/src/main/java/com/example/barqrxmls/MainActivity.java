package com.example.barqrxmls;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    User currentTestUser;

    Code testScannedCode;
    Bitmap testScannedCodeImage;

    ActivityResultLauncher<Intent> loginLauncher;
    ActivityResultLauncher<Intent> cameraLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Citation: https://www.youtube.com/watch?v=DfDj9EadOLk
        // from youtube channel https://www.youtube.com/@DailyCoding Daily Coding.
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Use the camera data to build a new Code object.
                        if (result.getResultCode() == 15) {
                            Intent camResultIntent = result.getData();

                            if (camResultIntent != null) {
                                Bundle cameraResults = camResultIntent.getExtras();
                                Log.d(TAG, "onActivityResult: ".concat(cameraResults.get("codeData").toString()));
                                testScannedCode = new Code(cameraResults.get("codeData").toString());
                                testScannedCodeImage = (Bitmap) cameraResults.get("codeImage");
                                currentTestUser.addCode(testScannedCode.getHash(), testScannedCode.getPoints());
                                codesRef.document(testScannedCode.getHash()).set(testScannedCode);
                                CodeDataList.add(testScannedCode);
                                CodeAdapter.notifyDataSetChanged();
                                updateCountTextViews(currentTestUser, CodeDataList);
                            }
                        }
                    }
                });
        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle bundle = result.getData().getExtras();
                currentTestUser = (User)bundle.get("User");
                codesRef = dataBase.collection("Codes");
                usersRef = dataBase.collection("Users");

                CodesList = findViewById(R.id.myCodesDisplay);
                CodeDataList = new ArrayList<Code>();
                CodeAdapter = new CodeArrayAdapter(MainActivity.this, CodeDataList);
                CodesList.setAdapter(CodeAdapter);
                doStuff(currentTestUser);
            }
        });
        Intent loginIntent = new Intent(this, NewAccount.class);
        loginLauncher.launch(loginIntent);
    }


    /**
     * Performs necessary operations to retrieve and display codes associated with the current user.
     *
     * @param currentUserTest The User object representing the current user.
     */
    public void doStuff(User currentUserTest) {
        System.out.println("After attempting to convert to User object" + currentUserTest);

        for (String key : currentUserTest.getCodes().keySet()) {
            codesRef.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        System.out.println("Trying to convert database object with hash " + key);
                        Code userCode = task.getResult().toObject(Code.class);
                        if (userCode == null) {
                            System.out.println("Our converted Code object was null");
                        } else {
                            System.out.println("Document doStuff adding code: " + userCode);
                            CodeDataList.add(userCode);
                            CodeAdapter.notifyDataSetChanged();
                            updateCountTextViews(currentUserTest, CodeDataList);
                        }
                    }
                }
            });
        }
//        CodeAdapter.notifyDataSetChanged();
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
                            updateCountTextViews(currentUserTest, CodeDataList);
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

    public void updateCountTextViews(User currentUserTest, ArrayList<Code> codeDataList) {
        TextView codeCountTV = findViewById(R.id.codeTotal);
        codeCountTV.setText(String.valueOf(codeDataList.size()));

        TextView codePointsTV = findViewById(R.id.pointTotal);
        codePointsTV.setText(String.valueOf(currentUserTest.getTotalPoints()));
    }

    public void onResume() {
        super.onResume();
        //setContentView(R.layout.main_screen);
        System.out.print("Inside onResume");
//        dataBase = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();

        Taskbar taskbar = new Taskbar(MainActivity.this);
        String id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID).replace("/","_");
        // setting screen changes from taskbar
        // <Praveenkumar, Gary> (<Nov. 9, 2016>) <How to switch between screens?> (<4>) [<source code>] https://stackoverflow.com/questions/7991393/how-to-switch-between-screens

        /**
         * Home Button implementation
         * @author Noah Jeans
         * @version 1
         * @return opens MainActivity which is linked to main_screen.xml
         */
        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
//        home.setOnClickListener(taskbar.getSwitchActivityMap().get("MainActivity"));

        /**
         * LeaderBoard Button implementation
         * @author Noah Jeans
         * @version 1
         * @return opens LeaderBoard which is linked to leaderboard_screen.xml
         */
        ImageButton leaderboard = (ImageButton) findViewById(R.id.leaderBoardButton);
        //leaderboard.setOnClickListener(taskbar.getSwitchActivityMap().get("LeaderBoard"));
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderIntent = new Intent(MainActivity.this, LeaderBoard.class);
                leaderIntent.putExtra("id", id);
                startActivity(leaderIntent);
            }
        });

        /**
         * NewCode Button implementation
         * @author Noah Jeans, Tyler Pollom
         * @version 2
         * @return opens NewCode which is linked to barqr_code.xml
         */
        ImageButton newCode = (ImageButton) findViewById(R.id.newCodeButton);
        newCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                cameraLauncher.launch(cameraIntent);
            }
        });

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
}
