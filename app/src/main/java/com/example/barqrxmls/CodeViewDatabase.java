package com.example.barqrxmls;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class CodeViewDatabase extends AppCompatActivity {

        ImageView uniqueRepr;
        ImageView surroundings;

        String comment = "";
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.player_barqr_code);

            // get code and user
            Bundle bundle = getIntent().getExtras();
            Code code = (Code) bundle.get("code");
            CurrentUser user = CurrentUser.getInstance();

            // set Name field
            TextView codeName = findViewById(R.id.nameField);
            codeName.setText(code.getName());

            TextView codeComment = findViewById(R.id.commentField);
            // set comment field
            if(user.hasComment(code.getHash())) {
                comment = user.getComment(code.getHash());
                codeComment.setText(comment);
            }
            // Set unique representation.
            uniqueRepr = findViewById(R.id.barQRImage);
            uniqueRepr.setImageBitmap(code.generateImage());

            // set point value
            TextView pointValue = findViewById(R.id.pointsField);
            pointValue.setText(code.getPoints().toString());

            // set geolocation
            TextView geolocation = findViewById(R.id.code_geolocation);
            String location = user.getGeoLocation(code.getHash());
            geolocation.setText(String.format(Locale.CANADA,"Geolocation: %s",location));

            // set up the image
            ImageView imageBlock = findViewById(R.id.playerImage);
            byte[] bytes = user.getImage(code.getHash());
            if(bytes != null) {

                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageBlock.setImageBitmap(image);
            }

//            // setup player scans button
//            Button playerScans = findViewById(R.id.playerScansButton);
//            playerScans.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent scans = new Intent(com.example.barqrxmls.CodeViewDatabase.this, PlayerScans.class);
//                    scans.putExtra("code",code);
//                    startActivity(scans);
//                }
//            });

            Taskbar taskbar = Taskbar.getInstance(CodeViewDatabase.this);

            ImageButton leader = (ImageButton) findViewById(R.id.leaderBoardButton);
            leader.setOnClickListener(taskbar.getSwitchActivityMap().get("LeaderBoard"));
            // setting screen changes from taskbar
            // <Praveenkumar, Gary> (<Nov. 9, 2016>) <How to switch between screens?> (<4>) [<source code>] https://stackoverflow.com/questions/7991393/how-to-switch-between-screens

            // Button to return to homepage of application.
            ImageButton home = (ImageButton) findViewById(R.id.homeButton);
            home.setOnClickListener(taskbar.getSwitchActivityMap().get("MainActivity"));



            // Button to change to the map of the application.
            ImageButton map = (ImageButton) findViewById(R.id.mapButton);
            map.setOnClickListener(taskbar.getSwitchActivityMap().get("Map"));

            // Button to view the current user's account information.
            ImageButton account = (ImageButton) findViewById(R.id.settingsButton);
            account.setOnClickListener(taskbar.getSwitchActivityMap().get("Account"));

            // setup close button
            Button close = findViewById(R.id.closeButton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
    }

