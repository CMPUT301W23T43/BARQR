package com.example.barqrxmls;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

    public void addCode(Code code, User user) {
        code.addUser(user.getUserName());
        updateCode(code);
        updateUser(user);
    }

    public void updateCode(Code code) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference codesRef;
        codesRef = dataBase.collection("Codes");
        String TAG = "DataBaseUpdater,code";
        System.out.println(code.getHash());
        codesRef.document(code.getHash()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG,"updating code database");
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            Code oldCode = document.toObject(Code.class);
                            if(oldCode != null) {
                                oldCode.combineUsers(code.getUsers());
                                codesRef.document(code.getHash()).set(oldCode);
                            }
                        }
                        else {
                            codesRef.document(code.getHash()).set(code);
                        }
                    }
                });
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
