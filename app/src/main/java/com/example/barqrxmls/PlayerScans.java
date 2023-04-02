package com.example.barqrxmls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlayerScans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_scans);

        // get code
        Bundle bundle = getIntent().getExtras();
        Code code = (Code)bundle.get("code");

        // setup array of users
        ArrayList<String> userDataList = new ArrayList<>();
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(PlayerScans.this, R.layout.code_array_adapter,R.id.codeText,userDataList);
        ListView userList = findViewById(R.id.accountNameList);
        userList.setAdapter(userAdapter);

        // get code from database with list of users that have scanned the code
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference codesRef;
        codesRef = dataBase.collection("Codes");
        String TAG = "PlayerScans";
        System.out.println(code.getHash());
        codesRef.document(code.getHash()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            Log.d(TAG,"code exists");
                            // if code exists add all users that have scanned the code to the list
                            Code foundCode = document.toObject(Code.class);
                            ArrayList<String> users = foundCode.getUsers();
                            userDataList.addAll(users);
                            userAdapter.notifyDataSetChanged();

                        }
                        // if code does not exist, exit activity
                        else {
                            Log.d(TAG,"code does not exist");
                            finish();
                        }
                    }
                });

        // setup close button
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}