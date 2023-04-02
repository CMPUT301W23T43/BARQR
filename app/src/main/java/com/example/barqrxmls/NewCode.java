/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the
 */

package com.example.barqrxmls;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Locale;

//PUT IN MAIN
//
//        CodesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//              @Override
//          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Code code = CodeDataList.get(i);
//        Intent newCodeSwitch = new Intent(MainActivity.this,NewCode.class);
//        newCodeSwitch.putExtra("code",code);
//
//        }
//        });

public class NewCode extends AppCompatActivity {

    String comment = "";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_screen);

        // get code and user
        Bundle bundle = getIntent().getExtras();
        Code code = (Code) bundle.getSerializable("code");
        CurrentUser user = CurrentUser.getInstance();
        System.out.println(user.getCodes());
        System.out.println(code.getHash());

        // set Name field
        TextView codeName = findViewById(R.id.code_name);
        codeName.setText(String.format("Name: %s",code.getName()));

        EditText codeComment = findViewById(R.id.commentField);
        // set comment field
        if(user.hasComment(code.getHash())) {
            comment = user.getComment(code.getHash());
            codeComment.setText(comment);
        }

        // set point value
        TextView pointValue = findViewById(R.id.point_value);
        pointValue.setText(String.format(Locale.CANADA,"Point Value: %d",code.getPoints()));

        // set geolocation (UNFINISHED AS USER DOES NOT STOre CODE GEOLOCATION YET, CURRENTLY
        // USING PLACEHOLDER VALUE
        TextView geolocation = findViewById(R.id.code_geolocation);
        String location = user.getGeoLocation(code.getHash());
        geolocation.setText(String.format(Locale.CANADA,"Geolocation: %s",location));

        // set up the image
        ImageView imageBlock = findViewById(R.id.myImage);
        byte[] bytes = user.getImage(code.getHash());
        if(bytes != null) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageBlock.setImageBitmap(image);
        }

        // setup player scans button
        Button playerScans = findViewById(R.id.playerScansButton);
        playerScans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(code.getHash());
                Intent scans = new Intent(NewCode.this,PlayerScans.class);
                scans.putExtra("code",code);
                startActivity(scans);
            }
        });

        // setup close button
        Button close = findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if the code comment has changed
                if(!codeComment.getText().toString().equals(comment)) {
                    user.addComment(code.getHash(),codeComment.getText().toString());
                }
                Intent newIntent = new Intent(NewCode.this,MainActivity.class);
                startActivity(newIntent);
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
                            Intent newIntent = new Intent(NewCode.this,MainActivity.class);
                            startActivity(newIntent);
                        }).
                        setNegativeButton("No", (dialog, which) -> {
                        }).

                        create();
                alertDialog.show();
            }
        });
    }
}
