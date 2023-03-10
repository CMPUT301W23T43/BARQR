package com.example.barqrxmls;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


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
public class CameraActivity extends AppCompatActivity {
    String data="";
    byte[] dataBytes;
    Bitmap bitmapOfLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

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
     * Launches the activity for taking the picture of the surrounding image.
     */
    private void captureImage() {
        // <Muhammad Saad> (<Dec 25, 2022  >) <How to Take Image From Camera> [<source>] https://www.youtube.com/watch?v=JMdHMMEO8ZQ&t=400s
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activitySurroundingResultLauncher.launch(intent);
    }

    /**
     * The activity for the image of surrounding location.
     * @return the generated bitmap of the image of surrounding location.
     */
    ActivityResultLauncher<Intent> activitySurroundingResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null){
                    Bundle bundle = result.getData().getExtras();
                    bitmapOfLocation=(Bitmap) bundle.get("data");
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

        backToMain.putExtra(qrData,data);
        backToMain.putExtra(bitmap,bitmapOfLocation);
        setResult(15, backToMain);
        //startActivity(backToMain);
        finish();
    }







}