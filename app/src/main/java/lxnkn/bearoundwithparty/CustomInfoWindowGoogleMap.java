package lxnkn.bearoundwithparty;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private ArrayList<Integer> current_party = new ArrayList<Integer>();
    private ArrayList<String> partys = new ArrayList<>();
    private ArrayList<String> datePartys = new ArrayList<>();
    private ArrayList<String> timePartys = new ArrayList<>();
    private TextView verbindung_tv;
    private TextView party_tv;
    private TextView date_tv;
    private TextView time_tv;
    private Button next_tv;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        verbindung_tv = view.findViewById(R.id.verbindung);
        party_tv = view.findViewById(R.id.name_party);
        date_tv = view.findViewById(R.id.date_party);
        time_tv = view.findViewById(R.id.time_party);


        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        partys = infoWindowData.getPartys();
        datePartys = infoWindowData.getDatePartys();
        timePartys = infoWindowData.getTimePartys();
        verbindung_tv.setText(infoWindowData.getVerbindung());
        if(partys.size() < 1){
            ((ViewManager)party_tv.getParent()).removeView(party_tv);
            ((ViewManager)date_tv.getParent()).removeView(date_tv);
            ((ViewManager)time_tv.getParent()).removeView(time_tv);
            ((ViewManager)next_tv.getParent()).removeView(next_tv);
        }else {
            if(partys.size() == 1){
                party_tv.setText(partys.get(0));
                date_tv.setText(datePartys.get(0));
                time_tv.setText(timePartys.get(0));
                ((ViewManager)next_tv.getParent()).removeView(next_tv);
            }else{
                party_tv.setText(partys.get(0));
                date_tv.setText(datePartys.get(0));
                time_tv.setText(timePartys.get(0));
                final Integer current_size = current_party.size();
                current_party.add(1);
                next_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        party_tv.setText(partys.get(current_party.get(current_size)));
                        date_tv.setText(datePartys.get(current_party.get(current_size)));
                        time_tv.setText(timePartys.get(current_party.get(current_size)));
                        int temp = current_party.get(current_size);
                        temp = temp +1;
                        if(temp>=partys.size()){
                            current_party.set(current_size,0);
                        }else{
                            current_party.set(current_size,temp);
                        }
                    }
                });
            }
        }

        return view;
    }
}
