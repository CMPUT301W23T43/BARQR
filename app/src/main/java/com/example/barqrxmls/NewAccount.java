package com.example.barqrxmls;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barqrxmls.databinding.NewAccountBinding;

public class NewAccount extends AppCompatActivity {
//NOTE need to add instructions for setting this up on main
//https://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle.get("User") == null) {
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
                    //NOTE: need to pull main and set user to update the database upon being created
                    User user = new User(newUserName.toString(), newEmail.toString());
                    // implement methods to go to MainActivity
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.putExtra("User",user);
                    startActivityForResult(myIntent,1);
                }
            });
        }
        else {
            Intent myIntent = new Intent(NewAccount.this, MainActivity.class);
            myIntent.putExtra("User",user);
            startActivityForResult(myIntent,1);
        }
    }



}
