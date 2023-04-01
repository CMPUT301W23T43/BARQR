package com.example.barqrxmls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SearchQrCodeGeo extends AppCompatActivity {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");
    CollectionReference codesRef = dataBase.collection("Codes");
    private CodeArrayAdapter CodeAdapter;
    private ArrayList<Code> GeoCodeDataList;
    ListView GeoCodesList;
    EditText Latitude;
    EditText Longitude;
    Button Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qr_code_geo);
        GeoCodesList = findViewById(R.id.codeList);
        GeoCodeDataList = new ArrayList<>();
        Latitude = findViewById(R.id.Latitude);
        Longitude = findViewById(R.id.Longitude);
        String lat = Latitude.getText().toString();
        String lon = Longitude.getText().toString();

        Search.setOnClickListener(v -> {
            codesRef.whereEqualTo("latitude", lat).whereEqualTo("longitude", lon).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the document data
                        Code obj = document.toObject(Code.class);
                        Log.d("MyActivity", obj.toString());
                        GeoCodeDataList.add(obj);
                        CodeAdapter = new CodeArrayAdapter(SearchQrCodeGeo.this, GeoCodeDataList);
                        GeoCodesList.setAdapter(CodeAdapter);
                        CodeAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Handle errors here
                    Log.e("MyActivity", "Error getting documents: " + task.getException().getMessage());
                }
            });
        });


    }
}