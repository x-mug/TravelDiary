package com.claire.traveldiary.map.showdiary;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;
import com.claire.traveldiary.data.Diary;

import java.util.List;

public interface ShowDiaryContract {

    interface View extends BaseView<Presenter> {

        void openDiaryDialogByPlace(double lat, double lng);

        void openEditUi(Diary diary);

        void closePopupUi();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadDiaryByPlace(double lat, double lng);

        void openEdit(Diary diary);

        void closePopup();

    }
}
