package com.example.tp3;



import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.tp3.City;

public class NewCityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);

        final EditText textName = (EditText) findViewById(R.id.editNewName);
        final EditText textCountry = (EditText) findViewById(R.id.editNewCountry);

        final Button but = findViewById(R.id.button);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherDbHelper wDB = new WeatherDbHelper(NewCityActivity.this);
                String ville = textName.getText().toString();
                String pays = textCountry.getText().toString();

                if(!ville.isEmpty() && !pays.isEmpty())
                {
                    City c = new City(ville, pays);
                    wDB.addCity(c);

                    Toast.makeText(NewCityActivity.this, "Ville ajouté",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(NewCityActivity.this, "Erreur",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
