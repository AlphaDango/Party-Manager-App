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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static lxnkn.bearoundwithparty.util.constants.MAPVIEW_BUNDLE_KEY;

public class AdminActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

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
    private TextView txtDate;
    private TextView txtTime;
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

            mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                @Override
                public void onInfoWindowLongClick(Marker marker) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                    View view = (AdminActivity.this).getLayoutInflater()
                            .inflate(R.layout.dialog_admin_edit, null);
                    TextView standort_party = view.findViewById(R.id.standort_party);
                    InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                    String verbindung = infoWindowData.getVerbindung();
                    standort_party.setText("Party bei "+verbindung);
                    Button btnDatePicker=view.findViewById(R.id.btn_date);
                    Button btnTimePicker=view.findViewById(R.id.btn_time);
                    txtDate=view.findViewById(R.id.select_date);
                    txtTime=view.findViewById(R.id.select_time);
                    SimpleDateFormat datumsformat = new SimpleDateFormat("dd.MM.yyyy");
                    txtDate.setText(datumsformat.format(kalender.getTime()));
                    txtTime.setText("00:00");
                    btnDatePicker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int mYear,mMonth,mDay;
                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(AdminActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            txtDate.setText(dayOfMonth + "." + ((monthOfYear + 1)<10?"0"+(monthOfYear + 1):(monthOfYear)) + "." + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                            datePickerDialog.show();
                        }
                    });
                    btnTimePicker.setOnClickListener(new View.OnClickListener() {
                        int mHour, mMinute;
                        @Override
                        public void onClick(View v) {
                            // Get Current Time
                            final Calendar c = Calendar.getInstance();
                            mHour = c.get(Calendar.HOUR_OF_DAY);
                            mMinute = c.get(Calendar.MINUTE);

                            // Launch Time Picker Dialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(AdminActivity.this,
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {

                                            txtTime.setText(hourOfDay + ":" + minute);
                                        }
                                    }, mHour, mMinute, true);
                            timePickerDialog.show();
                        }
                    });
                    builder.setView(view);
                    builder.show();


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
                            rs1.beforeFirst();
                            while(rs1.next()){
                                ArrayList<String> party=new ArrayList<>();
                                party.add(id);
                                String party_name = "";
                                String[] party_parts = rs1.getString(3).split("_");
                                if(party_parts.length>1){
                                    party_name = party_parts[0];
                                    for(int j=1;j<party_parts.length;j++){
                                        party_name = party_name+" " +party_parts[j];
                                    }
                                }
                                else{
                                    party_name=rs1.getString(3);
                                }
                                party.add(party_name);

                                String date_party = "";
                                String[] date_parts = rs1.getString(4).split("-");
                                for(int j=date_parts.length-1;j>0;j--){
                                    date_party = date_party+date_parts[j]+".";
                                }
                                date_party = date_party+date_parts[0];
                                party.add(date_party);

                                String time_party = "";
                                String[] time_parts = rs1.getString(5).split(":");
                                time_party = time_parts[0]+":"+time_parts[1];
                                party.add(time_party);

                                partys.add(party);
                            }
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


