package it.unirc.barbiana20.medinav;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ChoosePosition extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_position);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    protected void Explore(View view){

    }
}
