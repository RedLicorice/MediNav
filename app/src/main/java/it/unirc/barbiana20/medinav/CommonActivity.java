package it.unirc.barbiana20.medinav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by giuse on 30/08/2016.
 *
 * This class is the base class for our activity, it contains common code used in both
 * main Activity and MapView activity, results are handled in each activity separately,
 * because eg. after scanning a QR code, we have to place ourselves on the map.
 */

public class CommonActivity extends AppCompatActivity {
    protected static MapManager mm;
    public void InitMapManager(){
        //Load map data from assets - contains data for ALL maps
        //ToDo: Performance testing
        String jsonData;
        try {
            InputStream jsonStream = getAssets().open("mapData.json");
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonData = new String(buffer, "UTF-8");
            JSONArray jsonUniversityArray = new JSONArray(jsonData);
            mm = new MapManager(jsonUniversityArray);
            Log.d("Info","MapManager Loaded");
        } catch(IOException e) {
            e.printStackTrace();
            mm = null;
            Log.e("MapData","IO Exception!!");
        }
        catch(org.json.JSONException e)
        {
            e.printStackTrace();
            mm = null;
            Log.e("MapData","Json ERROR!!");
        }
    }

    //Launch MarkCard Activity
    public void LaunchMarkCard(Mark markToLaunch){
        if(markToLaunch.cardDesc == null || markToLaunch.cardDesc == "")
            return;
        Intent launchIntent = new Intent(this, MarkCard.class);
        launchIntent.putExtra("CARD_IMAGE", markToLaunch.cardImage);
        launchIntent.putExtra("CARD_DESC", markToLaunch.cardDesc);
        startActivity(launchIntent);
    }

    public void initToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void initiateQRScan(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(getString(R.string.qr_scan));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void scanQR(View view) {
        initiateQRScan();
    }
}
