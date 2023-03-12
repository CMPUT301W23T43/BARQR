/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the leaderboard_screen.xml file
 */

package com.example.barqrxmls;

import android.os.Bundle;
import android.util.Log;
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

        usersRef = FirebaseFirestore.getInstance().collection("Users");
        leaderBoard = findViewById(R.id.leaderBoardList);

        Query getUsersByScores = usersRef.orderBy("totalPoints", Query.Direction.DESCENDING);

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
