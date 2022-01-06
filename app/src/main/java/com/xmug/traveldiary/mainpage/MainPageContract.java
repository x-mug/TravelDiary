package com.xmug.traveldiary.mainpage;

import android.widget.TextView;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;
import com.xmug.traveldiary.data.Diary;

import java.util.List;

public interface MainPageContract {

    interface View extends BaseView<Presenter> {

        void loadDiaryDataUi(List<Diary> diaries);

        void openEditPage(Diary diary);

        void removeDiaryUi();

        void shareDiaryUi(Diary diary);

        void loadSearchDataUi(List<Diary> diaries);

        void updateSearchStatusUi(List<Diary> diaries);

        void changeLayoutUi(int status);

        void setFontTypeUi(TextView title, TextView date);

        void sortDiaryByDateUi(List<Diary> diaries);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadDiaryData();

        void openEdit(Diary diary);

        void removeDiary(int id);

        void shareDiary(Diary diary);

        void loadSearchData(String searchText1, String searchText2);

        void updateSearchStatus();

        void changeLayout(int status);

        void setFontType(TextView title, TextView date);


    }

}
