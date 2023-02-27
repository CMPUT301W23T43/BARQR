package com.example.barqrxmls;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    final String TAG = "Sample";

    FirebaseFirestore dataBase;
    CollectionReference usersRef;
    CollectionReference codesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataBase = FirebaseFirestore.getInstance();
        codesRef = dataBase.collection("Codes");
        usersRef = dataBase.collection("Users");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The code below this comment is simply for testing that the database 
        HashMap<String, Object> userInfo = new HashMap<>();
        HashMap<String, Object> codesInfo = new HashMap<>();

        codesInfo.put("Code Name", "First Code");
        codesInfo.put("Hash Value", "abfa85a433a");
        codesInfo.put("Points Value", 3.14159);

        userInfo.put("Name", "Jane Doe");
        userInfo.put("Password", "123");
        userInfo.put("Codes Scanned", Arrays.asList());

        usersRef
                .document("User1")
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data not added"+e);
                    }
                });

        codesRef
                .document("TestCode")
                .set(codesInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data not added"+e);
                    }
                });


        }


}
