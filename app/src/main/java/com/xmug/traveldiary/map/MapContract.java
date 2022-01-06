package com.xmug.traveldiary.map;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;
import com.xmug.traveldiary.data.DiaryPlace;

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
