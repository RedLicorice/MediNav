package it.unirc.barbiana20.medinav;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MarkCard extends CommonActivity {

    Location curLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_card);
        initToolbar();
        Intent callerIntent = getIntent();
        String imagePath = callerIntent.getStringExtra("CARD_IMAGE");
        String desc = callerIntent.getStringExtra("CARD_DESC");
        if(imagePath != null) {
            Log.d("MarkCard","Setting image path "+imagePath);
            setImage(imagePath);
        }
        if(desc != null) {
            Log.d("MarkCard","Setting description "+desc);
            setDescription(desc);
        }
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
