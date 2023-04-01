package com.example.barqrxmls;

import android.util.Log;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * represents a User and stores all user related information
 * Unit test for User class exists but does not work, could not figure out how to do a unit test
 * on a class that communicates with the database
 */

public class User implements Serializable {
    private String userName;
    private HashMap<String,String> codes;
    private int totalPoints;
    private String email;
    private String id;
    private int numCodes;


    /**
     * stores information related to the user's account
     * @param userName is a String of the player's Unique Username
     * @param id is a String of the player's id
     * @param email is a String of the user's email
     *

     */
    User(String userName, String id, String email) {
        this.userName = userName;
        this.id = id;
        this.totalPoints = 0;
        this.email = email;
        this.numCodes = 0;
        this.codes = new HashMap<>();
        updateInDatabase();
    }

    /**
     *Empty user constructor for pulling a user from the database
     */
    User() {

    }

    /**
     * return the user's username
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * return's user's id
     * @return the user's id
     */
    public String getId() {
        return id;
    }

    /**
     * return's a hashmap of the user's code hashes and code comments
     * @return a hashmap of the user's code hashes and code comments
     */
    public HashMap<String, String> getCodes() {
        return codes;
    }

    /**
     * returns user's total points
     * @return user's total points
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * returns user's email
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * returns the number of codes the user has
     * @return the number of codes the user has
     */
    public int getNumCodes() {
        return numCodes;
    }



    /**
     * adds a code to the user's list of codes without a comment
     * @param codeHash is a String of the hash of the code
     * @param codePoints is an int of the number of points the code is worth
     */
    public void addCode(String codeHash, int codePoints) {
        // add code without comment to user codes, update points and total codes
        if(codes.containsKey(codeHash)) {
            return;
        }
        codes.put(codeHash,"");
        totalPoints = totalPoints + codePoints;
        numCodes = numCodes + 1;
        updateInDatabase();
    }

    /**
     * adds a code to the user's list of codes with a comment
     * @param codeHash is a String of the hash of the code
     * @param comment is a String of the user's comment on their code
     * @param codePoints is an int of the number of points the code is worth
     */
    public void addCode(String codeHash, String comment, int codePoints) {
        // add code with comment to user codes, update points and total codes
        if(codes.containsKey(codeHash)) {
            return;
        }
        codes.put(codeHash,comment);
        totalPoints = totalPoints + codePoints;
        numCodes = numCodes + 1;
        updateInDatabase();
    }

    /**
     * adds a comment to a code, overwrites any existing comment
     * @param codeHash is a String of the hash of the code to add
     * @param codeComment is the comment to add to the code
     */
    public void addComment(String codeHash, String codeComment) {
        if(!codes.containsKey(codeHash)) {
            return;
        }
        codes.put(codeHash, codeComment);
        updateInDatabase();
    }

    /**
     * removes a comment from a code
     * @param codeHash is a String of the hash of the code to remove comment from
     */
    public void removeComment(String codeHash) {
        if(!codes.containsKey(codeHash) || codes.get(codeHash) == null) {
            return;
        }
        codes.put(codeHash,"");
        updateInDatabase();
    }

    /**
     * checks if a code has a comment
     * @param codeHash the hash of the code to check
     * @return true if the code has a comment, false otherwise
     */
    public boolean hasComment(String codeHash) {
        if (Objects.equals(codes.get(codeHash), "") || codes.get(codeHash) == null) {
            return false;
        }
        else {
            return true;
        }

    }

    /**
     * removes a code from the user's list of codes
     * @param removeCode is the hash of the code to remove from the user's code list
     * @param codePoints is the number of points the code to remove is worth
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
     * updates the database when a change is made to the user
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
