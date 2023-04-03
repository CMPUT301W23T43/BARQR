package com.example.barqrxmls;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/***
 * Simple singleton representing the current logged in User, so that any class which wants to act upon
 * or reference the current user can just get this instance and work with it.
 * updates the database with when the current user changes information about their account, codes, etc.
 */
public class CurrentUser extends User{
    private static CurrentUser instance = null;
    private DataBaseUpdater updater;
    //private User user;

    /***
     * Get the instance of the CurrentUser class
     * @return instance
     */
    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    /**
     * creates a new instance of CurrentUser based on the user passed to it
     * @param user is the user to base the new instance on
     */
    private CurrentUser(User user) {
        // setup account info
        super(user.getUserName(),user.getId(),user.getEmail());
        setCodes(user.getCodes());
        setNumCodes(user.getNumCodes());
        setTotalPoints(user.getTotalPoints());
        // get the database updater and update the user
        updater = DataBaseUpdater.getInstance();
        updater.updateUser((User)this);
    }

    /***
     * Empty constructor used privately
     */
    private CurrentUser() {
        super();
        updater = DataBaseUpdater.getInstance();
    }

    /***
     * Sets the CurrentUser's user field.
     * @param user The user to become the CurrentUser
     */
    public void setUser(User user) {
        instance = new CurrentUser(user);
    }


    /**
     * adds a code without a geolocation to CurrentUser's list of codes
     * updates the database
     * @param code the code to add
     */
    public void addCode(Code code) {
        super.addCode(code.getHash(),code.getPoints());
        updater.addCode(code,(User)this);
    }

    /**
     * adds a code with a geolocation to CurrentUser's list of codes
     * updates the database
     * @param code the code to add
     * @param geolocation the code's geolocation
     */
    public void addCode(Code code,String geolocation) {
        super.addCode(code.getHash(),geolocation,code.getPoints());
        updater.addCode(code,(User)this);
    }

    /**
     * removes a code from CurrentUser's code list
     * updates the database
     * @param removeCode is the hash of the code to remove
     * @param codePoints is the number of points the code to remove is worth
     */
    public void removeCode(String removeCode, int codePoints) {
        super.removeCode(removeCode,codePoints);
        updater.updateUser((User)this);
    }

    /**
     * adds a comment to one of CurrentUser's codes
     * updates the database
     * @param codeHash is a String of the hash of the code to add
     * @param codeComment is the comment to add to the code
     */
    public void addComment(String codeHash, String codeComment) {
        super.addComment(codeHash,codeComment);
        updater.updateUser((User)this);

    }

    /**
     * removes a comment from one of CurrentUser's codes
     * updates the database
     * @param codeHash is a String of the hash of the code to remove comment from
     */
    public void removeComment(String codeHash) {
        super.removeComment(codeHash);
        updater.updateUser((User)this);
    }

    /**
     * adds an Image to one of CurrentUser's codes
     * updates the database
     * @param codeHash is the hash of the code to add the image to
     * @param image is the byte array representing the image
     */
    public void addImage(String codeHash, byte[] image) {
        super.addImage(codeHash,image);
        updater.updateUser((User)this);

    }

}
