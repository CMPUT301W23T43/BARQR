/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the
 */

package com.example.barqrxmls;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewCode extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_screen);
        Bundle bundle = getIntent().getExtras();
        Code code = (Code)bundle.get("code");

        TextView codeName = findViewById(R.id.code_name);
        codeName.setText(String.format("Name: %s",code.getName()));
        
    }
}
