package com.xmug.traveldiary.edit.weather;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;

public interface WeatherContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

    }

}
