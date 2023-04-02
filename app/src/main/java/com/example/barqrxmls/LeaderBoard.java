/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the leaderboard_screen.xml file
 */

package com.example.barqrxmls;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

/**
 * This class displays the leaderboard along with the current player's (the app user)
 * ranking amongst all the other players in the database.
 */
public class LeaderBoard extends AppCompatActivity {
    final String TAG = "Sample";

    CollectionReference usersRef;

    private ListView leaderBoard;
    private LeaderboardAdapter boardAdapter;
    HashMap<Object, Object> leaders;
    HashMap<Object, Object> rankings;

    TextView userRank;
    Button ShowAllCodes;

    /**
     * Upon creation the users are grabbed from the database and sorted by their player scores.
     * The onCreate method below handles this
     * @param savedInstanceState
     *
     * One outside source was referenced when writing this code:
     *
     * URL: https://stackoverflow.com/questions/57041298/android-studio-firestore-get-name-of-user-with-highest-points
     * Website: https://stackoverflow.com
     * Author: https://stackoverflow.com/user/52446885/alex-mamo
     *
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_screen);

//        Taskbar taskbar = new Taskbar(LeaderBoard.this);
        Taskbar taskbar = Taskbar.getInstance(LeaderBoard.this);
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
        ShowAllCodes = findViewById(R.id.playerSearchButton);

        usersRef = FirebaseFirestore.getInstance().collection("Users");
        leaderBoard = findViewById(R.id.leaderBoardList);

        Query getUsersByScores = usersRef.orderBy("points", Query.Direction.DESCENDING);

        List<User> queryUsers = new ArrayList<>();

        userRank = findViewById(R.id.playerRanking);
        leaders = new HashMap<>();
        rankings = new HashMap<>();
        getUsersByScores.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, String.valueOf(document.get("userName")));
                        leaders.put(document.get("userName"), document.get("totalPoints"));
                        boardAdapter = new LeaderboardAdapter(leaders);
                        leaderBoard.setAdapter(boardAdapter);
                        updateList();
                        rankings.put(document.get("userName"), i);
                        i++;

                    }
                    userRank.setText(rankings.get("CurrentUser").toString());
                }
            }
        });
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
     * This updates the HashMap adapter and notifies that the data has been changed.
     *
     */
    private void updateList() {
        boardAdapter.notifyDataSetChanged();
        System.out.println(leaders);
    }
}
