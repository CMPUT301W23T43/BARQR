package com.example.barqrxmls;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.util.Log;
import android.widget.Toast;


/**
 * QR Camera and Capture Location implementation
 * @author Shafayat Sadman
 * @version 1
 * Has no Unit and UI tests as the QR code have to be scanned manually after opening camera.
 * @return
 *      String data: Data of the QR code that is scanned.
 *      Bitmap bitmapOfLocation: The bitmap of the image of the surrounding Location.
 * Citations:
 *              SCANCODE = "https://www.youtube.com/watch?v=jtT60yFPelI&t=18s"
 *              Take Image = "https://www.youtube.com/watch?v=JMdHMMEO8ZQ&t=400s"
 *
 *
 */
public class CameraActivity extends AppCompatActivity  {
    String data="";
    byte[] dataBytes;
    Bitmap bitmapOfLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    String country,city,address;
    double longitude;
    double latitude;

    byte[] compressedBytes=null;

    Bitmap compBitmap;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);


        scanCode();


    }


    /**
     * Implements the QR scanner by starting the Capture Act.
     */
    public void scanCode(){
        // <Cambo Tutorial> (<Mar 18, 2022>) <How to implement QR Scanner> [<source>] https://www.youtube.com/watch?v=jtT60yFPelI&t=18s
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up for Flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);



    }

    /**
     * The activity for Scanning QR code.
     * Has two Alert Dialog, one for scanning QR code and  one for taking picture of surrounding location
     * User can take surrounding Picture after having successfully scanning an QR code.
     */
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result-> {
        if(result.getContents() !=null) {


            AlertDialog.Builder surroundingBuilder = new AlertDialog.Builder(CameraActivity.this);
            surroundingBuilder.setTitle("Do you want to take a picture of the surrounding?");

            surroundingBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getBackToMain();
                    dialogInterface.dismiss();
                }
            });
            surroundingBuilder.setPositiveButton("Take", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    captureImage();

                    dialogInterface.dismiss();
                }
            }).show();
            AlertDialog.Builder geoLocationBuilder = new AlertDialog.Builder(CameraActivity.this);
            geoLocationBuilder.setTitle("Do you want to take a Geolocation of surrounding?");
            geoLocationBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }

            });
            geoLocationBuilder.setPositiveButton("Take Location", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    geoLocation();

                    dialogInterface.dismiss();
                }
            }).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
            builder.setTitle("QR Code Successfully Scanned!");
            builder.setMessage(result.getContents());
            data= result.getContents();
            dataBytes=result.getRawBytes();
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                    dialogInterface.dismiss();
                }
            }).show();

        }
    });

    /**
     * Launches the activity for taking the Geolocation.
     */
    public void geoLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location !=null){
                                Geocoder geocoder=new Geocoder(CameraActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    latitude=addresses.get(0).getLatitude();
                                    longitude=(addresses.get(0).getLongitude());
                                    address=(addresses.get(0).getAddressLine(0)).toString();
                                    city=(addresses.get(0)).getLocality().toString();
                                    country=(addresses.get(0).getCountryName()).toString();}
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
        else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(CameraActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                geoLocation();
            }
            else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Launches the activity for taking the picture of the surrounding image.
     */
    private void captureImage() {
        // <Muhammad Saad> (<Dec 25, 2022  >) <How to Take Image From Camera> [<source>] https://www.youtube.com/watch?v=JMdHMMEO8ZQ&t=400s
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activitySurroundingResultLauncher.launch(intent);
    }





    /**
     * The activity for the image of surrounding location and compresses the image.
     * @return the generated bitmap of the image of surrounding location .
     */
    ActivityResultLauncher<Intent> activitySurroundingResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null){
                    Bundle bundle = result.getData().getExtras();
                    bitmapOfLocation=(Bitmap) bundle.get("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapOfLocation.compress(Bitmap.CompressFormat.JPEG,50,stream);
                    compressedBytes=stream.toByteArray();
                    compBitmap= BitmapFactory.decodeByteArray(compressedBytes,0,compressedBytes.length);

                    getBackToMain();


                }
            }
    );

    public String getData(){
        return data;
    }
    public Bitmap getBitmap(){
        return bitmapOfLocation;
    }
    public void getBackToMain(){
        Intent backToMain = new Intent(CameraActivity.this, MainActivity.class);
        String qrData = "codeData";
        String bitmap="codeBitmap";
        String mAddress = "codeAddress";
        String mCountry="codeCountry";
        String mLongitude = "codeLongitude";
        String mLatitude="codeLatitude";
        String mCity="codeCity";
        String mByteArray="codeByteArray";


        backToMain.putExtra(qrData,data);
        backToMain.putExtra(bitmap,compBitmap);
        backToMain.putExtra(mAddress,address);
        backToMain.putExtra(mCountry,country);
        backToMain.putExtra(mLongitude,longitude);
        backToMain.putExtra(mLatitude,latitude);
        backToMain.putExtra(mCity,city);
        backToMain.putExtra(mByteArray,compressedBytes);

        setResult(15, backToMain);
        finish();
    }



}