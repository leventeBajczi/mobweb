package hu.bme.aut.weatherinfo.feature.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import hu.bme.aut.weatherinfo.R;
import hu.bme.aut.weatherinfo.model.WeatherData;
import hu.bme.aut.weatherinfo.network.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements WeatherDataHolder {

    private static final String TAG = "DetailsActivity";
    private WeatherData weatherData=null;
    public static final String EXTRA_CITY_NAME = "extra.city_name";

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        city = getIntent().getStringExtra(EXTRA_CITY_NAME);

        getSupportActionBar().setTitle(getString(R.string.weather, city));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWeatherData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public WeatherData getWeatherData() {
        return weatherData;
    }
    private void displayWeatherData(WeatherData receivedWeatherData) {
        weatherData = receivedWeatherData;
        ViewPager mainViewPager = findViewById(R.id.mainViewPager);
        DetailsPagerAdapter detailsPagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager(), this);
        mainViewPager.setAdapter(detailsPagerAdapter);
    }

    private void loadWeatherData() {
        NetworkManager.getInstance().getWeather(city).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call,
                                   @NonNull Response<WeatherData> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    displayWeatherData(response.body());
                } else {
                    Toast.makeText(DetailsActivity.this,
                            "Error: "+response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(DetailsActivity.this,
                        "Network request error occurred, check LOG",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}