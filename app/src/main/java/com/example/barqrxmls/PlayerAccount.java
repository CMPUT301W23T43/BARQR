package com.example.barqrxmls;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlayerAccount extends AppCompatActivity {

    private ArrayList<Code> CodeDataList;
    private CodeArrayAdapter CodeAdapter;

    FirebaseFirestore dataBase;
    CollectionReference usersRef;
    CollectionReference codesRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    ListView CodesList;



    public void onResume(Bundle savedInstanceState) {
        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        codesRef = dataBase.collection("Codes");
        usersRef = dataBase.collection("Users");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_account);

        CodesList = findViewById(R.id.playerCodesDisplay);
        if (currentUser != null) {
            // Get the current user's ID
            String currentUserId = currentUser.getUid();
            if (currentUser != null) {
                // Get the current user's ID
                String currentUserEmail = currentUser.getEmail();
                User user = new User(currentUser.toString(), currentUserId.toString(), currentUserEmail.toString());






                CodeDataList = new ArrayList<Code>();
                for (String key : user.getCodes().keySet()) {
                    CodeDataList.add(new Code(key));
                }
                CodeAdapter = new CodeArrayAdapter(this, CodeDataList);
                CodesList.setAdapter(CodeAdapter);





                /**
                 Sets up an OnItemLongClickListener for the CodesList AdapterView in the PlayerAccount activity.
                 When a long click is detected on an item in the CodesList, an AlertDialog is displayed to confirm
                 if the user wants to delete the selected code. If the user confirms deletion, the corresponding code
                 is removed from the current user's codes collection in Firestore.
                 @param parent The AdapterView containing the list of codes.
                 @param view The View representing the selected code in the list.
                 @param position The position of the selected code in the list.
                 @param id The ID of the selected code.
                 @return true if the long click event is consumed, false otherwise.
                 */
                CodesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        AlertDialog alertDialog = new AlertDialog.Builder(PlayerAccount.this).
                                setTitle("Delete Code").
                                setMessage("Are you sure you want to delete this Code?").
                                setPositiveButton("Yes", (dialog, which) -> {
                                    String CodeToDelete = (String) parent.getItemAtPosition(position);
                                    Code CodeDeleted = new Code(CodeToDelete);
                                    user.removeCode(CodeToDelete,CodeDeleted.getPoints());
                                }).
                                setNegativeButton("No", (dialog, which) -> {
                                }).
                                create();
                        alertDialog.show();
                        return true;
                    }
                });


            }


        }
    }
}