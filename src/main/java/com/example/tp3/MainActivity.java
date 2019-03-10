package com.example.tp3;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import com.example.tp3.R;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    public int ADD_CITY = 0;
    public int UPDATE_WEATHER = 0 ;
    public SwipeRefreshLayout swipe;
    public SimpleCursorAdapter simpleCursorAdapter = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipe.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright)
                , getResources().getColor(android.R.color.holo_green_light)
                , getResources().getColor(android.R.color.holo_orange_light)
                , getResources().getColor(android.R.color.holo_red_light));

        swipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        updateWeather();
                    }
                }
        );



        final WeatherDbHelper dbHelper = new WeatherDbHelper(this) ;
        //dbHelper.clearDataBase();
        //dbHelper.populate();

        simpleCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHelper.fetchAllCities(),
                new String[] {WeatherDbHelper.COLUMN_CITY_NAME, WeatherDbHelper.COLUMN_COUNTRY},
                new int [] {android.R.id.text1, android.R.id.text2}) ;

        ListView list = (ListView) findViewById(R.id.listView) ;
        list.setAdapter(simpleCursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor selectCursor = (Cursor) parent.getItemAtPosition(position) ;
                Intent intent = new Intent(MainActivity.this, CityActivity.class) ;
                City selectedCity = dbHelper.cursorToCity(selectCursor) ;
                intent.putExtra("Ville", selectedCity) ;
                startActivityForResult(intent, UPDATE_WEATHER);
            }
        });

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.ajouter) ;

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewCityActivity.class) ;
                startActivityForResult(intent, ADD_CITY);
            }
        });
    }

    public void updateCity()
    {
        WeatherDbHelper wDB = new WeatherDbHelper(this);
        ArrayList<City> listeVille = wDB.getAllCities();

        for(City ville : listeVille)
        {

        }
    }


    public void updateWeather()
    {
        final WeatherDbHelper wDB = new WeatherDbHelper(this);
        final ArrayList<City> listeVille = wDB.getAllCities();

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
                    for(City city : listeVille)
                    {
                        // préparation de l'appel au webService
                        URL url = WebServiceUrl.build(city.getName(), city.getCountry());

                        // Appel au webservice
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // Initialise l'objet JSONResponseHandler avec la ville récupérer
                        JSONResponseHandler JSresp = new JSONResponseHandler(city);

                        // Récupère les données du web service
                        JSresp.readJsonStream(connection.getInputStream());


                        wDB.updateCity(city);
                    }

                } catch (Exception e)
                {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void)
            {
                swipe.setRefreshing(false);
                simpleCursorAdapter.swapCursor(wDB.fetchAllCities());
                simpleCursorAdapter.notifyDataSetChanged();
            }


        }.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Appeler quand on ajoute une ville
        if (requestCode == ADD_CITY)
        {
            if (resultCode == RESULT_CANCELED)
            {
                WeatherDbHelper wDB = new WeatherDbHelper(this);
                simpleCursorAdapter.swapCursor(wDB.fetchAllCities());
                simpleCursorAdapter.notifyDataSetChanged();
            }
        }

        // Appeler quand on met à jour la météo
        if(requestCode == UPDATE_WEATHER)
        {
            Log.d("DEBUG DEBUG", "HERE");
            if (resultCode == RESULT_OK)
            {
                City city = (City)data.getExtras().get("laVille");
                WeatherDbHelper wDB = new WeatherDbHelper(MainActivity.this);
                wDB.updateCity(city);
                simpleCursorAdapter.swapCursor(wDB.fetchAllCities());
                simpleCursorAdapter.notifyDataSetChanged();
            }
        }
    }

}