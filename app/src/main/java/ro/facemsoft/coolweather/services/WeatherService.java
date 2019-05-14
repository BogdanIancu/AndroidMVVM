package ro.facemsoft.coolweather.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.facemsoft.coolweather.converters.WeatherConverter;
import ro.facemsoft.coolweather.model.Weather;

public class WeatherService {
    private interface OpenWeatherMapService {
        @GET("weather?units=metric&format=json&lang=en")
        Call<Weather> getCurrentWeather(@Query("q") String city,
                                        @Query("appid") String apiKey);
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build();

    private OpenWeatherMapService service =
            retrofit.create(OpenWeatherMapService.class);

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Weather.class, new WeatherConverter());
        return builder.create();
    }

    public Call<Weather> getWeatherData(String city, String apiKey) {
        return service.getCurrentWeather(city, apiKey);
    }
}
