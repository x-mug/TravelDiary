package com.claire.traveldiary.map;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;
import com.claire.traveldiary.data.DiaryPlace;

import java.util.List;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        void loadDiaryOnMapUi(List<DiaryPlace> places);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadDiaryOnMap();
    }
}
