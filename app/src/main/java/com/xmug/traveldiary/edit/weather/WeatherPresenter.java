package com.xmug.traveldiary.edit.weather;

import androidx.annotation.NonNull;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class WeatherPresenter implements WeatherContract.Presenter{

    private WeatherContract.View mWeatherView;

    public WeatherPresenter(@NonNull WeatherContract.View weatherView) {
        mWeatherView = checkNotNull(weatherView, "weatherView cannot be null!");
        mWeatherView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void start() {

    }
}
