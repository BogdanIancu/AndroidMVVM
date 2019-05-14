package ro.facemsoft.coolweather.services;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ro.facemsoft.coolweather.converters.WeatherConverter;
import ro.facemsoft.coolweather.model.Weather;
import ro.facemsoft.coolweather.presenters.MainActivityPresenter;

public class AsyncTaskWeatherService extends AsyncTask<String, Void, Weather> {
    private final static String OPEN_WEATHER_API_KEY = "7b10426ee90376dc3d6525f847128b35";
    private MainActivityPresenter.View view;

    public AsyncTaskWeatherService(MainActivityPresenter.View view) {
        this.view = view;
    }

    @Override
    protected Weather doInBackground(String... strings) {
        if(strings != null && strings.length > 0) {
            String city = strings[0];
            String address =
                    String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&format=json&lang=en",
                            city, OPEN_WEATHER_API_KEY);
            try {
                URL url = new URL(address);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder stringBuilder = new StringBuilder();

                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String result = stringBuilder.toString();

                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Weather.class, new WeatherConverter());
                Gson gson = builder.create();
                return gson.fromJson(result, Weather.class);
            }
            catch (Exception e) {
                Log.e("AsyncTask", e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        if(weather != null) {
            view.updateDescription(weather.getDescription());
            view.updateTemperature(weather.getTemperature());
            view.updateImage(weather.getImageUrl());
        }
        else {
            view.displayServiceErrorMessage();
        }
    }
}
