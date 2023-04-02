package com.example.barqrxmls;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddCommentFragment extends DialogFragment {
    private Code current_code;
    User currentTestUser;
    interface AddCommentDialogueListener{
        void addComment(Code code,String comment);

    }

    public AddCommentFragment() {
        super();
    }

    public AddCommentFragment(Code code) {
        current_code = code;
    }

    private AddCommentDialogueListener listener;
    private EditText addComment;
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");
    CollectionReference codesRef = dataBase.collection("Codes");

    @Override
    /**
     Called when the fragment is attached to its context. This method sets the listener for the fragment
     to the activity hosting it, and throws a ClassCastException if the activity does not implement the
     AddCommentDialogueListener interface.
     @param context The context to attach the fragment to
     */
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddCommentDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddCommentDialogueListener");
        }
    }



    
    @NonNull
    @Override
    /**
     Creates and returns a new instance of a dialog to add a comment.
     @param savedInstanceState The saved state of the fragment, or null if there is no saved state
     @return A new instance of a dialog to add a comment.
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_comment_fragment, null);
        addComment = view.findViewById(R.id.AddComment);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add a Comment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String comment = addComment.getText().toString().trim();
                    listener.addComment(current_code, comment);
                })
                .create();
    }
}

