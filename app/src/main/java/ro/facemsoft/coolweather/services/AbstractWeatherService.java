package ro.facemsoft.coolweather.services;

import ro.facemsoft.coolweather.model.Weather;

public interface AbstractWeatherService {
    public Weather getCurrentWeather(String cityName);
}
