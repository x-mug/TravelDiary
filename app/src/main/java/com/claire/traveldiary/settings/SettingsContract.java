package com.claire.traveldiary.settings;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter> {

        void openSyncDialogUi();

        void openDownloadDialogUi();

        void loginFacebookUi();

        void logoutFacebookUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openSyncDialog();

        void openDownloadDialog();

        void loginFacebook();

        void logoutFacebook();
    }
}
