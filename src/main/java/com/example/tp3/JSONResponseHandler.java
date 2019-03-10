package com.example.tp3;


import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import com.example.tp3.City;

/**
 * Process the response to a GET request to the Web service
 * api.openweathermap.org
 * Responses must be provided in JSON.
 *
 *
 *
 */


public class JSONResponseHandler {

    private static final String TAG = JSONResponseHandler.class.getSimpleName();

    private City city;


    public JSONResponseHandler(City city) {
        this.city = city;
    }

    /**
     * @param response done by the Web service
     * @return A City with attributes filled with the collected information if response was
     * successfully analyzed; a void list otherwise
     */
    public void readJsonStream(InputStream response) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
        try {
            readCity(reader);
        } finally {
            reader.close();
        }
    }

    public void readCity(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("weather")) {
                readWeather(reader);
            } else if (name.equals("main")) {
                readMain(reader);
            } else if (name.equals("wind")) {
                readWind(reader);
            } else if (name.equals("clouds")) {
                readClouds(reader);
            } else if (name.equals("dt")) {
                city.setLastUpdate(unixTime2date(reader.nextLong()));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }


    private void readWeather(JsonReader reader) throws IOException {
        reader.beginArray();
        int nb = 0; // only consider the first element of the array
        while (reader.hasNext() && nb==0) {
            reader.beginObject();
            while(reader.hasNext())
            {
                String name = reader.nextName();
                if(name.equals("icon"))
                {
                    city.setIcon(reader.nextString());
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            nb++;
        }
        reader.endArray();
    }

    private void readMain(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("temp")) {
                city.setTemperature(kelvin2celsius(reader.nextDouble()));
            } else if (name.equals("humidity")) {
                city.setHumidity(reader.nextString());

            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    private void readWind(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("speed")) {
                city.setWindSpeed(reader.nextString());
            } else if (name.equals("deg")) {
                city.setWindDirection(deg2compass(reader.nextInt()));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    private void readClouds(JsonReader reader) throws IOException {
        reader.beginObject();
        while(reader.hasNext())
        {
            String name = reader.nextName();
            city.setCloudiness(reader.nextString());
        }
        reader.endObject();
    }

    private String unixTime2date(long time) {
        Date date = new Date(time*1000);
        return date.toString();
    }

    private String kelvin2celsius(double t) {
        Log.d(TAG, "read temperature="+t);
        return String.valueOf((int)(t-273.15));
    }

    private String farenheit2celsius(double t) {
        return String.valueOf((int) ((5.0/9.0) * (t-32)));
    }

    private String mph2kmh(double speed) {
        return String.valueOf((int) (speed*1.609344));
    }

    private String deg2compass(int deg) {
        String[] arrComp = {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};
        int val = (int)((((float)deg)/22.5)+.5);
        return arrComp[val % 16];
    }
}
