package com.prixesoft.david.weatherextract;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.prixesoft.david.weatherextract.model.Weather;

import org.json.JSONException;

/**
 * Created by david on 23-Mar-17.
 */

public class WeatherInfoActivity extends AppCompatActivity {

    private TextView txtSpeedWind , txtTemperature , txtPresure , txtHumidity , txtWeatherCond , txtCity;


    private String longitude , latitude , location ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        // Retrieving GUI textViews
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtSpeedWind = (TextView) findViewById(R.id.txtSpeedWind);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtPresure = (TextView) findViewById(R.id.txtPresure);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtWeatherCond = (TextView) findViewById(R.id.txtWeatherCond);


        // Retrieving the information from MapsActivity
        final Intent intent = getIntent();
        longitude = intent.getExtras().get("longitude").toString();
        latitude = intent.getExtras().get("latitude").toString();
        location = longitude + "," + latitude ;


        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{location});


    }



    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(latitude,longitude));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }




        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
             //   imgView.setImageBitmap(img);
            }

            txtCity.setText(weather.location.getCity() + "," + weather.location.getCountry());
            txtWeatherCond.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            txtTemperature.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            txtHumidity.setText("" + weather.currentCondition.getHumidity() + "%");
            txtPresure.setText("" + weather.currentCondition.getPressure() + " hPa");
            txtSpeedWind.setText("" + weather.wind.getSpeed() + " mps");
            // .setText("" + weather.wind.getDeg() + "�");

        }







    }
}
