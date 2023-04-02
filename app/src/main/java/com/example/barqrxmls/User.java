package com.example.barqrxmls;

import android.graphics.Bitmap;
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
    protected String userName;
    protected HashMap<String,HashMap<String,String>> codes;
    protected int totalPoints;
    protected String email;
    protected String id;
    protected int numCodes;

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
        //updateInDatabase();
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
    public HashMap<String, HashMap<String,String>> getCodes() {
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

    public String getGeoLocation(String codeHash) {
        if(!codes.containsKey(codeHash)) {
            return null;
        }
        else {
            return codes.get(codeHash).get("geolocation");
        }

    }

    /**
     * adds a code to the user's list of codes without a geolocation
     * @param codeHash is a String of the hash of the code
     * @param codePoints is an int of the number of points the code is worth
     */
    public void addCode(String codeHash, int codePoints) {
        // re-init codes if null
        if(codes == null) {
            this.codes = new HashMap<>();
        }
        // return if user already has the code
        if(codes.containsKey(codeHash)) {
            return;
        }
        // add code without comment to user codes, update points and total codes
        HashMap<String,String> codeInfo = new HashMap<>();
        codeInfo.put("geolocation","");
        codeInfo.put("image","");
        codeInfo.put("comment","");
        codes.put(codeHash,codeInfo);
        totalPoints = totalPoints + codePoints;
        numCodes = numCodes + 1;
        //updateInDatabase();
    }

    /**
     * adds a code to the user's list of codes with a geolocation
     * @param codeHash is a String of the hash of the code
     * @param geolocation is a String of the geolocation where the code was found
     * @param codePoints is an int of the number of points the code is worth
     */
    public void addCode(String codeHash, String geolocation, int codePoints) {
        // re-init codes if null
        if(codes == null) {
            this.codes = new HashMap<>();
        }
        // return if user already has the code
        if(codes.containsKey(codeHash)) {
            return;
        }
        // add code with comment to user codes, update points and total codes
        HashMap<String,String> codeInfo = new HashMap<>();
        codeInfo.put("geolocation",geolocation);
        codeInfo.put("image","");
        codeInfo.put("comment","");
        codes.put(codeHash,codeInfo);
        totalPoints = totalPoints + codePoints;
        numCodes = numCodes + 1;
        //updateInDatabase();
    }

    /**
     * removes a code from the user's list of codes
     * @param removeCode is the hash of the code to remove from the user's code list
     * @param codePoints is the number of points the code to remove is worth
     */
    public void removeCode(String removeCode, int codePoints) {
        if(codes == null || !codes.containsKey(removeCode)) {
            return;
        }
        // remove code from user codes, update points and total codes
        codes.remove(removeCode);
        totalPoints = totalPoints - codePoints;
        numCodes = numCodes - 1;
        //updateInDatabase();
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
        codes.get(codeHash).put("comment",codeComment);
        //updateInDatabase();
    }

    /**
     * removes a comment from a code
     * @param codeHash is a String of the hash of the code to remove comment from
     */
    public void removeComment(String codeHash) {
        if(!codes.containsKey(codeHash) || codes.get(codeHash) == null) {
            return;
        }
        codes.get(codeHash).put("comment","");
        //updateInDatabase();
    }

    /**
     * checks if a code has a comment
     * @param codeHash the hash of the code to check
     * @return true if the code has a comment, false otherwise
     */
    public boolean hasComment(String codeHash) {
        return codes.get(codeHash) != null && !Objects.equals(codes.get(codeHash).get("comment"), "");

    }

    public String getComment(String codeHash) {
        if(!codes.containsKey(codeHash) || codes.get(codeHash) == null) {
            return null;
        }
        return codes.get(codeHash).get("comment");
    }

    /**
     * adds an image represented as a byte array to a code
     * @param codeHash is the hash of the code to add the image to
     * @param image is the byte array representing the image
     */

    // how to convert a String to a byte Array and vice versa:
    // website: Baeldung https://www.baeldung.com/java-string-to-byte-array
    // author: Chandra Prakash https://www.baeldung.com/author/chandra-prakash

    public void addImage(String codeHash, byte[] image) {
        if(!codes.containsKey(codeHash)) {
            return;
        }
        String convert = new String(image);
        codes.get(codeHash).put("image",convert);
        //updateInDatabase();
    }

    public byte[] getImage(String codeHash) {
        if(!codes.containsKey(codeHash)) {
            return null;
        }
        String convert = codes.get(codeHash).get("image");

        if(convert == null) {
            return null;
        }
        return convert.getBytes();

    }

    /**
     * updates the database when a change is made to the user
     */
//    private void updateInDatabase() {
//        if(this.getClass().getSuperclass() == User.class) {
//            return;
//        }
//        // get database
//        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
//        CollectionReference usersRef;
//        usersRef = dataBase.collection("Users");
//
//        // overwrite old user with new information based on this user
//        String TAG = "User.updateInDatabase";
//        User update = this;
//        usersRef.document(userName)
//                .set(update)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//    }

    /**
     *
     * @param
     */
    public int compareTo(Object o) {
        User user = (User) o;
        return this.userName.compareTo(user.getUserName());
    }

}
