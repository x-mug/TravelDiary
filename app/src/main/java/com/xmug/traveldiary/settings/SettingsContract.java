package com.xmug.traveldiary.settings;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter> {

        void openSyncDialogUi();

        void openDownloadDialogUi();

        void openFontDialogUi();

        void openLanguageDialogUi();

        void openFeedbackUi();

        void loginFacebookUi();

        void logoutFacebookUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openSyncDialog();

        void openDownloadDialog();

        void openFontDialog();

        void openLanguageDialog();

        void openFeedback();

        void loginFacebook();

        void logoutFacebook();
    }
}
