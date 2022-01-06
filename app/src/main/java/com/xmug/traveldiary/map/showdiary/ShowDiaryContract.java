package com.xmug.traveldiary.map.showdiary;

import android.widget.TextView;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;
import com.xmug.traveldiary.data.Diary;

import java.util.List;


public interface ShowDiaryContract {

    interface View extends BaseView<Presenter> {

        void loadDiaryByPlaceUi(List<Diary> diaries);

        void openEditUi(Diary diary);

        void closePopupUi();

        void setFontTypeUi(TextView title, TextView date);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadDiaryByPlace(double lat, double lng);

        void openEdit(Diary diary);

        void closePopup();

        void setFontType(TextView title, TextView date);

    }
}
