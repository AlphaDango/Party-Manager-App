package lxnkn.bearoundwithparty;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

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

        TextView verbindung_tv = view.findViewById(R.id.verbindung);
        TextView party_tv = view.findViewById(R.id.name_party);
        TextView date_tv = view.findViewById(R.id.date_party);
        TextView time_tv = view.findViewById(R.id.time_party);


        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        verbindung_tv.setText(infoWindowData.getVerbindung());
        if(infoWindowData.getParty() == null){
            ((ViewManager)party_tv.getParent()).removeView(party_tv);
            ((ViewManager)date_tv.getParent()).removeView(date_tv);
            ((ViewManager)time_tv.getParent()).removeView(time_tv);
        }else {
            party_tv.setText(infoWindowData.getParty());
            date_tv.setText(infoWindowData.getDateParty());
            time_tv.setText(infoWindowData.getTimeParty());
        }

        return view;
    }
}
