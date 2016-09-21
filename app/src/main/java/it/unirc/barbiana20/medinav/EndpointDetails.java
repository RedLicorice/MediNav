package it.unirc.barbiana20.medinav;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class EndpointDetails extends CommonActivity {

    Location curLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endpoint_details);
        initToolbar();
        Intent callerIntent = getIntent();
        String serializedLocation = callerIntent.getStringExtra("LOCATION");
        if(serializedLocation == null){
            Log.e("CardInit","Location not received!");
            this.finish();//Exit activity
        }
        curLoc = new Location(serializedLocation);
    }
    private void setDescription(String desc){
        TextView descText = (TextView)findViewById(R.id.cardDesc);
        descText.setText(desc);
    }
    private void setImage(String path){
        try{
            Drawable d = Drawable.createFromStream(getAssets().open(path), null);
            ImageView image = (ImageView)findViewById(R.id.cardImage);
            image.setImageDrawable(d);
        } catch(IOException ex){
            Log.e("Load image","Failed to load image!");
            ex.printStackTrace();
        }
    }
}
