package ro.facemsoft.coolweather;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.facemsoft.coolweather.databinding.ActivityMainBinding;
import ro.facemsoft.coolweather.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.temperatureTextView) TextView temperatureTextView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.weatherImageView) ImageView imageView;

    private MainViewModel viewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setViewmodel(viewModel);
        setContentView(binding.getRoot());

        ButterKnife.bind(this);

        final Observer<String> temperatureObserver = new Observer<String> () {
            @Override
            public void onChanged(@Nullable String s) {
                temperatureTextView.setText(s);
            }
        };
        viewModel.getTemperature().observe(this, temperatureObserver);

        final Observer<String> descriptionObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                descriptionTextView.setText(s);
            }
        };

        viewModel.getDescription().observe(this, descriptionObserver);

        final Observer<String> imageUrlObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Glide.with(MainActivity.this).load(s).into(imageView);
            }
        };

        viewModel.getImageUrl().observe(this, imageUrlObserver);

        final Observer<Integer> errorMessageObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Toast.makeText(MainActivity.this, integer, Toast.LENGTH_SHORT).show();
            }
        };

        viewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

}
