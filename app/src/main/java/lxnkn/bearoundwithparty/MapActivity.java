package lxnkn.bearoundwithparty;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static lxnkn.bearoundwithparty.util.constants.Lat;
import static lxnkn.bearoundwithparty.util.constants.Lng;
import static lxnkn.bearoundwithparty.util.constants.Locations;
import static lxnkn.bearoundwithparty.util.constants.MAPVIEW_BUNDLE_KEY;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mMapView;
    private Calendar kalender = Calendar.getInstance();
    private SimpleDateFormat zeitformat = new SimpleDateFormat("HH");
    private int zeit = Integer.parseInt(zeitformat.format(kalender.getTime()));


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


    @Override
    public void onMapReady(GoogleMap map) {
        if(zeit > 17 || zeit < 7)
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night));
        else
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_day));
        for(int i = 0; i < Locations.length; i++){
            map.addMarker(new MarkerOptions().position(new LatLng(Lat[i], Lng[i])).title(Locations[i]));
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()), 2));
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

}
