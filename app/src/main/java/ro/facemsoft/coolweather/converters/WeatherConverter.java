package ro.facemsoft.coolweather.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ro.facemsoft.coolweather.model.Weather;

public class WeatherConverter implements JsonDeserializer<Weather> {

    @Override
    public Weather deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray weatherArray = jsonObject.get("weather").getAsJsonArray();
        JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
        Weather weather = new Weather();
        weather.setDescription(weatherObject.get("description").getAsString());
        String icon = weatherObject.get("icon").getAsString();
        weather.setImageUrl(String.format("https://openweathermap.org/img/w/%s.png", icon));
        JsonObject mainObject = jsonObject.get("main").getAsJsonObject();
        weather.setTemperature((int)mainObject.get("temp").getAsDouble());
        return weather;
    }
}
