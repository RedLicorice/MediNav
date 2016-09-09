package it.unirc.barbiana20.medinav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

/**
 * Created by schia on 09/09/2016.
 */

public class ChooseDestination extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_destination);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final ListView listView = (ListView) findViewById(R.id.listview_2);
        Intent universityChosenIntent = getIntent();
        int idUniversityChosen =  universityChosenIntent.getIntExtra("idUniversityChosen", 0);
        List<Location> locationList = mm.getLocations(idUniversityChosen);
        String[] destArray = new String[locationList.size()];
        for(int i = 0; i<locationList.size(); i++){
            Location location = locationList.get(i);
            destArray[i] = location.getName();
        }

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, R.layout.row, R.id.listview_list_2, destArray);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                String universityChosen = listView.getSelectedItem().toString();


            }
        });

    }
}
