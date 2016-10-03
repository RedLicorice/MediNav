package it.unirc.barbiana20.medinav;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class ChooseFaculty extends CommonActivity  {

    boolean isExplore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_faculty);
        initToolbar();
        isExplore = false;
        isExplore = getIntent().getBooleanExtra("MODE",isExplore);
        if(!isExplore)
        {
            Log.d("Mode","Explore mode not active!");
            //Hide FAB
            android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.qrScanFAB);
            fab.setVisibility(View.GONE);
        }
        final ListView listView = (ListView) findViewById(R.id.facultyList);
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
                Intent universityChosenIntent = new Intent(ChooseFaculty.this, (isExplore) ? ChooseStartingPoint.class : ChooseDestination.class);
                universityChosenIntent.putExtra("idUniversityChosen", position);
                startActivity(universityChosenIntent);

            }
        });
    }
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


}
