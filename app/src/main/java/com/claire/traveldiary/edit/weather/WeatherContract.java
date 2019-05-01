package com.claire.traveldiary.edit.weather;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;

public interface WeatherContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

    }

}
