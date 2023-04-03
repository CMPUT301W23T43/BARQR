package com.example.barqrxmls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *  This class allows the current user to search for any codes in the database that
 *  have been scanned at a certain location. Allows search by City, Province, or Country.
 *
 * @author Anjelica Marianicz
 * @author Kannan Khosla
 * @Version 2
 *
 */
public class SearchQrCodeGeo extends AppCompatActivity {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    CollectionReference codesRef = dataBase.collection("Codes");
    private CodeArrayAdapter CodeAdapter;
    private ArrayList<Code> GeoCodeDataList;
    ListView GeoCodesList;
    EditText searchCity;
    Button Search;
    String location;
    Button home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qr_code_geo);
        GeoCodesList = findViewById(R.id.codeList);
        GeoCodeDataList = new ArrayList<>();
        searchCity = findViewById(R.id.cityCountry);
        Search = findViewById(R.id.Search);
        home = findViewById(R.id.go_Back);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Search.setOnClickListener(v -> {
            GeoCodeDataList.clear();
            CodeAdapter = new CodeArrayAdapter(SearchQrCodeGeo.this, GeoCodeDataList);
            GeoCodesList.setAdapter(CodeAdapter);
            CodeAdapter.notifyDataSetChanged();
            Geocoder geocoder = new Geocoder(SearchQrCodeGeo.this, Locale.getDefault());
            List<Address> addresses= null;
            location =  searchCity.getText().toString();

            if (location.length() != 0) {
                try {
                    addresses = geocoder.getFromLocationName(location, 1);
                    if (addresses.isEmpty()) {
                        Context context = getApplicationContext();
                        CharSequence error = "Invalid search. Try a city, province, or country name.";
                        Toast toast = Toast.makeText(context, error, Toast.LENGTH_LONG);
                        toast.show();
                    }else {
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = (addresses.get(0).getLongitude());
                        SearchDatabase(location);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Context context = getApplicationContext();
                CharSequence error = "Please enter text before trying to search.";
                Toast toast = Toast.makeText(context, error, Toast.LENGTH_LONG);
                toast.show();
            }
        });


    }
    /**
     * This method queries the database to lok through every code to find which match geolocation.
     * @param location This is the location entered by the user which the code must match.
     */
    private void SearchDatabase(String location){

        Query getCodesByLocation = codesRef.orderBy("points", Query.Direction.DESCENDING);

        Geocoder geocoder = new Geocoder(SearchQrCodeGeo.this, Locale.getDefault());
        // Queries the database to find all codes and convert their latitudes and longitudes to the user input location.
        getCodesByLocation.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<HashMap<Object,Object>> latLongPairs = (ArrayList<HashMap<Object,Object>>) document.get("latLongPairs");
                        for (HashMap<Object, Object> latLong : latLongPairs) {
                                List<Address> addresses2 = null;
                                try {
                                    addresses2 = geocoder.getFromLocation((Double) latLong.get("first"), (Double) latLong.get("second"), 1);

                                    if (addresses2.get(0).getLocality().toLowerCase().equalsIgnoreCase(location) || addresses2.get(0).getCountryName().equalsIgnoreCase(location) ||
                                            addresses2.get(0).getAdminArea().equalsIgnoreCase(location)) {
                                        GeoCodeDataList.add(new Code((String) document.get("hash")));
                                        CodeAdapter = new CodeArrayAdapter(SearchQrCodeGeo.this, GeoCodeDataList);
                                        GeoCodesList.setAdapter(CodeAdapter);
                                        CodeAdapter.notifyDataSetChanged();
                                    }

                                } catch (IOException e) {

                                    e.printStackTrace();
                                }

                        }
                    }
                }
            }
        });
    }
}