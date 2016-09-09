package it.unirc.barbiana20.medinav;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class ChooseFaculty extends CommonActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_faculty);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final ListView listView = (ListView) findViewById(R.id.listview);
        List<University> uniList = mm.getUniList();
        String[] uniArray = new String[uniList.size()];
        for (int i = 0; i < uniList.size(); i++) {
            uniArray[i] = uniList.get(i).getName();
        }
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, R.layout.row, R.id.listview_list, uniArray);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                Intent universityChosenIntent = new Intent(ChooseFaculty.this, ChooseDestination.class);
                universityChosenIntent.putExtra("idUniversityChosen", position);
                startActivity(universityChosenIntent);

            }
        });

    }


}
