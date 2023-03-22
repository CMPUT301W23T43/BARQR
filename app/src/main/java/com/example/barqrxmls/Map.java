/**
 * @author Noah Jeans
 * @version 1
 * This class connects to the map.xml file
 */

package com.example.barqrxmls;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

//    private ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
//                if (isGranted.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    // TODO: Call a nice function that does all our work for us
//                    showMap();
//                } else {
//                    // TODO: Just pop up a dialog or something and show a blank screen
//                    System.out.println("We don't have permission :(");
//                }
//            });
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
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();

        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        // TODO: We have to make sure the location permission is enabled and stuff
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
        GeoPoint myLocation = mLocationOverlay.getMyLocation();
//        System.out.println("Our location is " + myLocation.toString());
        mapController.setCenter(myLocation);
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
