package com.example.tp3;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp3.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class CityActivity extends AppCompatActivity {

    private static final String TAG = CityActivity.class.getSimpleName();
    private TextView textCityName, textCountry, textTemperature, textHumdity, textWind, textCloudiness, textLastUpdate;
    private ImageView imageWeatherCondition;
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        city = (City) getIntent().getExtras().get("Ville") ;

        textCityName = (TextView) findViewById(R.id.nameCity);
        textCountry = (TextView) findViewById(R.id.country);
        textTemperature = (TextView) findViewById(R.id.editTemperature);
        textHumdity = (TextView) findViewById(R.id.editHumidity);
        textWind = (TextView) findViewById(R.id.editWind);
        textCloudiness = (TextView) findViewById(R.id.editCloudiness);
        textLastUpdate = (TextView) findViewById(R.id.editLastUpdate);

        imageWeatherCondition = (ImageView) findViewById(R.id.imageView);

        updateView();

        final Button but = (Button) findViewById(R.id.button);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CityActivity.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if(activeNetworkInfo != null)
                {
                    Log.d("my weather received","START");
                    getJson();

                }
                else
                {
                    Log.d("my weather received","NO INTERNET CONNECTION");
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getJson()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    // préparation de l'appel au webService
                    URL url = WebServiceUrl.build(city.getName(), city.getCountry());

                    // Appel au webservice
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Initialise l'objet JSONResponseHandler avec la ville récupérer
                    JSONResponseHandler JSresp = new JSONResponseHandler(city);

                    // Récupère les données du web service
                    JSresp.readJsonStream(connection.getInputStream());

                }
                catch (Exception e)
                {
                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void)
            {
                try
                {
                    // Récupère les nouvelles données de la ville
                    String infoCity = city.getName() + ", " +
                            city.getCountry() + ", " +
                            city.getTemperature() + ", " +
                            city.getHumidity() + ", " +
                            city.getWindSpeed() + ", " +
                            city.getWindDirection() + ", " +
                            city.getCloudiness() + ", " +
                            city.getIcon() + ", " +
                            city.getDescription() + ", " +
                            city.getIcon() + ", " +
                            city.getDescription() + ", " +
                            city.getLastUpdate();

                    String icon = "icon_" + city.getIcon();

                    // Met à jour les champs du layout
                    textTemperature.setText(city.getTemperature() + " °C");
                    textHumdity.setText(city.getHumidity() + " %");
                    textWind.setText(city.getWindSpeed() + " km/h (" + city.getWindDirection() + ")");
                    textCloudiness.setText(city.getCloudiness() + " %");
                    textLastUpdate.setText(city.getLastUpdate());

                    // Met à jour l'image du temps
                    Resources res = CityActivity.this.getResources();
                    int resID = res.getIdentifier(icon, "drawable", getPackageName());
                    imageWeatherCondition.setImageResource(resID);


                }
                catch (Exception e)
                {
                    System.out.println("Exception "+ e.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("laVille", city);
        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void updateView()
    {
        textCityName.setText(city.getName());
        textCountry.setText(city.getCountry());
        textTemperature.setText(city.getTemperature()+" °C");
        textHumdity.setText(city.getHumidity()+" %");
        textWind.setText(city.getFullWind());
        textCloudiness.setText(city.getHumidity()+" %");
        textLastUpdate.setText(city.getLastUpdate());

        if (city.getIcon()!=null && !city.getIcon().isEmpty()) {
            Log.d(TAG,"icon="+"icon_" + city.getIcon());
            imageWeatherCondition.setImageDrawable(getResources().getDrawable(getResources()
                    .getIdentifier("@drawable/"+"icon_" + city.getIcon(), null, getPackageName())));
            imageWeatherCondition.setContentDescription(city.getDescription());
        }

    }



}
