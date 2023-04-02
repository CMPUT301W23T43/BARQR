package com.example.barqrxmls;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class DataBaseUpdater {

    private static DataBaseUpdater instance = null;
    private DataBaseUpdater() {

    }

    public static DataBaseUpdater getInstance() {
        if (instance == null) {
            instance = new DataBaseUpdater();
        }
        return instance;
    }

    public void updateCode(Code code) {

    }

    public void updateUser(User user) {
        // get database
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference usersRef;
        usersRef = dataBase.collection("Users");

        // overwrite old user with new information based on this user
        String TAG = "DataBaseUpdater,user";
        usersRef.document(user.getUserName().toLowerCase(Locale.ROOT))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}