package com.xmug.traveldiary.settings.language;

import com.xmug.traveldiary.base.BasePresenter;
import com.xmug.traveldiary.base.BaseView;

public interface LanguageContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

    }
}
