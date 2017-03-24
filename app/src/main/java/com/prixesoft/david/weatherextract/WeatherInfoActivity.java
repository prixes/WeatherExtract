package com.prixesoft.david.weatherextract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;
import com.prixesoft.david.weatherextract.model.Weather;

import org.json.JSONException;

/**
 * Created by david on 23-Mar-17.
 */

public class WeatherInfoActivity extends AppCompatActivity {

    // all the general components that we will use GUI-elements and variables

    private TextView txtSpeedWind , txtTemperature , txtPressure, txtHumidity , txtWeatherCond , txtCity;

    private String longitude , latitude , location ;

    private ImageView imgWeather;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        imgWeather = (ImageView) findViewById(R.id.icon_weather);


        // Setting the custom action bar with back button
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        ImageButton btnBack = (ImageButton)  getSupportActionBar().getCustomView().findViewById(R.id.btnBack);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                backButtonAction();

            }
        });

        // Retrieving GUI textViews
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtSpeedWind = (TextView) findViewById(R.id.txtSpeedWind);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtPressure = (TextView) findViewById(R.id.txtPresure);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtWeatherCond = (TextView) findViewById(R.id.txtWeatherCond);


        // Retrieving the information from MapsActivity
        final Intent intent = getIntent();
        longitude = intent.getExtras().get(getString(R.string.longitude)).toString();
        latitude = intent.getExtras().get(getString(R.string.latitude)).toString();
        location = longitude + "," + latitude ;


        dialog = new ProgressDialog(this);

        // Start requesting and parsing information from server
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{location});


    }


    // AsyncTask as requested
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        // Before we start the job let's inform the user that we are doing our job :)
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.inform_loading));
            dialog.show();
        }

        // background actions
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(latitude,longitude));

            try {
                //Try extract the weather info
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


            // Setting the small resolution icons that openweathermap provides ( yes it's ugly )
            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgWeather.setImageBitmap(bitmap);
            }

            txtCity.setText(weather.location.getCity() + "," + weather.location.getCountry());
            txtWeatherCond.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            txtTemperature.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            txtHumidity.setText("" + weather.currentCondition.getHumidity() + " %");
            txtPressure.setText("" + weather.currentCondition.getPressure() + " hPa");
            txtSpeedWind.setText("" + weather.wind.getSpeed() + " m/s");


            // job finished remove the dialog
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }


    }

    // Back button action
    // Probably it's enough to just call hardware's back button press
    void backButtonAction(){
        Intent intentBack = new Intent(this,MapsActivity.class);
        startActivity(intentBack);
    }
}
