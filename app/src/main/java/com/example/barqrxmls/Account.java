/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the account_screen.xml file
 */

package com.example.barqrxmls;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = database.collection("Users");
    private CollectionReference codesRef = database.collection("Codes");

    String usernameValue;

    TextView username;
    TextView email;

    Integer highestScore = 0;
    Integer lowestScore = 0;
    String highCode;
    String lowCode;

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

        DocumentReference userInDatabase = usersRef.document(thisUser.getUserName());

        userInDatabase.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<Object, Object> userCodes = new HashMap<>();
                userCodes.putAll((Map<Object, Object>) documentSnapshot.get("codes"));

                for (Map.Entry<Object, Object> code : userCodes.entrySet()) {
                    DocumentReference checkCode = codesRef.document(code.getKey().toString());
                    checkCode.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (Long.valueOf((Long) documentSnapshot.get("points")).intValue() >= highestScore || highestScore == 0) {
                                highCode = (String) documentSnapshot.get("name");
                                highestScore = (Long.valueOf((Long) documentSnapshot.get("points")).intValue());
                            } else if (Long.valueOf((Long) documentSnapshot.get("points")).intValue() <= lowestScore || lowestScore == 0) {
                                lowCode = (String) documentSnapshot.get("name");
                                lowestScore = Long.valueOf((Long) documentSnapshot.get("points")).intValue();
                            }
                        }
                    });
                }

            }
        });

    }

}
