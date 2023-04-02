package com.example.barqrxmls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

public class SearchQrCodeGeo extends AppCompatActivity {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");
    CollectionReference codesRef = dataBase.collection("Codes");
    private CodeArrayAdapter CodeAdapter;
    private ArrayList<Code> GeoCodeDataList;
    ListView GeoCodesList;
    EditText searchCity;
    Button Search;

    String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qr_code_geo);
        GeoCodesList = findViewById(R.id.codeList);
        GeoCodeDataList = new ArrayList<>();
        searchCity = findViewById(R.id.cityCountry);
        Search = findViewById(R.id.Search);



        Search.setOnClickListener(v -> {

            Geocoder geocoder = new Geocoder(SearchQrCodeGeo.this, Locale.getDefault());
            List<Address> addresses= null;

            List<Address> addresses2= null;
            location =  searchCity.getText().toString();

            if (location.length() != 0) {
                try {
                    addresses = geocoder.getFromLocationName(location, 1);
                    System.out.println(addresses);
                    double latitude = addresses.get(0).getLatitude();
                    double longitude = (addresses.get(0).getLongitude());
                    String address = (addresses.get(0).getAddressLine(0)).toString();
                    String city = (addresses.get(0).getLocality()).toString();
                    String country = (addresses.get(0).getCountryName()).toString();
                    SearchDatabase(location);
                    System.out.println(latitude);
                    System.out.println(longitude);




                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Context context = getApplicationContext();
                CharSequence error = "Please enter text before trying to search.";
                Toast toast = Toast.makeText(context, error, Toast.LENGTH_LONG);
                toast.show();
            }

//            codesRef.whereEqualTo("latitude", lat).whereEqualTo("longitude", lon).get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        // Access the document data
//                        Code obj = document.toObject(Code.class);
//                        Log.d("MyActivity", obj.toString());
//                        GeoCodeDataList.add(obj);
//                        CodeAdapter = new CodeArrayAdapter(SearchQrCodeGeo.this, GeoCodeDataList);
//                        GeoCodesList.setAdapter(CodeAdapter);
//                        CodeAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    // Handle errors here
//                    Log.e("MyActivity", "Error getting documents: " + task.getException().getMessage());
//                }
//            });
        });


    }

    private void SearchDatabase(String location){

        Query getCodesByLocation = codesRef.orderBy("points", Query.Direction.DESCENDING);

        Geocoder geocoder = new Geocoder(SearchQrCodeGeo.this, Locale.getDefault());
        List<Address> addresses2 = null;

        getCodesByLocation.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    int i = 1;
                    System.out.println("Hi"+task.getResult().getDocuments());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<HashMap<Object,Object>> latLongPairs = (ArrayList<HashMap<Object,Object>>) document.get("latLongPairs");
                        for (HashMap<Object, Object> latLong : latLongPairs) {
                                List<Address> addresses2 = null;
                                try {
                                    System.out.println("inTRY");
                                    addresses2 = geocoder.getFromLocation((Double) latLong.get("first"), (Double) latLong.get("second"), 1);
                                    System.out.println(addresses2.get(0).getLocality().toLowerCase());
                                    System.out.println(location.toLowerCase());
                                    if (addresses2.get(0).getLocality().toLowerCase().equalsIgnoreCase(location) || addresses2.get(0).getCountryName().equalsIgnoreCase(location) ||
                                            addresses2.get(0).getAdminArea().equalsIgnoreCase(location)) {
                                        System.out.println("match");
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