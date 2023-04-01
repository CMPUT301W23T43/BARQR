/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the map.xml file
 */

package com.example.barqrxmls;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;

public class Map extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map;

    private MyLocationNewOverlay mLocationOverlay;
    private IMapController mapController;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double latitude, longitude;

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        });

        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(15.5);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(Map.this), map);
        // TODO: We have to make sure the location permission is enabled and stuff
        ActivityCompat.requestPermissions(this, Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION).toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);

//        System.out.println("Our location is " + myLocation.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);
        GeoPoint myLocation = this.mLocationOverlay.getMyLocation();
        if (myLocation == null) {
            System.out.println("myLocation is null");
        } else {
            System.out.println("myLocation is not null! It is" + myLocation);
            lastCenterLocation = myLocation;
        }

        mapController.setCenter(lastCenterLocation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationOverlay.disableMyLocation();
        map.onPause();
    }

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
