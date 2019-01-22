package lxnkn.bearoundwithparty.util;

import lxnkn.bearoundwithparty.R;

public class constants {
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    //Daten der Orte

    public static final double[] Lat = {51.805387, 51.805345, 51.806975, 51.809221, 51.808125};
    public static final double[] Lng = {10.346454, 10.341174, 10.346690, 10.341263, 10.334972};
    public static final String[] Locations = {"VdSt", "Alemania-Selesia", "Sportverein Barbara", "Wingolf Cantena", "Agricola"};
    public static final int[] Icon = {R.raw.marker, R.raw.panorama, R.raw.marker, R.raw.panorama, R.raw.marker};

}
