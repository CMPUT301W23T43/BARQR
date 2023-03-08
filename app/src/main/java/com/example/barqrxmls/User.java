package com.example.barqrxmls;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    private String userName;
    private HashMap<String,String> codes;
    private int totalPoints;
    private String email;
    private String id;
    private int numCodes;

    /**
     *
     * @param
     */
    User(String userName, String id, String email) {
        this.userName = userName;
        this.id = id;
        this.totalPoints = 0;
        this.email = email;
        this.numCodes = 0;
    }

    /**
     *
     * @param
     */
    User() {

    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param
     */
    public HashMap<String, String> getCodes() {
        return codes;
    }

    /**
     *
     * @param
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     *
     * @param
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param
     */
    public int getNumCodes() {
        return numCodes;
    }

    /**
     *
     * @param
     */
    public void addCode(String codeHash, int codePoints) {
        // add code without comment to user codes, update points and total codes
        codes.put(codeHash,"");
        totalPoints = totalPoints + codePoints;
        numCodes = numCodes + 1;
        updateInDatabase();
    }

    /**
     *
     * @param
     */
    public void addCode(String codeHash, String comment, int codePoints) {
        // add code with comment to user codes, update points and total codes
        codes.put(codeHash,comment);
        totalPoints = totalPoints + codePoints;
        numCodes = numCodes + 1;
        updateInDatabase();
    }

    /**
     *
     * @param
     */
    public void removeCode(String removeCode, int codePoints) {
        // remove code from user codes, update points and total codes
        codes.remove(removeCode);
        totalPoints = totalPoints - codePoints;
        numCodes = numCodes - 1;
        updateInDatabase();
        return;
    }

    /**
     *
     * @param
     */
    private void updateInDatabase() {
        // get database
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference usersRef;
        usersRef = dataBase.collection("Users");

        // overwrite old user with new information based on this user
        String TAG = "User.updateInDatabase";
        User update = this;
        usersRef.document(userName)
                .set(update)
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

    /**
     *
     * @param
     */
    public int compareTo(Object o) {
        User user = (User) o;
        return this.userName.compareTo(user.getUserName());
    }

}
