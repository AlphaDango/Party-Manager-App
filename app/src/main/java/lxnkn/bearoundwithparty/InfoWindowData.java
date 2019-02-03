package lxnkn.bearoundwithparty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InfoWindowData {
    private String verbindung;
    private String id;
    private ArrayList<String> partys = new ArrayList<String>();
    private ArrayList<String> datePartys = new ArrayList<String>();
    private ArrayList<String> timePartys = new ArrayList<String>();

    public ArrayList<String> getTimePartys() {
        return timePartys;
    }
    public void addTimeParty(String timeParty) {
        this.timePartys.add(timeParty);
    }
    public void addTimeParty(String timeParty, int index){
        if(index>this.timePartys.size()){
            this.timePartys.add(timeParty);
        }else{
            ArrayList<String> timePartys2 = new ArrayList<>();
            timePartys2.addAll(timePartys);
            this.timePartys.set(index,timeParty);
            for(int i = index; i<timePartys2.size()-1;i++){
                this.timePartys.set(i+1,timePartys2.get(i));
            }
            this.timePartys.add(timePartys2.get(timePartys2.size()-1));
        }
    }

    public String getId(){return  this.id;};
    public void setId(String id){
        this.id = id;
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

    public void addParty(String party, int index){
        if(index>this.partys.size()){
            this.partys.add(party);
        }else{
            ArrayList<String> partys2 = new ArrayList<>();
            partys2.addAll(partys);
            this.partys.set(index,party);
            for(int i = index; i<partys2.size()-1;i++){
                this.partys.set(i+1,partys2.get(i));
            }
            this.partys.add(partys2.get(partys2.size()-1));
        }
    }

    public ArrayList<String> getDatePartys() {
        return datePartys;
    }
    public void addDateParty(String dateParty) {
        this.datePartys.add(dateParty);
    }

    public int addDateParty(String dateParty, boolean newParty){
        if(this.datePartys.size()<1) {
            this.datePartys.add(dateParty);
            return 0;
        }else{
            Date date_new = new Date();
            SimpleDateFormat sdfToDate = new SimpleDateFormat(
                    "dd.MM.yyyy");
            try {
                date_new = sdfToDate.parse(dateParty);
            } catch (ParseException ex2) {
                ex2.printStackTrace();
            }

            ArrayList<String> dateParty2 = new ArrayList<>();
            dateParty2.addAll(this.datePartys);
            int merke_id =0;
            boolean insert_mittig = false;
            for(int i=0;i<dateParty2.size();i++){
                merke_id = i;
                Date date1 = new Date();
                try {
                    date1 = sdfToDate.parse(dateParty2.get(i));
                } catch (ParseException ex2) {
                    ex2.printStackTrace();
                }
                if(date_new.before(date1)){
                    insert_mittig = true;
                    break;
                }
            }
            if(!insert_mittig){
                this.datePartys.add(dateParty);
                return datePartys.size();
            }else{
                boolean date_before = false;
                for(int i=merke_id;i<dateParty2.size();i++){
                    Date date1 = new Date();
                    try {
                        date1 = sdfToDate.parse(dateParty2.get(i));
                    } catch (ParseException ex2) {
                        ex2.printStackTrace();
                    }
                    if(date1.before(date_new)){
                        date_before =true;
                    }
                    if(date_before && date_new.before(date1)){
                        merke_id = i;
                        break;
                    }
                }

                this.datePartys.set(merke_id,dateParty);
                for(int i = merke_id;i<dateParty2.size()-1;i++){
                    this.datePartys.set(i+1,dateParty2.get(i));
                }
                this.datePartys.add(dateParty2.get(dateParty2.size()-1));
                return merke_id;
            }
        }
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
