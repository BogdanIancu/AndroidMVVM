package ro.facemsoft.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.facemsoft.coolweather.model.Weather;
import ro.facemsoft.coolweather.presenters.MainActivityPresenter;
import ro.facemsoft.coolweather.services.WeatherService;

public class MainActivity extends AppCompatActivity
        implements MainActivityPresenter.View, Callback<Weather> {

    @BindView(R.id.cityEditText) EditText cityEditText;
    @BindView(R.id.temperatureTextView) TextView temperatureTextView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.weatherImageView) ImageView imageView;
    @BindString(R.string.api_key) String apiKey;
    private MainActivityPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);
    }

    @OnClick(R.id.searchButton)
    public void searchClickHandler(View view) {
        String city = cityEditText.getText().toString();
        try {
            presenter.searchCommand(city);
        }
        catch(Exception e) {
            Toast.makeText(this, R.string.input_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean validateInput(String value) {
        return (value != null) && value.length() > 2;
    }

    @Override
    public void performSearch(String value) {
        WeatherService weatherService = new WeatherService();
        Call<Weather> call = weatherService.getWeatherData(value, apiKey);
        call.enqueue(this);
    }

    @Override
    public void updateTemperature(int value) {
        temperatureTextView.setText(String.format(Locale.getDefault(), "%d° C", value));
    }

    @Override
    public void updateDescription(String value) {
        descriptionTextView.setText(value);
    }

    @Override
    public void updateImage(String value) {
        Glide.with(this).load(value).into(imageView);
    }

    @Override
    public void displayServiceErrorMessage() {
        Toast.makeText(this, R.string.service_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Call<Weather> call, Response<Weather> response) {
        if(response.isSuccessful()) {
            Weather weather = response.body();
            if(weather != null) {
                updateTemperature(weather.getTemperature());
                updateDescription(weather.getDescription());
                updateImage(weather.getImageUrl());
            }
        }
        else {
            displayServiceErrorMessage();
        }
    }

    @Override
    public void onFailure(Call<Weather> call, Throwable t) {
        displayServiceErrorMessage();
    }
}
