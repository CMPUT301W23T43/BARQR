
package com.example.barqrxmls;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * This class displays the leaderboard along with the current player's (the app user)
 * ranking amongst all the other players in the database.
 * @author Anjelica Marianicz
 */
public class LeaderBoard extends AppCompatActivity {
    final String TAG = "Sample";
    private String currentUser;

    CollectionReference usersRef;

    private ListView leaderBoard;
    private LeaderboardAdapter boardAdapter;
    LinkedHashMap<Object, Object> leaders;
    LinkedHashMap<Object, Object> rankings;

    TextView userRank;
    Button ShowAllCodes;

    /**
     * Upon creation the users are grabbed from the database and sorted by their player scores.
     * The onCreate method below handles this
     * @param savedInstanceState
     *
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_screen);

        Taskbar taskbar = Taskbar.getInstance(LeaderBoard.this);
        CurrentUser currentUser = CurrentUser.getInstance();
        // setting screen changes from taskbar
        // <Praveenkumar, Gary> (<Nov. 9, 2016>) <How to switch between screens?> (<4>) [<source code>] https://stackoverflow.com/questions/7991393/how-to-switch-between-screens

        // Button to return to homepage of application.
        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(taskbar.getSwitchActivityMap().get("MainActivity"));



        // Button to change to the map of the application.
        ImageButton map = (ImageButton) findViewById(R.id.mapButton);
        map.setOnClickListener(taskbar.getSwitchActivityMap().get("Map"));

        // Button to view the current user's account information.
        ImageButton account = (ImageButton) findViewById(R.id.settingsButton);
        account.setOnClickListener(taskbar.getSwitchActivityMap().get("Account"));
        ShowAllCodes = findViewById(R.id.playerSearchButton);

        usersRef = FirebaseFirestore.getInstance().collection("Users");
        leaderBoard = findViewById(R.id.leaderBoardList);

        Query getUsersByScores = usersRef.orderBy("totalPoints", Query.Direction.DESCENDING);

        List<User> queryUsers = new ArrayList<>();

        userRank = findViewById(R.id.playerRanking);
        leaders = new LinkedHashMap<>();
        rankings = new LinkedHashMap<>();
        // Query to grab all users from the database, sorted by their total points (descending order)
        getUsersByScores.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, String.valueOf(document.get("userName")));
                        leaders.put(document.get("userName"), document.get("totalPoints"));
                        rankings.put(document.get("id"), i);
                        i++;

                    }
                    updateList();
                    userRank.setText(rankings.get(currentUser.getId()).toString());
                }
            }
        });
        // On click, switches to view all codes within the database.
        ShowAllCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the next activity
                Intent intent = new Intent(LeaderBoard.this, ShowAllQrCodes.class);
                // Start the next activity
                startActivity(intent);
            }
        });
    }

    /**
     * This method updates the HashMap adapter
     * and notifies that the data has been changed.
     * This is how the users get displayed on the leaderboard view.
     */
    private void updateList() {
        boardAdapter = new LeaderboardAdapter(leaders);
        leaderBoard.setAdapter(boardAdapter);
        boardAdapter.notifyDataSetChanged();
    }
}