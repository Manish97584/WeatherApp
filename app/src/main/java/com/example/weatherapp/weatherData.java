package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature,micon,mcity,mWeathertype;
    private int mcondiotion;

    public static weatherData fromJson(JSONObject jsonObject)   //initializing from json - constructor
    {
        try
        {
            weatherData weatherD = new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mcondiotion=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeathertype=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mcondiotion);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedvalue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedvalue);

            return weatherD;
        }
       catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return "tstorms";
        }
        if(condition>300 && condition<=500)
        {
            return "lightrain";
        }
        if(condition>500 && condition<=600)
        {
            return "rain";
        }
        if(condition>600 && condition<=700)
        {
            return "snow";
        }
        if(condition>700 && condition<=771)
        {
            return "fog";
        }
        if(condition>771 && condition<=800)
        {
            return "overcast";
        }
        if(condition==800)
        {
            return "sunny";
        }
        if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        if(condition>=900 && condition<=902)
        {
            return "tstorms";
        }
        if(condition==903)
        {
            return "snow";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstorm1";
        }
        return"searchicon";
    }

    public String getmTemperature() {
        return mTemperature+" °C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeathertype() {
        return mWeathertype;
    }
}
