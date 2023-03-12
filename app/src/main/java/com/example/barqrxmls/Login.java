package com.example.barqrxmls;

import static java.lang.System.exit;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

public class Login {

    private FirebaseFirestore database;
    private CollectionReference usersRef;
    private String id;

    private User user;

    public Login(){
        database = FirebaseFirestore.getInstance();
        usersRef = database.collection("Users");
        user = null;
    }

    //https://www.geeksforgeeks.org/how-to-fetch-device-id-in-android-programmatically/amp/
    public void initLogin(ContentResolver contentResolver,Context c) {
        id = Settings.Secure.getString(contentResolver,Settings.Secure.ANDROID_ID);
        String TAG = "Login.getUser";

        Task<QuerySnapshot> matchUser = usersRef.whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        if(matchUser.getResult() == null) {
            System.out.println("task.getResults = null, use this code");
            createNewUser(c);
        }
        for (QueryDocumentSnapshot document : matchUser.getResult()) {
                this.user = document.toObject(User.class);
        }
        createNewUser(c);
    }

    //https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
    private void createNewUser(Context c) {
        Intent intent = new Intent(c, NewAccount.class);
        //NOTE have pull from main and have user implement serializable
        intent.putExtra("User",this.user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

}
