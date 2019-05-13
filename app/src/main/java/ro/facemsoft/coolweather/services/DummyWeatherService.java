package ro.facemsoft.coolweather.services;

import android.graphics.Bitmap;

import ro.facemsoft.coolweather.model.Weather;

public class DummyWeatherService implements AbstractWeatherService {
    public Weather getCurrentWeather(String cityName) {
        Weather weather = new Weather();
        weather.setTemperature(26);
        weather.setDescription("sunny");
        Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        image.eraseColor(android.graphics.Color.GREEN);
        weather.setImage(image);

        return weather;
    }
}
