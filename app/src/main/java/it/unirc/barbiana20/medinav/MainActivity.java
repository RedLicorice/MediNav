package it.unirc.barbiana20.medinav;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends CommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        CheckPermissions();
        InitMapManager();
    }
    // Navigate button handler
    // User will be asked to choose  destination and then scan a QRCode,
    // A path will then be generated for the selected destination.
    public void onNavigate(View view) {
        Intent intent = new Intent(this, ChooseFaculty.class);
        startActivity(intent);
    }
    //Explore button handler
    // User will be asked to choose a starting point (or scan a QR Code)
    // the map will then be shown.
    public void onExplore(View view) {
        Intent intent = new Intent(this, ChooseFaculty.class);
        intent.putExtra("MODE",true);
        startActivity(intent);
    }

    //Check if user has granted permission to use camera. (This is Asynchronous)
    public void CheckPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
        }
    }
    //Handle permission request results: being the app camera-based, in case the user denies permission
    //to use camera, it will exit.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;//Permission granted
                } else {

                    AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
                    dlgBuilder.setTitle(R.string.permission_error_title);
                    dlgBuilder.setMessage(R.string.permission_error);
                    dlgBuilder.setCancelable(false);
                    dlgBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            System.exit(0);
                        }
                    });
                    AlertDialog alertDialog = dlgBuilder.create();
                    alertDialog.show();
                }
                return;
            }
        }
    }
}
