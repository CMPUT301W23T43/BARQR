package com.example.barqrxmls;

import android.graphics.Bitmap;
import android.util.Log;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
    protected String searchUser;


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
        searchUser = userName.toLowerCase();
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

    public String getSearchUser() {
        return searchUser;
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

    /**
     * gets the geolocation for a given code
     * @param codeHash the hash of the code to get the geolocation for
     * @return the geolocation of the code
     */
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
     * allows subclasses to set their list of codes
     * @param codes the list of codes to set User.codes to
     */
    protected void setCodes(HashMap<String,HashMap<String,String>> codes) {
        this.codes = codes;
    }

    /**
     * allows subclasses to set their total points
     * @param points the integer of points to set totalPoints to
     */
    protected void setTotalPoints(int points) {
        this.totalPoints = points;
    }

    /**
     * allows subclasses to set their number of codes
     * @param numCodes integer of the number of codes to set numCodes to
     */
    protected void setNumCodes(int numCodes) {
        this.numCodes = numCodes;
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

    /**
     * gets the comment of a given code
     * @param codeHash the hash of the code to get the comment for
     * @return the comment of the code
     */
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
    // website: https://www.toptip.ca/2019/04/java-convert-byte-array-to-string-then.html
    // author: Zen

    /**
     * adds an image to the a the given code of a User
     * @param codeHash the hash of the code to add the image to
     * @param image the image to add to the code
     */
    public void addImage(String codeHash, byte[] image) {
        if(!codes.containsKey(codeHash)) {
            return;
        }

        String convert = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            convert = Base64.getEncoder().encodeToString(image);
        }
        codes.get(codeHash).put("image",convert);
        //updateInDatabase();
    }

    /**
     * gets an image for a given code
     * @param codeHash the hash of the code to get the image for
     * @return an image represented as a byte array
     */
    public byte[] getImage(String codeHash) {
        if(!codes.containsKey(codeHash)) {
            return null;
        }
        String convert = codes.get(codeHash).get("image");

        if(convert == null) {
            return null;
        }

        byte[] image = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            image = Base64.getDecoder().decode(convert);
        }
        return image;

    }

    /**
     * compares two users by username
     * @param o
     */
    public int compareTo(Object o) {
        User user = (User) o;
        return this.userName.compareTo(user.getUserName());
    }

}