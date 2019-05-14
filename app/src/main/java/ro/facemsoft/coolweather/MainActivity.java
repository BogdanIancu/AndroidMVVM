package ro.facemsoft.coolweather;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ro.facemsoft.coolweather.model.Weather;
import ro.facemsoft.coolweather.presenters.MainActivityPresenter;
import ro.facemsoft.coolweather.services.AsyncTaskWeatherService;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {

    @BindView(R.id.cityEditText) EditText cityEditText;
    @BindView(R.id.temperatureTextView) TextView temperatureTextView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.weatherImageView) ImageView imageView;
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
        AsyncTaskWeatherService service = new AsyncTaskWeatherService(this);
        service.execute(value);
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
}
