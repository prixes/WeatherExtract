package com.prixesoft.david.weatherextract.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


/**
 * Created by david on 24-Mar-17.
 */

public class WeatherUI extends BaseObservable {


    private String city , weatherCond , temperature , windSpeed , pressure , humidity ;


    private Drawable iconWeather;


    public WeatherUI () {
        city = "empty";
        weatherCond = "empty";
        temperature = "empty";
        windSpeed = "empty";
        pressure = "empty";
        humidity = "empty";

    }

    public Drawable getIconWeather() {
        return iconWeather;
    }

    public void setIconWeather(Drawable iconWeather) {
        this.iconWeather = iconWeather;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherCond() {
        return weatherCond;
    }

    public void setWeatherCond(String weatherCond) {
        this.weatherCond = weatherCond;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String presure) {
        this.pressure = presure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
