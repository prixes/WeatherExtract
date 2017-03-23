package com.prixesoft.david.weatherextract;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prixesoft.david.weatherextract.model.Weather;

import org.json.JSONException;

/**
 * Created by david on 23-Mar-17.
 */

public class WeatherInfoActivity extends AppCompatActivity {

    private TextView txtSpeedWind , txtTemperature , txtPresure , txtHumidity , txtWeatherCond , txtCity;

    private RelativeLayout generalLayout;

    private String longitude , latitude , location ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        generalLayout =(RelativeLayout) findViewById(R.id.layout_weather_info);

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
        txtPresure = (TextView) findViewById(R.id.txtPresure);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtWeatherCond = (TextView) findViewById(R.id.txtWeatherCond);


        // Retrieving the information from MapsActivity
        final Intent intent = getIntent();
        longitude = intent.getExtras().get("longitude").toString();
        latitude = intent.getExtras().get("latitude").toString();
        location = longitude + "," + latitude ;



        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Processing the request and the information");
        pd.show();


        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{location});

        pd.hide();
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


            // not working still
            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                Drawable image = new BitmapDrawable(getResources(),bitmap);
                generalLayout.setBackground(image);
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

    void backButtonAction(){
        Intent intentBack = new Intent(this,MapsActivity.class);
        startActivity(intentBack);
    }
}
