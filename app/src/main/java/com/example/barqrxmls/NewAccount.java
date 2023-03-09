package com.example.barqrxmls;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;

import com.example.barqrxmls.databinding.NewAccountBinding;

public class NewAccount extends MainActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);

        // creating variables for new username and email
        EditText newUserName = findViewById(R.id.usernameField);
        EditText newEmail = findViewById(R.id.emailField);

        // creating submit button functionality
        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implement methods to save username and password to database

                // implement methods to go to MainActivity
                Intent myIntent = new Intent (v.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
    }



}
