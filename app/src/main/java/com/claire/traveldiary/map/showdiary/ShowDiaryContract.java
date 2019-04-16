package com.claire.traveldiary.map.showdiary;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;
import com.claire.traveldiary.data.Diary;

public interface ShowDiaryContract {

    interface View extends BaseView<Presenter> {

        void openDiaryDialogByPlace(Diary diary);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadDiaryByPlace(Diary diary);

    }
}
