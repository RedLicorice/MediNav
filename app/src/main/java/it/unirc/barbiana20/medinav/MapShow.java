package it.unirc.barbiana20.medinav;

import android.net.Uri;
import android.os.Bundle;
import android.content.*;
import android.graphics.*;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.*;
import com.onlylemi.mapview.library.*;
import com.onlylemi.mapview.library.layer.*;
import com.onlylemi.mapview.library.utils.*;
import java.io.*;
import java.util.*;

public class MapShow extends CommonActivity {
    //MapView and related layers
    private MapView mapView;
    private CustomMarkLayer markLayer;
    private RouteLayer routeLayer;
    private LocationLayer locationLayer;
    //Adapter class instance for using Floor data from MapManager in MapView
    private FloorAdapter floorAdapter;
    private Floor curFloor;
    //Current position data
    private PointF curPoint;
    private Location curPosition;
    //Current destination data
    private Location curDestination;
    private PointF destStair;//Used in navigation between floors

    /*
    *   Initialize MapShow Activity, processing user position and destination (sent as intent extras).
    *   If user position and destination are not available, the activity will be in "Explore" mode.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);
        initToolbar();
        curPoint = new PointF(0,0);//Current position in floor is initialized to 0,0
        InitMapView();//Initialize position - independent attributes
        //Grab user-choosen destination from caller intent
        String destTarget = getIntent().getStringExtra("TARGET_POSITION");
        if(destTarget != null){
            Log.d("Received Destination",destTarget);
            curDestination = new Location(destTarget);
        }
        //Grab starting point from caller intent (User has to scan a QRCode before navigation starts)
        String qrData = getIntent().getStringExtra("CURRENT_POSITION");
        if(qrData != null){
            Log.d("Received Position",qrData);
            curPosition = new Location(qrData);
            //Load current floor map
            LoadFloor(curPosition);
            //Update current position, in navigation mode this also checks if the user is in the correct building/university
            UpdatePosition(curPosition);
        }
    }
    /*
    * Initialize MapView with specified position
    */
    public void InitMapView(){
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setScaleAndRotateTogether(true);//Allow pinch-and-rotate gestures
        //Create Location Layer
        locationLayer = new LocationLayer(mapView);
        locationLayer.setOpenCompass(false);
        mapView.addLayer(locationLayer);
        //Create Route Layer
        routeLayer = new RouteLayer(mapView);
        mapView.addLayer(routeLayer);
        //Create Mark Layer
        markLayer = new CustomMarkLayer(mapView);
        //Set Mark click handler, this is called whenever the user taps on a mark.
        //We are not using this feature for now.
                    /*markLayer.setMarkIsClickListener(new MarkLayer.MarkIsClickListener() {
                        @Override
                        public void markIsClick(int num) {
                            //num is the Mark ID IN CURRENT FLOOR!

                            return;
                        }
                    });*/
        mapView.addLayer(markLayer);
    }

    private float zoom;//Holds current zoom information between floor changes
    public void LoadFloor(Location pos)
    {
        try {
            //Load Floor Data
            curFloor = mm.getFloor(pos);//Get current floor Data from MapManager - It's preloaded at application start!
            floorAdapter = new FloorAdapter(curFloor);//Instantiate the adapter class for MapView
            Bitmap mapImage = BitmapFactory.decodeStream(getAssets().open(curFloor.getMap()));//Load map image from assets
            MapUtils.init(floorAdapter.getNodeCount(), floorAdapter.getEdgeCount());//Initialize MapUtils with current floor graph
            //Initialize map view
            mapView = (MapView) findViewById(R.id.mapview);
            //save zoom
            zoom = 0.0F;
            if(mapView.isMapLoadFinish())
                zoom = mapView.getCurrentZoom();
            mapView.loadMap(mapImage);

            mapView.setMapViewListener(new MapViewListener() {
                //This is called AFTER mapView's base image is loaded!
                @Override
                public void onMapLoadSuccess() {
                    Log.d("MAP_LOAD","Map load success");
                    routeLayer.setRouteList(null);//Reset route
                    routeLayer.setNodeList(floorAdapter.getNodes());
                    markLayer.setMarks(curFloor.marks);
                    mapView.refresh();
                    //Restore zoom
                    if(zoom != 0.0F)
                        mapView.setCurrentZoom(zoom);
                }

                @Override
                public void onMapLoadFail() {
                    AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(getApplicationContext());
                    dlgBuilder.setTitle(R.string.map_loading_error_title);
                    dlgBuilder.setMessage(R.string.map_loading_error);
                    dlgBuilder.setCancelable(false);
                    dlgBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            System.exit(0);
                        }
                    });
                    AlertDialog alertDialog = dlgBuilder.create();
                    alertDialog.show();
                }

            });
            Toast.makeText(getApplicationContext(),R.string.map_loaded,Toast.LENGTH_SHORT).show();
        } catch(IOException e){
            e.printStackTrace();
        } catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    /*
     *  Handle position updates in same floor
     */
    public void UpdateFloorPosition(Location location){
        curPoint = curFloor.getMark(location.getMarkId()).pos;
        curPosition = location;
        if(locationLayer != null) {
            locationLayer.setCurrentPosition(curPoint);
        }
        mapView.mapCenterWithPoint(curPoint.x, curPoint.y);
        mapView.refresh();
        Toast.makeText(getApplicationContext(),R.string.position_updated,Toast.LENGTH_SHORT).show();
    }
    /*
     * Handle university, building and floor switching, as well as updating user position.
     */
    private Uri changeUniIntentUri;
    public void UpdatePosition(Location location)
    {
        if(curPosition.getFloorId() != location.getFloorId()) {
            LoadFloor(location);
        }
        UpdateFloorPosition(location);//Update position in floor
        routeLayer.setRouteList(null);
        if(curDestination != null){
            Log.d("Navigate-Dest:",curDestination.jsonSerialize());
            //Toast.makeText(getApplicationContext(),"[DEBUG]Destination set!",Toast.LENGTH_LONG).show();
        }
        ///Update route / building
        //curDestination = new Location(0,0,4,0);
        //Target is in different university
        if(curDestination != null && curDestination.getUniversityId() != location.getUniversityId())
        {
            //Destination is in another university, show dialog
            University destUniversity = mm.getUniversity(curDestination.getUniversityId());
            String lat = String.format("%.6f",destUniversity.getLatitude()).replace(',','.');
            String lng = String.format("%.6f",destUniversity.getLongitude()).replace(',','.');
            changeUniIntentUri = Uri.parse("geo:"+ lat+","+lng+"?q="+lat+","+lng);
            Log.d("Navigation URI",changeUniIntentUri.toString());
            AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
            dlgBuilder.setTitle(R.string.change_group_dialog_title);
            dlgBuilder.setMessage(String.format(getResources().getString(R.string.change_group_dialog_message),destUniversity.getName()));
            dlgBuilder.setCancelable(false);
            dlgBuilder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, changeUniIntentUri);
                    //mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.no_compatible_app,Toast.LENGTH_LONG).show();
                    }
                    dialog.cancel();
                }
            });
            dlgBuilder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, start QR Scan and close.
                    initiateQRScan();
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = dlgBuilder.create();
            alertDialog.show();
            return;
        }
        //Target is in different building
        if(curDestination != null && curDestination.getBuildingId() != location.getBuildingId())
        {
            //Destination is in another building, show dialog
            Building destBuilding = mm.getBuilding(curDestination);
            AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
            dlgBuilder.setTitle(R.string.change_building_dialog_title);
            dlgBuilder.setMessage(String.format(getResources().getString(R.string.change_building_dialog_message),destBuilding.getName()));
            dlgBuilder.setCancelable(false);
            dlgBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    initiateQRScan();
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = dlgBuilder.create();
            alertDialog.show();
            return;
        }
        //Target is in different floor
        if(curDestination != null && curDestination.getFloorId() != location.getFloorId())
        {
            Floor f = mm.getFloor(location);
            Mark m = f.marks.get(location.getMarkId());
            if(destStair != null && curPoint.equals(destStair) || m.isStair()) {
                //Target has reached a stair: Invite him to reach correct floor
                destStair = null;
                UpdateFloorPosition(location);
                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
                dlgBuilder.setTitle(R.string.change_floor_dialog_title);
                dlgBuilder.setMessage(String.format(getResources().getString(R.string.change_floor_dialog_message),curDestination.getFloorId(),curPosition.getFloorId()));
                dlgBuilder.setCancelable(false);
                dlgBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        initiateQRScan();
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = dlgBuilder.create();
                alertDialog.show();
                return;
            } else {
                //If it's the first scan with a target in different floor, destStair is null
                //Find nearest waypoint with "stair" attribute and set a route to it.
                destStair = curPoint;//We assume user is on a stair. If he is not...
                if(!m.isStair())//we find the closest stair or elevator, and set it as our destination..
                {
                    double min = 0;
                    for (Mark p : curFloor.marks) {
                        if(p.type != Mark.Types.Elevator && p.type != Mark.Types.Stair)
                            continue;
                        double res = Math.sqrt(Math.pow(p.pos.x - curPoint.x, 2) + Math.pow(p.pos.y - curPoint.y, 2));
                        if (min == 0 || res < min) {
                            min = res;
                            destStair = p.pos;
                        }
                    }
                    List<Integer> routeList = MapUtils.getShortestDistanceBetweenTwoPoints(curPoint, destStair, floorAdapter.getNodes(), floorAdapter.getEdges());
                    routeLayer.setRouteList(routeList);
                } else {
                    Toast.makeText(getApplicationContext(),"You already are on a stair!",Toast.LENGTH_LONG).show();
                }
                mapView.refresh();
                return;
            }
        }
        //Target is in same floor
        if(curDestination != null &&
                curDestination.getUniversityId() == location.getUniversityId() &&
                curDestination.getBuildingId() == location.getBuildingId() &&
                curDestination.getFloorId() == location.getFloorId())
        {
            Floor f = mm.getFloor(location);
            Mark m = f.marks.get(curDestination.getMarkId());
            List<Integer> routeList = MapUtils.getShortestDistanceBetweenTwoPoints(curPoint, m.pos, floorAdapter.getNodes(), floorAdapter.getEdges());
            routeLayer.setRouteList(routeList);
            mapView.refresh();
        }
    }

    //QRCode scan callback
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                String re = scanResult.getContents();
                if (re == null) {
                    //Scan aborted or not succesful
                    return;
                }
                //Data in QRCodes will be stored in json format, since it is human readable (easier generation)
                Location location = new Location(re);
                UpdatePosition(location);
            } else {
                Log.d("Error","Scan result is null!");
            }
        } catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
