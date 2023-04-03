
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
 *  This class displays the information of the current user, including:
 *  username, email, highest scoring code, lowest scoring code, and the rank of
 *  the highest scoring code.
 *
 * @author Anjelica Marianicz
 * @author Danielle Hopfe
 *
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

        Button close = findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        username = findViewById(R.id.usernameMyAccount);

        email = findViewById(R.id.emailMyAccount);

        highCodeName = findViewById(R.id.highestQRCode);
        lowCodeName = findViewById(R.id.lowestQRCode);
        highCodeRank = findViewById(R.id.highCodeHash);

        highCodeName.setVisibility(View.INVISIBLE);
        lowCodeName.setVisibility(View.INVISIBLE);

        Button closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        usernameValue = user.getUserName();
        username.setText(usernameValue);
        email.setText(user.getEmail());

        userInDatabase = usersRef.document(user.getUserName().toLowerCase(Locale.ROOT));

        Query codesByScores = codesRef.orderBy("points", Query.Direction.DESCENDING);
        getCodeRank(codesByScores);

        highCodeName.setVisibility(View.VISIBLE);
        lowCodeName.setVisibility(View.VISIBLE);

    }

    /**
     * This method queries the database to determine the highest and lowest
     * scoring codes for the current user. The method sets the text for both
     * TextView objects for display.
     *
     * @param docRef
     * @param codesRank
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
     * This method retrieves the ranking of the highest code amongst
     * all of the codes within the database.
     * @param query this query retrieves all codes in order from highest to lowest points value.
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
                        // Grabs all codes from the database, looks for the matching code and sets integer value to the rank display.
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

