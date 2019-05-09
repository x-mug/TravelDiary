package com.claire.traveldiary.settings.download;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;

public interface DownloadContract {

    interface View extends BaseView<Presenter> {

        void successfullyDownloadUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void ReadDataFromFirebase();

    }
}
