/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the account_screen.xml file
 */

package com.example.barqrxmls;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);
        User user = CurrentUser.getInstance().getUser();

        TextView username = findViewById(R.id.usernameField);
        TextView email = findViewById(R.id.emailField);

        username.setText(user.getUserName());
        email.setText(user.getEmail());

        Button close = findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
