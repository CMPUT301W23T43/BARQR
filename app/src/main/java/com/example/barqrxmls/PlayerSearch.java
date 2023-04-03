package com.example.barqrxmls;

import static android.view.KeyEvent.KEYCODE_ENTER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Locale;

/**
 * allows a person using the app to search for other users by username
 */
public class PlayerSearch extends AppCompatActivity {

    //NOTE need to limit num chars user can add to their username

    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");

    private ArrayList<String> userDataList;
    private ArrayAdapter userAdapter;
    private ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_search);

        //setup close button
        Button close = findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //setup search bar
        EditText searchBar = findViewById(R.id.search_bar);
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               //clear previous search
               userDataList = new ArrayList<>();
               userAdapter = new ArrayAdapter<>(PlayerSearch.this, R.layout.user_array_adapter,R.id.userText,userDataList);
               userList = findViewById(R.id.accountNameList);
               userList.setAdapter(userAdapter);

               //get query
               String queryString = searchBar.getText().toString().toLowerCase(Locale.ROOT);
               if (queryString.isEmpty()) {
                   Snackbar.make(findViewById(R.id.mySnackId), "Please enter text to search.", BaseTransientBottomBar.LENGTH_LONG).show();
               }else{
                   //search database for usernames the first 20 usernames that start with queryString
                   String TAG = "PlayerSearch";
                   usersRef
                           .whereGreaterThanOrEqualTo("searchUser", queryString)
                           .orderBy("searchUser", Query.Direction.ASCENDING).limit(20)
                           .get()
                           .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   if (task.isSuccessful()) {
                                       // get the string with the biggest ascii code that satisfies the query
                                       String searchLimit = queryString.concat("zzzzzzzzzzzzzzz");
                                       searchLimit = String.format("%.15s", searchLimit);
                                       // add usernames to the listview
                                       for (QueryDocumentSnapshot document : task.getResult()) {
                                           Log.d(TAG, document.getId() + " => " + document.getData());
                                           String foundUsername = (String) document.get("userName");
                                           assert foundUsername != null;
                                           if (foundUsername.toLowerCase().compareTo(searchLimit) <= 0) {
                                               userDataList.add(foundUsername);
                                               userAdapter.notifyDataSetChanged();
                                           }
                                           // Reached part of list that does not begin with query
                                           else {
                                               break;
                                           }
                                       }

                                       //if user clicks on item in the
                                       userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                           @Override
                                           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                               String username = (String) userAdapter.getItem(i);
                                               switchToPlayer(username);
                                           }
                                       });

                                   } else {
                                       Log.d(TAG, "Error getting documents: ", task.getException());
                                   }
                                   if (userDataList.size() == 0){
                                       Snackbar.make(findViewById(R.id.mySnackId), "No matching users were found, please try again.", BaseTransientBottomBar.LENGTH_LONG).show();
                                   }
                               }
                           });
               }
            }
        });
    }

    /**
     * changes to the PlayerAccount activity
     * @param username the username of the player who's account to display
     */
    private void switchToPlayer(String username) {
        Intent intent = new Intent(PlayerSearch.this, PlayerAccount.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

}