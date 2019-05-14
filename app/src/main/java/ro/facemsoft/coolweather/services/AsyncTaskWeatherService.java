package ro.facemsoft.coolweather.services;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ro.facemsoft.coolweather.model.Weather;

public class AsyncTaskWeatherService extends AsyncTask<String, Void, Weather> {
    private final static String OPEN_WEATHER_API_KEY = "7b10426ee90376dc3d6525f847128b35";
    private TextView temperatureTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private ImageDownloadingService imageService;

    public AsyncTaskWeatherService(TextView temperatureTextView, TextView descriptionTextView, ImageView imageView) {
        this.temperatureTextView = temperatureTextView;
        this.descriptionTextView = descriptionTextView;
        this.imageView = imageView;
        imageService = new ImageDownloadingService();
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
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();

                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String result = stringBuilder.toString();
                Weather weather = new Weather();
                JSONObject json = new JSONObject(result);
                JSONArray weatherArray = json.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                weather.setDescription(weatherObject.getString("description"));
                String icon = weatherObject.getString("icon");
                Bitmap image = imageService.downloadImage(icon);
                weather.setImage(image);
                JSONObject mainObject = json.getJSONObject("main");
                weather.setTemperature((int)mainObject.getDouble("temp"));
                return weather;
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

        temperatureTextView.setText(weather.getTemperature() + " °C");
        descriptionTextView.setText(weather.getDescription());
        imageView.setImageBitmap(weather.getImage());
    }
}
