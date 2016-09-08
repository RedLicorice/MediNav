package it.unirc.barbiana20.medinav;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChoosePosition extends CommonActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_position);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ListView listView = (ListView)findViewById(R.id.listview);
        String [] array = {"Ingegneria", "Agraria", "Architettura", "Giurisprudenza"};
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, R.layout.row, R.id.listview_list, array);
        listView.setAdapter(arrayAdapter);
    }


    protected void Explore(View view){

    }
}
