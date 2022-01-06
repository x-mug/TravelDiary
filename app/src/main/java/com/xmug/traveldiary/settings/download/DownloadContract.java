package com.xmug.traveldiary.settings.download;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;

public interface DownloadContract {

    interface View extends BaseView<Presenter> {

        void successfullyDownloadUi();

        void noDataInFirebaseUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void ReadDataFromFirebase();

    }
}
