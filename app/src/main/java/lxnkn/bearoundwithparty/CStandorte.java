package lxnkn.bearoundwithparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CStandorte {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titel;
    private Double latitude;
    private Double longitude;

    public boolean setAll(String titel, String latitude,String longitude){
        this.titel=titel;
        this.latitude=Double.parseDouble(latitude);
        this.longitude=Double.parseDouble(longitude);
        return true;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }
    public void setTitel(String titel){
        this.titel = titel;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }


    public Double getLatitude() {
        return latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }
}
