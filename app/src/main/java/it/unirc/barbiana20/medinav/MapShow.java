package it.unirc.barbiana20.medinav;

import android.os.Bundle;
import android.content.*;
import android.graphics.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.zxing.integration.android.*;
import com.onlylemi.mapview.library.*;
import com.onlylemi.mapview.library.layer.*;
import com.onlylemi.mapview.library.utils.*;
import java.io.*;
import java.util.*;

public class MapShow extends CommonActivity {
    //MapView and related layers
    private MapView mapView;
    private MarkLayer markLayer;
    private RouteLayer routeLayer;
    private LocationLayer locationLayer;
    //Adapter class instance for using Floor data from MapManager in MapView
    private FloorAdapter floorAdapter;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        curPoint = new PointF(0,0);//Current position in floor is initialized to 0,0
        //Grab user-choosen destination from caller intent
        String destTarget = getIntent().getStringExtra("TARGET_POSITION");
        if(destTarget != null){
            curDestination = ParseTarget(destTarget);
        }
        //Grab starting point from caller intent (User has to scan a QRCode before navigation starts)
        String qrData = getIntent().getStringExtra("CURRENT_POSITION");
        if(qrData != null){
            curPosition = ParseTarget(qrData);
            //Load current floor map
            LoadFloor(curPosition);
            //Update current position, in navigation mode this also checks if the user is in the correct building/university
            UpdatePosition(curPosition);
        }
    }

    /*
    * Initialize MapView with specified position
    */
    public void LoadFloor(Location pos)
    {
        try {
            //Load Floor Data
            Floor floor = mm.getFloor(pos);//Get current floor Data from MapManager - It's preloaded at application start!
            floorAdapter = new FloorAdapter(floor);//Instantiate the adapter class for MapView
            Bitmap mapImage = BitmapFactory.decodeStream(getAssets().open(floor.getMap()));//Load map image from assets
            MapUtils.init(floorAdapter.getNodeCount(), floorAdapter.getEdgeCount());//Initialize MapUtils with current floor graph
            //Initialize map view
            mapView = (MapView) findViewById(R.id.mapview);
            mapView.destroyDrawingCache();

            mapView.loadMap(mapImage);
            mapView.setScaleAndRotateTogether(true);//Allow pinch-and-rotate gestures
            mapView.setMinZoom(1.3F);
            mapView.setMaxZoom(3F);
            //Set event handlers for MapView
            mapView.setMapViewListener(new MapViewListener() {
                //This is called AFTER mapView's base image is loaded!
                @Override
                public void onMapLoadSuccess() {
                    //Create Location Layer
                    locationLayer = new LocationLayer(mapView, curPoint);
                    mapView.setCurrentZoom(3,curPoint.x, curPoint.y);
                    locationLayer.setOpenCompass(false);
                    mapView.addLayer(locationLayer);
                    //Create Route Layer
                    routeLayer = new RouteLayer(mapView);
                    routeLayer.setNodeList(floorAdapter.getNodes());
                    mapView.addLayer(routeLayer);
                    //Create Mark Layer
                    markLayer = new MarkLayer(mapView, floorAdapter.getMarks(), floorAdapter.getMarkNames());
                    //Set Mark click handler, this is called whenever the user taps on a mark
                    markLayer.setMarkIsClickListener(new MarkLayer.MarkIsClickListener() {
                        @Override
                        public void markIsClick(int num) {
                            //num is the Mark ID IN CURRENT FLOOR!
                            //ATM we do not want to do anything on mark click, so just return.
                            return;
                        }
                    });
                    mapView.addLayer(markLayer);
                    mapView.refresh();
                }

                @Override
                public void onMapLoadFail() {
                    //ToDo: Handle map loading failure (Fatal error!)
                }

            });
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
        curPoint = floorAdapter.getMark(location.getMarkId());
        curPosition = location;
        if(locationLayer != null) {
            locationLayer.setCurrentPosition(curPoint);
        }
        mapView.mapCenterWithPoint(curPoint.x, curPoint.y);
        mapView.setCurrentZoom(3,curPoint.x, curPoint.y);
        mapView.refresh();
        mapView.refresh();
        mapView.refresh();
    }
    /*
     * Handle university, building and floor switching, as well as updating user position.
     */
    public void UpdatePosition(Location location)
    {
        if(curPosition.getFloorId() != location.getFloorId())
            LoadFloor(location);
        UpdateFloorPosition(location);//Update position in floor
        ///Update route / building
        //University switching
        if(curDestination != null && curDestination.getUniversityId() != location.getUniversityId())
        {
            //Destination is in another university, show dialog
            University destUniversity = mm.getUniversity(curDestination.getUniversityId());
            AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
            dlgBuilder.setTitle(R.string.change_group_dialog_title);
            dlgBuilder.setMessage(String.format(getResources().getString(R.string.change_group_dialog_message),destUniversity.getName()));
            dlgBuilder.setCancelable(false);
            dlgBuilder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //ToDo: Navigate from current university to destination university (through GMaps)
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
        //Building switching
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
        //Floor switching
        if(curDestination != null && curDestination.getFloorId() != location.getFloorId())
        {
            if(destStair == null) {
                //If it's the first scan with a target in different floor, destStair is null
                //Find nearest waypoint with "stair" attribute and set a route to it.
                List<PointF> stairMarks = floorAdapter.getStairMarks();
                PointF curMark = floorAdapter.getMark(location.getMarkId());
                destStair = curPoint;//Just for initialization
                //Min distance in Cartesian plane
                double min = 0;
                for (PointF p : stairMarks) {
                    double res = Math.sqrt(Math.pow(p.x - curMark.x, 2) + Math.pow(p.y - curMark.y, 2));
                    if (min == 0 || res < min) {
                        min = res;
                        destStair = p;
                    }
                }
                List<Integer> routeList = MapUtils.getShortestDistanceBetweenTwoPoints(curPoint, destStair, floorAdapter.getNodes(), floorAdapter.getEdges());
                routeLayer.setRouteList(routeList);
                mapView.refresh();
                return;
            } else if(curPoint == destStair) {
                //Target has reached dest stair: Invite him to reach correct floor
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
            } else if(floorAdapter.getStairMarks().contains(destStair)) {
                //Target has reached another QRCode in same floor
                List<Integer> routeList = MapUtils.getShortestDistanceBetweenTwoPoints(curPoint, destStair, floorAdapter.getNodes(), floorAdapter.getEdges());
                routeLayer.setRouteList(routeList);
                mapView.refresh();
            }
        }

    }

    public void translateTestX(View v){
        TextView log = (TextView) findViewById(R.id.log_viewer);
        log.append("x + 10");
        mapView.translate(10, 0);
        mapView.refresh();
    }
    public void translateTestY(View v){
        TextView log = (TextView) findViewById(R.id.log_viewer);
        log.append("y + 10");
        mapView.translate(0,10);
        mapView.refresh();
    }
    public void resetMap(View v){
        TextView log = (TextView) findViewById(R.id.log_viewer);
        log.append("Reset Map");
        mapView.mapCenterWithPoint(curPoint.x, curPoint.y);
        mapView.setCurrentZoom(3,curPoint.x, curPoint.y);
        mapView.refresh();
    }
    //QRCode scan callback
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            //ToDo:Handle user cancels scan
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            TextView log = (TextView) findViewById(R.id.log_viewer);
            if (scanResult != null) {
                String re = scanResult.getContents();
                if(re == null) {
                    //Scan aborted or not succesful
                    return;
                }
                Log.d("Scan result", re);
                log.setText("Scanned: " + re + "\n");
                //Data in QRCodes will be stored in json format, since it is human readable (easier generation)
                Location location = ParseTarget(re);
                //ToDo: Floor and building changing

                UpdatePosition(location);
                //mapView.setCurrentZoom(1.5F, curPoint.x, curPoint.y);

                log.append("Current Waypoint: " + floorAdapter.getMarkName(location.getMarkId()) + "\n");
                log.append(">x:" + floorAdapter.getMark(location.getMarkId()).x + " y:" + floorAdapter.getMark(location.getMarkId()).y + "\n");
            } else {
                log.setText("scanResult is NULL!");
            }
        } catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
