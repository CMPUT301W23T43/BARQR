/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the map.xml file
 */

package com.example.barqrxmls;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

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

    private MyLocationNewOverlay mLocationOverlay;
    private IMapController mapController;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double latitude, longitude;
    String country, city, address;
    GeoPoint lastCenterLocation = new GeoPoint(53.534444,  -113.490278);

    MapLogic mapLogician;

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

        mapLogician = new MapLogic(lastCenterLocation, 20000);

    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(10.5);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(Map.this), map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Map.this);
        getGeoLocation();

        mapLogician.populateCodeList();
        map.getOverlays().add(mapLogician.showCodesOnMap());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationOverlay.disableMyLocation();
        map.onPause();
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
