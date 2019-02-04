package lxnkn.bearoundwithparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Time;
import java.sql.Date;

@Entity(foreignKeys = @ForeignKey(entity = CStandorte.class,
                                    parentColumns = "id",
                                    childColumns = "standort_uid"))
public class CPartys {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int standort_uid;
    private String name;
    private Date datum;
    private Time time;

    public boolean setAll(String standort_uid, String name,String datum, String time) {
        this.standort_uid = Integer.parseInt(standort_uid);
        this.name = name;
        this.datum = Date.valueOf(datum); //yyyy-mm-dd
        this.time = Time.valueOf(time); //hh:mm:ss
        return true;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getStandort_uid() {
        return standort_uid;
    }
    public void setStandort_uid(int standort_uid) {
        this.standort_uid = standort_uid;
    }
    public void setStandort_uid(String standort_uid) {
        this.standort_uid = Integer.parseInt(standort_uid);
    }

    public Date getDatum() {
        return datum;
    }
    public void setDatum(Date datum) {
        this.datum = datum;
    }
    public void setDatum(String datum) {
        this.datum = Date.valueOf(datum);
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }
    public void setTime(String time) {
        this.time = Time.valueOf(time);
    }

}
