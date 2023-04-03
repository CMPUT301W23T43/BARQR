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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * display's the user's account information
 */
public class Account extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = database.collection("Users");
    private CollectionReference codesRef = database.collection("Codes");

    String usernameValue;

    TextView username;
    TextView email;
    TextView highCodeRank;

    Integer highestScore = 0;
    Integer lowestScore = 0;
    Integer rank = 0;
    String highCode;
    String lowCode;

    TextView highCodeName;
    TextView lowCodeName;
    DocumentReference userInDatabase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

        CurrentUser user = CurrentUser.getInstance();

        // setup close button
        Button close = findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // get code rank views
        highCodeName = findViewById(R.id.highestQRCode);
        lowCodeName = findViewById(R.id.lowestQRCode);
        highCodeRank = findViewById(R.id.highCodeHash);

        highCodeName.setVisibility(View.INVISIBLE);
        lowCodeName.setVisibility(View.INVISIBLE);

        // setup username and email fields in the layout
        username = findViewById(R.id.usernameMyAccount);
        email = findViewById(R.id.emailMyAccount);
        usernameValue = user.getUserName();
        username.setText(usernameValue);
        email.setText(user.getEmail());

        // get a document reference of the user from the database
        userInDatabase = usersRef.document(user.getUserName().toLowerCase(Locale.ROOT));

        // setup a query of the database codes by rank
        Query codesByScores = codesRef.orderBy("points", Query.Direction.DESCENDING);
        getCodeRank(codesByScores);

        highCodeName.setVisibility(View.VISIBLE);
        lowCodeName.setVisibility(View.VISIBLE);

    }

    /**
     * gets the highest and lowest scoring codes of the user
     * @param docRef a reference of the user
     * @param codesRank a list of codes in order of point value
     */

    private void getPolarCodes(DocumentReference docRef, LinkedHashMap<String, Integer> codesRank) {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<Object, Object> userCodes = new HashMap<>();
                userCodes.putAll((Map<Object, Object>) documentSnapshot.get("codes"));

                for (Map.Entry<Object, Object> code : userCodes.entrySet()) {
                    DocumentReference checkCode = codesRef.document(code.getKey().toString());
                    checkCode.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            System.out.println(Long.valueOf((Long) documentSnapshot.get("points")).intValue());
                            if (Long.valueOf((Long) documentSnapshot.get("points")).intValue() > highestScore || highestScore == 0) {
                                highCode = (String) documentSnapshot.get("name");
                                highestScore = (Long.valueOf((Long) documentSnapshot.get("points")).intValue());
                                highCodeName.setText(highCode);
                                highCodeRank.setText("number "+codesRank.get((String) documentSnapshot.get("hash"))+" of "+codesRank.size()+"!");

                                }
                            if (Long.valueOf((Long) documentSnapshot.get("points")).intValue() < lowestScore || lowestScore == 0) {
                                lowCode = (String) documentSnapshot.get("name");
                                lowestScore = Long.valueOf((Long) documentSnapshot.get("points")).intValue();
                                lowCodeName.setText(lowCode);
                            }

                        }
                    });
                }
            }

        });
    }

    /**
     * gets a ranking of all database codes
     * @param query a database query listing the codes by point value
     */

    private void getCodeRank(Query query){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> codes = new ArrayList<>();
                LinkedHashMap<String, Integer> codesRank = new LinkedHashMap<>();

                if (task.isSuccessful()) {
                    int i = 0;
                    Double topScore = Double.POSITIVE_INFINITY;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //System.out.println(highCodeHash);
                        //System.out.println((String) document.get("hash"));
                        if (Long.valueOf((Long) document.get("points")).doubleValue() < topScore){
                            topScore = Long.valueOf((Long) document.get("points")).doubleValue();
                            i++;
                        }
                        codesRank.put((String) document.get("hash"), i);
                        }
                    getPolarCodes(userInDatabase, codesRank);
                    }

                }
        });
    }
}

