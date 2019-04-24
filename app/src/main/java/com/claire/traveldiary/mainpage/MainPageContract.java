package com.claire.traveldiary.mainpage;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;
import com.claire.traveldiary.data.Diary;

import java.util.List;

public interface MainPageContract {

    interface View extends BaseView<Presenter> {

        void openEditPage(Diary diary);

        void deleteDiaryUi(int id);

        void shareDiaryUi(Diary diary);

        void loadSearchDataUi(List<Diary> diaries);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openEdit(Diary diary);

        void deleteDiary(int id);

        void shareDiary(Diary diary);

        void loadSearchData(List<Diary> diaries);

    }
}
