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
    //If user wants to navigate freely, he has to manually enter position.
    //Otherwise, QRScan (Inherited from CommonActivity) will be run.
    public void navigate(View view) {
        Intent intent = new Intent(this, ChooseFaculty.class);
        startActivity(intent);
    }
    //Retrieve QRCode scan result and pass it to MapShow Activity
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String re = scanResult.getContents();
            if(re != null) {
                Log.d("Scanned code:", re);
                Intent intent2 = new Intent(this, MapShow.class);
                intent2.putExtra("CURRENT_POSITION", re);
                startActivity(intent2);
            }
        } else {
            //Show error
            Log.d("Error scanning code!","");
        }

    }

    public void CheckPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {*/
                 ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            //}
        }
    }
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
