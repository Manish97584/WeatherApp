package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.jar.Attributes;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String apiKey = "2ba53e1544639208f8ba5850bc8044c7";
    final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";

    final long min_time = 5000;
    final float min_distance = 1000;
    final int REQUEST_CODE = 101;

    String location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;
    LocationManager mlocationManager;
    LocationListener mlocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weather_condition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weathericon);
        mCityFinder = findViewById(R.id.cityfinder);
        NameofCity = findViewById(R.id.cityname);

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        String city=mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewLocation(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewLocation(String city)
    {
        RequestParams params= new RequestParams();
        params.put("q",city);
        params.put("Apikey",apiKey);
        letsdosomenetworking(params);
    }

    private void getWeatherForCurrentLocation() {
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("long",Longitude);
                params.put("ApiKey",apiKey);
                letsdosomenetworking(params);
            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mlocationManager.requestLocationUpdates(location_Provider, min_time, min_distance, mlocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "LOCATED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied
                Toast.makeText(MainActivity.this, "not LOCATED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void letsdosomenetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(weatherUrl,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                Toast.makeText(MainActivity.this,"Data Retreived Successflly",Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);//calling constructor
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void updateUI(weatherData weather)
    {
        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeathertype());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mlocationManager!=null)
        {
            mlocationManager.removeUpdates(mlocationListener);
        }
    }
}