package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myweatherapp.databinding.ActivityMainBinding;
import com.example.myweatherapp.MyWeather;

public class MainActivity extends AppCompatActivity implements com.example.myweatherapp.MyWeatherTaskListener
{
    private ActivityMainBinding binding;

    //Web URL of the JSON file
    private String mApiKey = "9dc2b5fbf5a625639bc953e434afe704";
    private String mCity = "New Orleans";
    private String mCountry = "United States of America";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //http://api.openweathermap.org/data/2.5/weather?q=city,country&APPID={your api key};
        String weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=" + mCity + "," + mCountry + "&APPID=" + mApiKey;
        new com.example.myweatherapp.MyWeatherTask(this).execute(weatherURL);
    }

    @Override
    public void onMyWeatherTaskPreExecute()
    {
        binding.myLoadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMyWeatherTaskPostExecute(MyWeather myWeather)
    {
        if (myWeather != null)
        {
            binding.cityTextView.setText(mCity);
            binding.countryTextView.setText(mCountry);

            binding.weatherConditionTextView.setText(myWeather.getWeatherCondition());
            binding.weatherDescriptionTextView.setText(myWeather.getWeatherDescription());

            int temp = Math.round(myWeather.getTemperature() - 273.15f);
            String tempStr = String.valueOf(temp);
            binding.temperatureTextView.setText(tempStr);

            String imgUrl = "http://openweathermap.org/img/wn/" + myWeather.getWeatherIconStr() + "@2x.png";

            Glide.with(MainActivity.this)
                    .asBitmap()
                    .load(imgUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(binding.weatherIconImageView);
        }
        binding.myLoadingLayout.setVisibility(View.GONE);
    }
}