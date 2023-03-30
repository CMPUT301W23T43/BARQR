package com.example.barqrxmls;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class PlayerAccount extends AppCompatActivity {

    private ArrayList<Code> CodeDataList;
    private CodeArrayAdapter CodeAdapter;

    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");

    ListView CodesList;

    Taskbar taskbar = Taskbar.getInstance(PlayerAccount.this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_account);

        //get username from intent
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");

        //set username textview
        TextView usernameView = findViewById(R.id.playerName);

        //search for user in database
        String TAG = "PlayerAccount";
        usernameView.setText(username);
        usersRef.document(username.toLowerCase(Locale.ROOT))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "successfully retrieved data");

                            //get player account
                            DocumentSnapshot document = task.getResult();

                            //if the user does not exist exit from the player activity and log the issue
                            if(!document.exists()) {
                                Log.d(TAG, "username in intent does not exist");
                                finish();
                            }

                            User player = document.toObject(User.class);

                            //setup player's list of codes
                            CodeDataList = new ArrayList<>();
                            CodeAdapter = new CodeArrayAdapter(PlayerAccount.this, CodeDataList);
                            CodesList = findViewById(R.id.playerCodesDisplay);
                            CodesList.setAdapter(CodeAdapter);
                            ArrayList<String> playerCodes = new ArrayList<>(player.getCodes().keySet());

                            //add all codes to the listview
                            for (int i = 0; i < playerCodes.size(); i++) {
                                Code code = new Code(playerCodes.get(i));
                                CodeDataList.add(code);
                                CodeAdapter.notifyDataSetChanged();
                            }

                            //set codeTotal and pointTotal TextViews to the player's code and point totals
                            TextView totalScanned = findViewById(R.id.codeTotal);
                            TextView totalPoints = findViewById(R.id.pointTotal);
                            totalPoints.setText(Integer.toString(player.getTotalPoints()));
                            totalScanned.setText(Integer.toString(player.getNumCodes()));

                            //Taskbar
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

                            //setup close button
                            Button close = findViewById(R.id.closeButton);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });

                        }

                        //if task was unsuccessful
                        else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }
}