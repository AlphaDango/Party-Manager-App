package lxnkn.bearoundwithparty;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static lxnkn.bearoundwithparty.util.constants.Icon;
import static lxnkn.bearoundwithparty.util.constants.Lat;
import static lxnkn.bearoundwithparty.util.constants.Lng;
import static lxnkn.bearoundwithparty.util.constants.Locations;
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
            boolean is_party = false;
            InfoWindowData info = new InfoWindowData();
            for(int j=0;j<partys.size();j++){
                ArrayList<String> party = partys.get(j);
                if(ids.get(i).equals(party.get(0))){
                   info.setVerbindung(title_show);
                   info.setParty(party.get(1));
                   info.setDateParty(party.get(2));
                   info.setTimeParty(party.get(3));
                   is_party=true;
                }
            }
            if(is_party){
                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                mMap.setInfoWindowAdapter(customInfoWindow);
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude.get(i),longitude.get(i))).title(title_show).icon(
                        BitmapDescriptorFactory.fromResource(R.raw.marker)
                )).setTag(info);
            }else{
                info.setVerbindung(title_show);
                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                mMap.setInfoWindowAdapter(customInfoWindow);
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude.get(i),longitude.get(i))).title(title_show).icon(
                        BitmapDescriptorFactory.fromResource(R.raw.marker)
                )).setTag(info);
            }



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
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
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

    class SyncTask extends AsyncTask<String, Integer, String> {
        private String DB_URL;
        private String USER;
        private String PASS;
        private InputStream in_user;
        private InputStream in_pass;
        private InputStream in_url;
        private PreparedStatement preparedStatement;
        private String statement;
        private ResultSet rs;
        private ResultSet rs1;
        private String id;
        String msg;


        @Override
        protected String doInBackground(String... params) {

            //Dateieren auslesen
            try {
                in_user = getAssets().open("username.txt");
                in_pass = getAssets().open("passwort.txt");
                in_url = getAssets().open("db_url.txt");
                USER = new BufferedReader(new InputStreamReader(in_user)).readLine();
                PASS = new BufferedReader(new InputStreamReader(in_pass)).readLine();
                DB_URL = new BufferedReader(new InputStreamReader(in_url)).readLine();
                in_pass.close();
                in_user.close();
                in_url.close();
            } catch (IOException e) {
                Log.e("User_read", "Es kann keine Daten lesen");
                msg = "Es können keine Daten gelesen werden aus Datei";
                return null;
            }

            //Party-Standorte auslesen
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);//Connection Object
                if (conn == null) {
                    msg = "Keine Verbindung zur Datenbank möglich";
                } else {
                    statement = "SELECT * FROM tb_nearby_p_standorte";
                    preparedStatement = conn.prepareStatement(statement);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        latitude.add(Double.parseDouble(rs.getString(3)));
                        longitude.add(Double.parseDouble(rs.getString(4)));
                        title.add(rs.getString(2));
                        ids.add(rs.getString(1));
                        id = rs.getString(1);
                        statement = "SELECT * FROM tb_nearby_partys WHERE DATE(datum) >= DATE(NOW()) and standort_uid = ? order by DATE(datum) asc";
                        preparedStatement = conn.prepareStatement(statement);
                        preparedStatement.setString(1,id);
                        rs1 = preparedStatement.executeQuery();
                        rs1.last();
                        if(rs1.getRow()>0){
                            rs1.first();
                            ArrayList<String> party=new ArrayList<>();
                            party.add(id);
                            party.add(rs1.getString(3));
                            party.add(rs1.getString(4));
                            party.add(rs1.getString(5));
                            partys.add(party);


                        }
                    }
                    msg = "Daten von Datenbank geholt";

                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = "Fehler beim Auslesen aus der Datenbank";
            }
            return msg;
        }

        protected void onPostExecute(String msg) {
            Log.d("Datei", msg);
        }
    }

}
