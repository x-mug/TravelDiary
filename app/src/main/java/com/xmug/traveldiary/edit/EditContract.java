package com.xmug.traveldiary.edit;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;
import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.DiaryPlace;

public interface EditContract {

    interface View extends BaseView<Presenter> {

        void openWeatherDialogUi();

        void openGalleryUi();

        void openDatePickerUi();

        void clickSaveDiaryUi();

        void openEditPageUi(Diary diary);

        void clickEditDiaryUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openWeatherDialog();

        void openGallery();

        void openDatePicker();

        void clickSaveDiary();

        void insertOrUpdateDiary(Diary diary);

        void insertOrUpdatePlace(DiaryPlace diaryPlace);

        void loadDiaryData(Diary diary);

        void clickEditDiary();

    }
}
