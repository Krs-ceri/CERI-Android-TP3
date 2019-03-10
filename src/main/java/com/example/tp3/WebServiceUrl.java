package com.example.tp3;



import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceUrl {


    private static final String API_KEY = "56ae8b3ed2c48569121506a644a643c8";

    // https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22
    //private static final String HOST = "samples.openweathermap.org";
    private static final String HOST = "api.openweathermap.org";
    private static final String PATH_1 = "data";
    private static final String PATH_2 = "2.5";
    private static final String PATH_3 = "weather";
    private static final String URL_PARAM1 = "q";
    private static final String URL_PARAM2 = "appid";

    public static URL build(String cityName, String countryName) throws MalformedURLException {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(HOST)
                .appendPath(PATH_1)
                .appendPath(PATH_2)
                .appendPath(PATH_3)
                .appendQueryParameter(URL_PARAM1, cityName + "," + countryName)
                .appendQueryParameter(URL_PARAM2, API_KEY);
        URL url = new URL(builder.build().toString());
        return url;
    }

}
