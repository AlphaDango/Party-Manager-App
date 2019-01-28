package lxnkn.bearoundwithparty;

import java.util.ArrayList;

public class InfoWindowData {
    private String verbindung;
    private ArrayList<String> partys = new ArrayList<String>();
    private ArrayList<String> datePartys = new ArrayList<String>();
    private ArrayList<String> timePartys = new ArrayList<String>();

    public ArrayList<String> getTimePartys() {
        return timePartys;
    }
    public void addTimeParty(String timeParty) {
        this.timePartys.add(timeParty);
    }

    public String getVerbindung() {
        return verbindung;
    }
    public void setVerbindung(String verbindung) {
        this.verbindung = verbindung;
    }

    public ArrayList<String> getPartys() {
        return partys;
    }
    public void addParty(String party) {
        this.partys.add(party);
    }

    public ArrayList<String> getDatePartys() {
        return datePartys;
    }
    public void addDateParty(String dateParty) {
        this.datePartys.add(dateParty);
    }

    public void refresh(){
        if(partys.size()>1) {
            String temp_p = partys.get(0);
            String temp_d = datePartys.get(0);
            String temp_t = timePartys.get(0);
            for (int i = 0; i < partys.size()-1; i++) {
                partys.set(i,partys.get(i+1));
                datePartys.set(i,datePartys.get(i+1));
                timePartys.set(i,timePartys.get(i+1));
            }
            partys.set(partys.size()-1,temp_p);
            datePartys.set(partys.size()-1,temp_d);
            timePartys.set(partys.size()-1,temp_t);
        }
    }
}
