package ro.facemsoft.coolweather.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.facemsoft.coolweather.R;
import ro.facemsoft.coolweather.model.Weather;
import ro.facemsoft.coolweather.services.WeatherService;

public class MainViewModel extends ViewModel implements Callback<Weather> {
    private String apiKey = "YOUR_APIKEY_HERE";
    private WeatherService weatherService;

    private MutableLiveData<String> city = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<String> temperature = new MutableLiveData<>();
    private MutableLiveData<String> imageUrl = new MutableLiveData<>();
    private MutableLiveData<Integer> errorMessage = new MutableLiveData<>();

    public MainViewModel() {
        weatherService = new WeatherService();
    }

    public MutableLiveData<String> getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city.setValue(city);
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<String> getTemperature() {
        return temperature;
    }

    public MutableLiveData<String> getImageUrl() {
        return imageUrl;
    }

    public MutableLiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    private boolean validateInput(String value) {
        return (value != null) && value.length() > 2;
    }

    public void searchCommand(View view) {
        String cityName = city.getValue();
        try {
            if(validateInput(cityName)) {
                WeatherService weatherService = new WeatherService();
                Call<Weather> call = weatherService.getWeatherData(cityName,
                        apiKey);
                call.enqueue(this);
            }
            else {
                errorMessage.setValue(R.string.input_error);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<Weather> call, Response<Weather> response) {
        if(response.isSuccessful()) {
            Weather weather = response.body();
            if(weather != null) {
                temperature.setValue(weather.getTemperature() + "° C");
                description.setValue(weather.getDescription());
                imageUrl.setValue(weather.getImageUrl());
            }
        }
        else {
            errorMessage.setValue(R.string.service_error);
        }
    }

    @Override
    public void onFailure(Call<Weather> call, Throwable t) {
        errorMessage.setValue(R.string.service_error);
    }
}
