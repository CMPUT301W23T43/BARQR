/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the
 */

package com.example.barqrxmls;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Locale;

public class NewCode extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_screen);

        // get code and user
        Bundle bundle = getIntent().getExtras();
        Code code = (Code)bundle.get("code");
        CurrentUser user = CurrentUser.getInstance();

        // set Name field
        TextView codeName = findViewById(R.id.code_name);
        codeName.setText(String.format("Name: %s",code.getName()));

        // set comment field
        if(user.hasComment(code.getHash())) {
            String comment = (String)user.getCodes().get(code.getHash()).get("comment");
            TextView codeComment = findViewById(R.id.commentField);
            codeComment.setText(comment);
        }

        // set point value
        TextView pointValue = findViewById(R.id.point_value);
        pointValue.setText(String.format(Locale.CANADA,"Point Value: %d",code.getPoints()));

        // set geolocation (UNFINISHED AS USER DOES NOT STOre CODE GEOLOCATION YET, CURRENTLY
        // USING PLACEHOLDER VALUE
        TextView geolocation = findViewById(R.id.code_geolocation);
        String location = (String)user.getCodes().get(code.getHash()).get("geolocation");
        geolocation.setText(String.format(Locale.CANADA,"Geolocation: %s",location));

        //TODO setup get geolocation, get image, and get comment in user, setup imageView

        // setup close button
        Button close = findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // setup delete button
        Button delete = findViewById(R.id.delete_code_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(NewCode.this).
                        setTitle("Delete Code").
                        setMessage("Are you sure you want to delete this Code?").
                        setPositiveButton("Yes", (dialog, which) -> {
                            user.removeCode(code.getHash(), code.getPoints());
                            finish();
                        }).
                        setNegativeButton("No", (dialog, which) -> {
                        }).

                        create();
                alertDialog.show();
            }
        });
    }
}
