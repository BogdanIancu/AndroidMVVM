package ro.facemsoft.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ro.facemsoft.coolweather.services.AsyncTaskWeatherService;

public class MainActivity extends AppCompatActivity {

    private EditText cityEditText = null;
    private TextView temperatureTextView = null;
    private TextView descriptionTextView = null;
    private ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        imageView = findViewById(R.id.weatherImageView);
    }

    public void searchClickHandler(View view) {
        String city = cityEditText.getText().toString();
        if(isInputDataValid(city)) {
            AsyncTaskWeatherService asyncTask  = new AsyncTaskWeatherService(temperatureTextView, descriptionTextView, imageView);
            asyncTask.execute(city);
        }
        else {
            Toast.makeText(this, R.string.input_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputDataValid(String value) {
        return (value != null) && value.length() > 2;
    }
}
