package com.example.barqrxmls;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barqrxmls.databinding.NewAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;


//Acknowledgements:

/**
 * gets the user from the user's device id or creates a new user if the user does not yet have an
 * account
 */

public class NewAccount extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = database.collection("Users");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get device id
        //https://www.geeksforgeeks.org/how-to-fetch-device-id-in-android-programmatically/amp/
        String id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID).replace("/","_");


        String TAG = "NewAccount getID";
        //query database for user with matching id
        usersRef.whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "successfully retrieved user");
                            User user = null;
                            //set user to result of query
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                user = document.toObject(User.class);
                            }

                            //if user exists pass user to main and end activity
                            if(user != null) {
                                //https://www.youtube.com/watch?v=DfDj9EadOLk , https://www.youtube.com/@DailyCoding
                                Intent myIntent = new Intent(NewAccount.this, MainActivity.class);
//                                setResult(RESULT_OK,myIntent);
                                CurrentUser currentUser = CurrentUser.getInstance();
                                currentUser.setUser(user);
                                startActivity(myIntent);
                            }

                            //if user does not exist start new account screen
                            setContentView(R.layout.new_account);

                            //make invalid username toast
                            //https://developer.android.com/guide/topics/ui/notifiers/toasts#java
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast invalidUsername = Toast.makeText(context, "Username already exists",duration);

                            // creating variables for new username and email
                            EditText newUserName = findViewById(R.id.usernameField);
                            EditText newEmail = findViewById(R.id.emailField);

                            // creating submit button functionality
                            Button submit = (Button) findViewById(R.id.submitButton);
                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //get chosen username
                                    String username = newUserName.getText().toString().replace(' ','-').replace('/','-');

                                    String TAG = "NewAccount username";

                                    //check if user with chosen username already exists
                                    usersRef.document(username)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "successfully retrieved data");
                                                        DocumentSnapshot document = task.getResult();
                                                        //if username already in use popup invalid username text
                                                        if(document.exists()) {
                                                            invalidUsername.show();
                                                        }
                                                        //if username not in use create new user
                                                        else {
                                                            //https://www.youtube.com/watch?v=DfDj9EadOLk , https://www.youtube.com/@DailyCoding
                                                            User user = new User(username, id, newEmail.getText().toString());
                                                            Intent myIntent = new Intent(NewAccount.this, MainActivity.class);
                                                            CurrentUser currentUser = CurrentUser.getInstance();
                                                            currentUser.setUser(user);
                                                            startActivity(myIntent);
                                                        }

                                                    }
                                                    else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}
