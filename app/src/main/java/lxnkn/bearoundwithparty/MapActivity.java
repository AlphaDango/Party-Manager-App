package lxnkn.bearoundwithparty;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static lxnkn.bearoundwithparty.util.constants.MAPVIEW_BUNDLE_KEY;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mMapView;
    private Calendar kalender = Calendar.getInstance();
    private SimpleDateFormat zeitformat = new SimpleDateFormat("HH");
    private int zeit = Integer.parseInt(zeitformat.format(kalender.getTime()));
    private GoogleMap mMap;
    private ArrayList<Double> latitude =new ArrayList<>();
    private  ArrayList<Double> longitude = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<ArrayList<String>> partys = new ArrayList<ArrayList<String>>();
    private TextView verbindung_tv;
    private TextView party_tv;
    private TextView date_tv;
    private TextView time_tv;
    private TextView label_party;
    private TextView label_date;
    private TextView label_time;
    private View divider;
    private ViewGroup infoWindow;
    private ArrayList<String> db_partys = new ArrayList<>();
    private ArrayList<String> db_datePartys = new ArrayList<>();
    private ArrayList<String> db_timePartys = new ArrayList<>();
    private boolean remove_views =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    private Location getLocation(){

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        return location;
    }
    public void placeMarkers(){
        SyncTask syncTask = new SyncTask();
        Object result;
        try {
            result = syncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < longitude.size(); i++) {


            String title_show = "";
            String[] title_parts = title.get(i).split("_");
            if(title_parts.length>1){
                for(int j=0;j<title_parts.length;j++){
                    title_show = title_show+title_parts[j];
                }
            }
            else{
                title_show=title.get(i);
            }


            InfoWindowData info  = new InfoWindowData();
            info.setVerbindung(title_show);
            for(int j=0;j<partys.size();j++){
                ArrayList<String> party = partys.get(j);
                if(ids.get(i).equals(party.get(0))){
                    info.setId(party.get(0));
                    info.addParty(party.get(1));
                    info.addDateParty(party.get(2));
                    info.addTimeParty(party.get(3));
                }
            }


            infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.map_custom_infowindow,null);
            verbindung_tv = infoWindow.findViewById(R.id.verbindung);
            party_tv = infoWindow.findViewById(R.id.name_party);
            date_tv = infoWindow.findViewById(R.id.date_party);
            time_tv = infoWindow.findViewById(R.id.time_party);
            label_party = infoWindow.findViewById(R.id.label_party);
            label_date = infoWindow.findViewById(R.id.label_date);
            label_time = infoWindow.findViewById(R.id.label_time);
            divider = infoWindow.findViewById(R.id.divider);

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                    db_partys = infoWindowData.getPartys();
                    db_datePartys = infoWindowData.getDatePartys();
                    db_timePartys = infoWindowData.getTimePartys();
                    verbindung_tv.setText(infoWindowData.getVerbindung());

                    if(db_partys.size() < 1){
                        if(!remove_views) {
                            infoWindow.removeView(party_tv);
                            infoWindow.removeView(date_tv);
                            infoWindow.removeView(time_tv);
                            infoWindow.removeView(label_party);
                            infoWindow.removeView(label_date);
                            infoWindow.removeView(label_time);
                            infoWindow.removeView(divider);
                            remove_views = true;
                        }
                    }else {
                        if (remove_views){
                            infoWindow.addView(party_tv);
                            infoWindow.addView(date_tv);
                            infoWindow.addView(time_tv);
                            infoWindow.addView(label_party);
                            infoWindow.addView(label_date);
                            infoWindow.addView(label_time);
                            infoWindow.addView(divider);
                            remove_views =false;
                        }
                        party_tv.setText(db_partys.get(0));
                        date_tv.setText(db_datePartys.get(0));
                        time_tv.setText(db_timePartys.get(0));


                    }
                    return infoWindow;
                }
            });


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    marker.hideInfoWindow();
                    InfoWindowData info_neu = new InfoWindowData();
                    InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                    db_partys = infoWindowData.getPartys();
                    if(db_partys.size()>1){
                        info_neu = infoWindowData;
                        info_neu.refresh();
                        marker.setTag(info_neu);
                    }

                    marker.showInfoWindow();

                }
            });

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude.get(i), longitude.get(i))).title(title_show).icon(
                    BitmapDescriptorFactory.fromResource(R.raw.marker)
            )).setTag(info);
        }
    }




    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if(zeit > 17 || zeit < 7)
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night));
        else
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_day));
        placeMarkers();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()), (float)14.5));

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }



    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed(){
        this.finishAffinity();
    }

    class SyncTask extends AsyncTask<String, Integer, String> {
        String msg;


        @Override
        protected String doInBackground(String... params) {

            List<CStandorte> standorte = DatabaseClient.getInstance(getApplicationContext(),getComponentName().getClassName()).
                    getDatabase().standorteDao().getAll();
            for(int i =0;i<standorte.size();i++){
                ids.add(String.valueOf(standorte.get(i).getId()));
                title.add(standorte.get(i).getTitel());
                latitude.add(standorte.get(i).getLatitude());
                longitude.add(standorte.get(i).getLongitude());
                String id = String.valueOf(standorte.get(i).getId());
                List<CPartys> party_list = DatabaseClient.getInstance(getApplicationContext(),getComponentName().getClassName()).
                        getDatabase().partyDao().getAll(Integer.parseInt(id));
                if(party_list.size()>0){
                    for(int j = 0;j<party_list.size();j++) {
                        ArrayList<String> party = new ArrayList<>();
                        party.add(id);
                        String party_name = "";
                        String[] party_parts = party_list.get(j).getName().split("_");
                        if(party_parts.length>1){
                            party_name = party_parts[0];
                            for(int l=1;l<party_parts.length;l++){
                                party_name = party_name+" " +party_parts[l];
                            }
                        }
                        else{
                            party_name=party_list.get(j).getName();
                        }
                        party.add(party_name);
                        String date_party = "";
                        String[] date_parts = party_list.get(j).getDatum().toString().split("-");
                        for(int l=date_parts.length-1;l>0;l--){
                            date_party = date_party+date_parts[l]+".";
                        }
                        date_party = date_party+date_parts[0];
                        party.add(date_party);

                        String time_party = "";
                        String[] time_parts = party_list.get(j).getTime().toString().split(":");
                        time_party = time_parts[0]+":"+time_parts[1];
                        party.add(time_party);

                        partys.add(party);

                    }

                }


            }
            msg="Daten erfolgreich gelesen";

            return msg;
        }

        protected void onPostExecute(String msg) {
            Log.d("Datei", msg);
        }
    }


}
