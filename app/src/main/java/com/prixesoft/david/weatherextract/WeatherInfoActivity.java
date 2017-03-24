package com.prixesoft.david.weatherextract;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.prixesoft.david.weatherextract.model.Weather;
import com.prixesoft.david.weatherextract.model.WeatherUI;
import com.prixesoft.david.weatherextract.databinding.WeatherInfoBinding;

import org.json.JSONException;

/**
 * Created by david on 23-Mar-17.
 */

public class WeatherInfoActivity extends AppCompatActivity {

    // all the general components that we will use GUI-elements and variables

    private String longitude , latitude , location ;

    private ProgressDialog dialog;

    WeatherUI weatherUI;
    WeatherInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        binding = DataBindingUtil.setContentView(this, R.layout.weather_info);
        weatherUI = new WeatherUI();


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
            if(data == null) {
                displayMsgBox("Failed to receive information from the weather server. Please try again!","Bad response from server!");

                return new Weather(); /// FIX THIS
            }
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


            // Setting the small resolution icons that openweathermap provides
            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                weatherUI.setIconWeather(drawable);
            }

            // Setting the information to the model and biding it
            weatherUI.setCity(" " + weather.location.getCity() + " , " + weather.location.getCountry());
            weatherUI.setWeatherCond(weather.currentCondition.getCondition());
            weatherUI.setTemperature(Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            weatherUI.setHumidity(weather.currentCondition.getHumidity() + " %");
            weatherUI.setPressure(weather.currentCondition.getPressure() + " hPa");
            weatherUI.setWindSpeed(weather.wind.getSpeed() + " m/s");
            binding.setWeatherUi(weatherUI);


            // job finished remove the dialog
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }


    }


    //Error or Success messages
    public void displayMsgBox(String msg,String title) {
        final AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setMessage(msg);
        msgBox.setTitle(title);
        msgBox.setPositiveButton(R.string.ok, null);
        msgBox.setCancelable(true);

        msgBox.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        onBackPressed();
                        msgBox.create().hide();
                    }
                });
        msgBox.create().show();

    }

    // Back button action
    // Probably it's enough to just call hardware's back button press
    void backButtonAction(){
        Intent intentBack = new Intent(this,MapsActivity.class);
        startActivity(intentBack);
    }
}
