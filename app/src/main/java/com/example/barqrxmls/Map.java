/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the map.xml file
 */

package com.example.barqrxmls;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/***
 * The map activity. Show the map. It's a Map.
 */
// Most of this class not related directly to Code objects comes from the OSMdroid documentation
// In particular https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
public class Map extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map;

    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference codesRef = dataBase.collection("Codes");
    private ArrayList<Code> codeList;
    private MyLocationNewOverlay mLocationOverlay;
    private IMapController mapController;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double latitude, longitude;
    String country, city, address;
    GeoPoint lastCenterLocation = new GeoPoint(53.534444,  -113.490278);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Map settings, like User Agent for internet and loading osmdroid configuration
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.map);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Source: https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
        requestPermissionsIfNecessary((String[]) Arrays.asList(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        ).toArray());

        codesRef = dataBase.collection("Codes");
        codeList = new ArrayList<>();

    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(10.5);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(Map.this), map);
        // TODO: We have to make sure the location permission is enabled and stuff
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Map.this);
        getGeoLocation();

        codesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot codeDocument : task.getResult()) {
                        Code codeToAdd = codeDocument.toObject(Code.class);
                        Log.d("MAP AddMarkers", "onComplete: Adding object " + codeToAdd.getName() + " to our list");
                        codeList.add(codeToAdd);
                    }
                    showCodesOnMap();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationOverlay.disableMyLocation();
        map.onPause();
    }

    /***
     * Show the codes on the map, by placing identical looking markers at the geocoordinates of the
     * Code object.
     */
    private void showCodesOnMap() {
        List<IGeoPoint> points = new ArrayList<>();
        for (Code code : codeList) {
            ArrayList<LatLongPair> latLongPairs = code.getLatLongPairs();
            for (LatLongPair latLongPair : latLongPairs) {
                LabelledGeoPoint codeLocation = new LabelledGeoPoint(latLongPair.first, latLongPair.second, code.getName());
                double MAX_RANGE = 20000;
                if (lastCenterLocation.distanceToAsDouble(codeLocation) <= MAX_RANGE) {
                    points.add(new LabelledGeoPoint(latLongPair.first, latLongPair.second, code.getName()));
                }
            }
        }
        SimplePointTheme pt = new SimplePointTheme(points, true);
        Paint textStyle = new Paint();
        textStyle.setStyle(Paint.Style.FILL);
        textStyle.setColor(Color.parseColor("#000ff0"));
        textStyle.setTextAlign(Paint.Align.CENTER);
        textStyle.setTextSize(36);

        SimpleFastPointOverlayOptions opt = SimpleFastPointOverlayOptions.getDefaultStyle()
                .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
                .setRadius(20).setIsClickable(true).setCellSize(24).setTextStyle(textStyle);

        final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pt, opt);
        map.getOverlays().add(sfpo);
    }

    /***
     * Get the Geolocation of the user, if possible.
     */
    private void getGeoLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(Map.this, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        latitude = addresses.get(0).getLatitude();
                        longitude = (addresses.get(0).getLongitude());
                        address = (addresses.get(0).getAddressLine(0));
                        city = (addresses.get(0).getLocality());
                        country = (addresses.get(0).getCountryName());
                        //lastCenterLocation = new GeoPoint(latitude, longitude);
                        this.mLocationOverlay.enableMyLocation();
                        map.getOverlays().add(this.mLocationOverlay);
//                        System.out.println("The OSMDroid method gives us " + mLocationOverlay.getMyLocation());
//                        GeoPoint myLocation = new GeoPoint(latitude, longitude);//this.mLocationOverlay.getMyLocation();
//                        System.out.println("After all the map finding stuff...");
//                        if (myLocation.equals(new GeoPoint(0.0, 0.0))) {
//                            System.out.println("myLocation is null, get last fix is " + this.mLocationOverlay.getMyLocationProvider().getLastKnownLocation());
//                        } else {
//                            System.out.println("myLocation is not null! It is" + myLocation);
//                            lastCenterLocation = myLocation;
//                        }

                        Log.d("Map", "onResume: Setting center location to " + lastCenterLocation);
                        mapController.setCenter(lastCenterLocation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("MAP: Request Last Location: Inside the else{} within location != null for fusedLocation...");
                }
            });
        } else {
            System.out.println("MAP: We didn't have permission for finding location");
            ActivityCompat.requestPermissions(Map.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }

    /***
     * Request the required permissions for the map, if we need them.
     * @param permissions String array of the permissions we want to ask for.
     */
    // Source: OSMDroid documentation
    // Link: https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
