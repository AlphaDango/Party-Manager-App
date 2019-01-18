package lxnkn.bearoundwithparty;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity {
    EditText benutzernamenFeld;
    EditText passwortFeld;
    Button anmeldungButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anmeldungButton = findViewById(R.id.anmeldungButton);
        benutzernamenFeld = findViewById(R.id.benutzernamenFeld);
        passwortFeld = findViewById(R.id.passwortFeld);
        anmeldungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = benutzernamenFeld.getText().toString();
                String pass = passwortFeld.getText().toString();
                anmeldungButton.setEnabled(false);
                benutzernamenFeld.setEnabled(false);
                passwortFeld.setEnabled(false);
                SyncTask1 syncTask = new SyncTask1();
                syncTask.execute(user, pass);

            }
        });
    }

    private class SyncTask1 extends AsyncTask<String, Integer, String> {
        private String DB_URL;
        private String USER;
        private String PASS;
        private InputStream in_user;
        private InputStream in_pass;
        private InputStream in_url;
        private PreparedStatement preparedStatement;
        private String statement;
        private String username;
        private String password;
        private ResultSet rs;
        String msg;



        @Override
        protected String doInBackground(String... params) {
            username = params[0].trim();
            password = params[1].trim();

            //Dateieren auslesen
            try {
                in_user = getAssets().open("username.txt");
                in_pass = getAssets().open("passwort.txt");
                in_url  = getAssets().open("db_url.txt");
                USER   = new BufferedReader(new InputStreamReader(in_user)).readLine();
                PASS   = new BufferedReader(new InputStreamReader(in_pass)).readLine();
                DB_URL = new BufferedReader(new InputStreamReader(in_url)).readLine();
                in_pass.close();
                in_user.close();
                in_url.close();
            } catch (IOException e) {
                Log.e("User_read","Es kann keine Daten lesen");
                msg="Es können keine Daten gelesen werden aus Datei";
                return null;
            }

            //Überprüfen der Login-Daten
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);//Connection Object
                if (conn == null) {
                    msg = "Keine Verbindung zur Datenbank möglich";
                } else {
                    // Change below query according to your own database.
                    statement = "SELECT * FROM tb_nearby_admin WHERE username = ? AND password = ?";
                    // statement = "SELECT * FROM tb_nearby_admin";
                    preparedStatement = conn.prepareStatement(statement);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    rs = preparedStatement.executeQuery();
                    if (rs.next()) {
                        msg = "Nutzer mit Passwort existiert";
                    } else {
                        msg = "Nutzer mit Passwort existiert nicht";
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }

        protected void onPostExecute(String msg) {
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
            benutzernamenFeld.setEnabled(true);
            passwortFeld.setEnabled(true);
            anmeldungButton.setEnabled(true);
        }


    }
}