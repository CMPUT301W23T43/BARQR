package com.example.barqrxmls;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;

public class MapLogic {

    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference codesRef = dataBase.collection("Codes");
    private ArrayList<Code> codeList = new ArrayList<>();
    GeoPoint lastCenterLocation;
    double MAX_RANGE;
    public MapLogic(GeoPoint lastCenterLocation, double MAX_RANGE) {
        this.lastCenterLocation = lastCenterLocation;
        this.MAX_RANGE = MAX_RANGE;
    }

    public void updateLastCenterLocation(GeoPoint newLocation) {
        lastCenterLocation = newLocation;
    }

    /***
     * Add Codes to the list of codes. The codes that are added are converted from entries in the
     * database.
     */
    public void populateCodeList() {
        codesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot codeDocument : task.getResult()) {
                        Code codeToAdd = codeDocument.toObject(Code.class);
                        Log.d("MAP AddMarkers", "onComplete: Adding object " + codeToAdd.getName() + " to our list");
                        codeList.add(codeToAdd);
                    }
                }
            }
        });
    }

    /***
     * Show the codes on the map, by placing identical looking markers at the coordinates of the
     * Code object. The returned Overlay has to be added to the Map overlays.
     * @return A SimpleFastPointOverlay (an Overlay) for adding to the map. Contains all the points as markers.
     */
    public SimpleFastPointOverlay showCodesOnMap() {
        List<IGeoPoint> points = new ArrayList<>();
        for (Code code : codeList) {
            ArrayList<LatLongPair> latLongPairs = code.getLatLongPairs();
            for (LatLongPair latLongPair : latLongPairs) {
                LabelledGeoPoint codeLocation = new LabelledGeoPoint(latLongPair.first, latLongPair.second, code.getName());
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

        return new SimpleFastPointOverlay(pt, opt);
    }
}
