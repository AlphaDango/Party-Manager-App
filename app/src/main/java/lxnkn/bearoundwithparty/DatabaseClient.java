package lxnkn.bearoundwithparty;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private static Database_bearound database;

    private static String DB_URL;
    private static String USER;
    private static String PASS;
    private static InputStream in_user;
    private static InputStream in_pass;
    private static InputStream in_url;
    private static PreparedStatement preparedStatement;
    private static String statement;
    private static ResultSet rs;
    private static ResultSet rs1;
    private static String msg;

    private DatabaseClient(Context mCtx){
        this.mCtx = mCtx;

        database = Room.databaseBuilder(mCtx,Database_bearound.class,"Database_bearound").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx, String activityName) {

        if (mInstance == null || activityName.equals(MainActivity.class.getName())) {

            database = Room.databaseBuilder(mCtx,Database_bearound.class,"Database_bearound").build();
            database.partyDao().delete();
            database.standorteDao().delete();


            try {
                in_user = mCtx.getAssets().open("username.txt");
                in_pass = mCtx.getAssets().open("passwort.txt");
                in_url = mCtx.getAssets().open("db_url.txt");
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
                    while(rs.next()){
                        CStandorte standort = new CStandorte();
                        standort.setId(Integer.parseInt(rs.getString(1)));
                        standort.setTitel(rs.getString(2));
                        standort.setLatitude(rs.getString(3));
                        standort.setLongitude(rs.getString(4));
                        database.standorteDao().insert(standort);
                    }
                    statement = "SELECT * FROM tb_nearby_partys WHERE DATE(datum) >= DATE(NOW()) order by DATE(datum) asc";
                    preparedStatement = conn.prepareStatement(statement);
                    rs1 = preparedStatement.executeQuery();
                    rs1.last();
                    if(rs1.getRow()>0){
                        rs1.beforeFirst();
                        while(rs1.next()){
                            CPartys party = new CPartys();
                            party.setStandort_uid(rs1.getString(2));
                            party.setName(rs1.getString(3));
                            party.setDatum(rs1.getString(4));
                            party.setTime(rs1.getString(5));
                            database.partyDao().insert(party);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = "Fehler beim Auslesen aus der Datenbank";
            }

            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public Database_bearound getDatabase(){
        return database;
    }

}
