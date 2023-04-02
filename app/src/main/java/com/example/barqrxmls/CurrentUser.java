package com.example.barqrxmls;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/***
 * Simple singleton container class for the CurrentUser, so that any class which wants to act upon
 * or reference the current utser can just get this instance and work with it.
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

    /***
     * Empty constructor used privately
     */
    private CurrentUser(String userName, String id, String email) {
        super(userName,id,email);
        updater = DataBaseUpdater.getInstance();
        updater.updateUser((User)this);
    }

    private CurrentUser() {
        super();
    }

    /***
     * Sets the CurrentUser's user field.
     * @param user The user to become the CurrentUser
     */
    public void setUser(User user) {
        instance = new CurrentUser(user.getUserName(),user.getId(),user.getEmail());
    }


    public void addCode(String codeHash, int codePoints) {
        super.addCode(codeHash,codePoints);
        updater.updateUser((User)this);
    }

    public void addCode(String codeHash, String geolocation, int codePoints) {
        super.addCode(codeHash,geolocation,codePoints);
        updater.updateUser((User)this);
    }

    public void removeCode(String removeCode, int codePoints) {
        super.removeCode(removeCode,codePoints);
        updater.updateUser((User)this);
    }

    public void addComment(String codeHash, String codeComment) {
        super.addComment(codeHash,codeComment);
        updater.updateUser((User)this);

    }

    public void removeComment(String codeHash) {
        super.removeComment(codeHash);
        updater.updateUser((User)this);
    }

    public void addImage(String codeHash, byte[] image) {
        super.addImage(codeHash,image);
        updater.updateUser((User)this);

    }

}
