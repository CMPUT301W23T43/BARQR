package com.example.barqrxmls;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class PlayerSearch extends AppCompatActivity {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");

    Button closeButton;
    EditText searchBar;
    Button searchGo;
    String searchValue;

    Snackbar mySnackBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_account_search);

        closeButton = findViewById(R.id.closeButton);

        // Returns from the search back to the main activity page
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchBar = findViewById(R.id.playerSearchBar);
        searchGo = findViewById(R.id.searchUserButton);

        searchGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchValue = searchBar.getText().toString();
                if (searchValue.isEmpty()) {
                    Snackbar.make(findViewById(R.id.mySnackId), "Please Enter Text", BaseTransientBottomBar.LENGTH_LONG).show();
                } else {
                    searchDatabase(searchValue);
                }
            }
        });




    }

    private void searchDatabase(String value) {
        Query searchQuery = usersRef.whereEqualTo("username", value);

        DocumentReference foundUser = usersRef.document(value).;

        if (foundUser == null){
            Snackbar.make(findViewById(R.id.mySnackId), "Please Enter Text", BaseTransientBottomBar.LENGTH_LONG).show();
        }else{
            System.out.println(usersRef.document(value).);
        }

    }
}
