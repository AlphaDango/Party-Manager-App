package lxnkn.bearoundwithparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    String benutzername = "Moritz";
    String passwort = "Bearoundwithparty";
    EditText benutzernamenFeld;
    EditText passwortFeld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button anmeldungButton = findViewById(R.id.anmeldungButton);
        benutzernamenFeld = findViewById(R.id.benutzernamenFeld);
        passwortFeld = findViewById(R.id.passwortFeld);
        anmeldungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (benutzernamenFeld.getText().toString().equals(benutzername) && passwortFeld.getText().toString().equals(passwort)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sie wurden erfolgreich angemeldet", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Benutzernamen und Passwort stimmen nicht überein", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

}
