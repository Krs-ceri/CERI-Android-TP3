package com.example.tp3;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {

    public static final String TAG = City.class.getSimpleName();

    private long id;


    private String name;
    private String country;
    private String temperature; // °C
    private String humidity; // percentage
    private String windSpeed; // km/h
    private String windDirection; // N,S,E,O
    private String cloudiness; // percentage
    private String icon; // icon name (ex: 09d)
    private String description; // description of the current weather condition (ex: light intensity drizzle)
    private String lastUpdate; // Last time when data was updated

    public City() {}

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public City(long id, String name, String country, String temperature, String humidity, String windSpeed, String windDirection, String cloudiness, String icon, String description, String lastUpdate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.cloudiness = cloudiness;
        this.icon = icon;
        this.description = description;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getFullName() { return name+"("+country+")"; }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() { return windSpeed;}

    public String getWindDirection() {
        return windDirection;
    }

    public String getFullWind() {
        return windSpeed+" km/h ("+windDirection+")";
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return icon;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    @Override
    public String toString() {
        return this.name+"("+this.country+"): "+this.temperature+"°C";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(temperature);
        dest.writeString(humidity);
        dest.writeString(windSpeed);
        dest.writeString(windDirection);
        dest.writeString(cloudiness);
        dest.writeString(icon);
        dest.writeString(description);
        dest.writeString(lastUpdate);
    }

    public static final Creator<City> CREATOR = new Creator<City>()
    {
        @Override
        public City createFromParcel(Parcel source)
        {
            return new City(source);
        }

        @Override
        public City[] newArray(int size)
        {
            return new City[size];
        }
    };

    public City(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.country = in.readString();
        this.temperature = in.readString();
        this.humidity = in.readString();
        this.windSpeed = in.readString();
        this.windDirection = in.readString();
        this.cloudiness = in.readString();
        this.icon = in.readString();
        this.description = in.readString();
        this.lastUpdate = in.readString();
    }
}
