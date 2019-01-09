package lxnkn.bearoundwithparty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;

public class MainActivity extends AppCompatActivity {

    private Map map = null;
    private MapFragment mapFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.getMap();
                    map.setCenter(new GeoCoordinate(51.805839, 10.356055, 0.0), Map.Animation.NONE);
                    map.setZoomLevel((map.getMaxZoomLevel()) - 4);

                }
            }
        });
    }
    /*public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //test

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.login:
                startActivity(new Intent(this,LoginActivity.class));
                return true;
            case R.id.map:
                startActivity(new Intent(this,MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/
    @Override
    public void onBackPressed(){
        this.finishAffinity();
    }

}