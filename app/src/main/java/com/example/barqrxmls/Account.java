/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the account_screen.xml file
 */

package com.example.barqrxmls;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.checkerframework.checker.units.qual.Current;

public class Account extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = database.collection("Users");

    String usernameValue;

    TextView username;
    TextView email;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

        CurrentUser currentUser = CurrentUser.getInstance();

        User thisUser = currentUser.getUser();

        username = findViewById(R.id.usernameMyAccount);

        email = findViewById(R.id.emailMyAccount);

        Button closeButton = (Button) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        usernameValue = thisUser.getUserName();
        username.setText(usernameValue);
        email.setText(thisUser.getEmail());


    }

}
