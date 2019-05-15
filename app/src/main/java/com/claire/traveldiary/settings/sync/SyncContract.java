package com.claire.traveldiary.settings.sync;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;


public interface SyncContract {

    interface View extends BaseView<Presenter> {

        void successfullySyncUi();

        void noDataSyncUi();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void insertOrUpdateDataToFirebase();

    }

}
