package com.example.tp3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String TAG = WeatherDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "weather.db";

    public static final String TABLE_NAME = "weather";

    public static final String _ID = "_id";
    public static final String COLUMN_CITY_NAME = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_WIND_SPEED = "windSpeed";
    public static final String COLUMN_WIND_DIRECTION = "windDirection";
    public static final String COLUMN_CLOUDINESS = "cloudiness";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LAST_UPDATE = "lastupdate";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +

                COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                COLUMN_COUNTRY + " TEXT NOT NULL, " +
                COLUMN_TEMPERATURE + " INTEGER, " +
                COLUMN_HUMIDITY+ " TEXT, " +
                COLUMN_WIND_SPEED+ " TEXT, " +
                COLUMN_WIND_DIRECTION+ " TEXT, " +
                COLUMN_CLOUDINESS+ " TEXT, " +
                COLUMN_DESCRIPTION+ " TEXT, " +
                COLUMN_ICON+ " TEXT, " +
                COLUMN_LAST_UPDATE+ " TEXT, " +

                // To assure the application have just one weather entry per
                // city name and country, it's created a UNIQUE
                " UNIQUE (" + COLUMN_CITY_NAME + ", " +
                COLUMN_COUNTRY + ") ON CONFLICT ROLLBACK);";

        db.execSQL(SQL_CREATE_BOOK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Adds a new city
     * @return  true if the city was added to the table ; false otherwise (case when the pair (city name, country) is
     * already in the data base
     */
    public boolean addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY_NAME, city.getName());
        values.put(COLUMN_COUNTRY, city.getCountry());
        values.put(COLUMN_TEMPERATURE, city.getTemperature());
        values.put(COLUMN_HUMIDITY, city.getHumidity());
        values.put(COLUMN_WIND_SPEED, city.getWindSpeed());
        values.put(COLUMN_WIND_DIRECTION, city.getWindDirection());
        values.put(COLUMN_CLOUDINESS, city.getCloudiness());
        values.put(COLUMN_ICON, city.getIcon());
        values.put(COLUMN_DESCRIPTION, city.getDescription());
        values.put(COLUMN_LAST_UPDATE, city.getLastUpdate());

        Log.d(TAG, "adding: "+city.getName()+" with id="+city.getId());

        // Inserting Row
        // The unique used for creating table ensures to have only one copy of each pair (city name, country)
        // If rowID = -1, an error occured
        long rowID = db.insertWithOnConflict(TABLE_NAME, null, values, CONFLICT_IGNORE);
        db.close(); // Closing database connection

        return (rowID != -1);
    }

    /**
     * Updates the information of a city inside the data base
     * @return the number of updated rows
     */
    public int updateCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY_NAME, city.getName());
        values.put(COLUMN_COUNTRY, city.getCountry());
        values.put(COLUMN_TEMPERATURE, city.getTemperature());
        values.put(COLUMN_HUMIDITY, city.getHumidity());
        values.put(COLUMN_WIND_SPEED, city.getWindSpeed());
        values.put(COLUMN_WIND_DIRECTION, city.getWindDirection());
        values.put(COLUMN_CLOUDINESS, city.getCloudiness());
        values.put(COLUMN_ICON, city.getIcon());
        values.put(COLUMN_DESCRIPTION, city.getDescription());
        values.put(COLUMN_LAST_UPDATE, city.getLastUpdate());

        // updating row
        return db.updateWithOnConflict(TABLE_NAME, values, _ID + " = ?",
                new String[] { String.valueOf(city.getId()) }, CONFLICT_IGNORE);
    }

    /**
     * Returns a cursor on all the cities of the data base
     */
    public Cursor fetchAllCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                null, null, null, null, COLUMN_CITY_NAME+" ASC", null);

        Log.d(TAG, "call fetchAllCities()");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Returns a list on all the cities of the data base
     */
    public ArrayList<City> getAllCities() {
        ArrayList<City> res = new ArrayList<>();

        Cursor c = fetchAllCities();

        c.moveToFirst();
        res.add(cursorToCity(c));
        while(c.moveToNext())
        {
            if(c != null)
            {
                res.add(cursorToCity(c));
            }
            else {
                break;
            }
        }

        return res;
    }

    public void deleteCity(Cursor cursor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, _ID + " = ?",
                new String[]{cursor.getString(cursor.getColumnIndex(_ID))});
        db.close();
    }

    public void deleteCity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, _ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void populate() {
        Log.d(TAG, "call populate()");
        addCity(new City("Avignon","France"));
        addCity(new City("Paris","France"));
        addCity(new City("Rennes","France"));
        addCity(new City("Montreal","Canada"));
        addCity(new City("Fortaleza","Brazil"));
        addCity(new City("Papeete","French Polynesia"));
        addCity(new City("Sydney","Australia"));
        addCity(new City("Seoul","South Korea"));
        addCity(new City("Bamako","Mali"));

        SQLiteDatabase db = this.getReadableDatabase();
        long numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+TABLE_NAME, null);
        Log.d(TAG, "nb of rows="+numRows);
        db.close();
    }

    public City cursorToCity(Cursor cursor) {
        City city = new City(cursor.getLong(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TEMPERATURE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMIDITY)),
                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_SPEED)),
                cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIRECTION)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CLOUDINESS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ICON)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_UPDATE))
        );
        return city;
    }

    public City getCity(Cursor cursor) {
        City city = null;

        int newId = cursor.getColumnIndexOrThrow(_ID);
        int newName = cursor.getColumnIndexOrThrow(COLUMN_CITY_NAME);
        int newCountry = cursor.getColumnIndexOrThrow(COLUMN_COUNTRY);
        int newTemperature = cursor.getColumnIndexOrThrow(COLUMN_TEMPERATURE);
        int newHumidity = cursor.getColumnIndexOrThrow(COLUMN_HUMIDITY);
        int newWindSpeed = cursor.getColumnIndexOrThrow(COLUMN_WIND_SPEED);
        int newWindDirection = cursor.getColumnIndexOrThrow(COLUMN_WIND_DIRECTION);
        int newCloudiness = cursor.getColumnIndexOrThrow(COLUMN_CLOUDINESS);
        int newIcon = cursor.getColumnIndexOrThrow(COLUMN_ICON);
        int newDescription = cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION);
        int newLastUpdate = cursor.getColumnIndexOrThrow(COLUMN_LAST_UPDATE);

        long id = cursor.getLong(newId);
        String name = cursor.getString(newName);
        String country = cursor.getString(newCountry);
        String temperature = cursor.getString(newTemperature);
        String humidity = cursor.getString(newHumidity);
        String windSpeed = cursor.getString(newWindSpeed);
        String windDirection = cursor.getString(newWindDirection);
        String cloudiness = cursor.getString(newCloudiness);
        String icon = cursor.getString(newIcon);
        String description = cursor.getString(newDescription);
        String lastUpdate = cursor.getString(newLastUpdate);

        city = new City(id, name, country, temperature, humidity, windSpeed, windDirection, cloudiness, icon, description, lastUpdate);

        return city;
    }

    public void clearDataBase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_NAME;
        db.execSQL(clearDBQuery);

    }

}
