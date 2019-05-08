package com.claire.traveldiary.settings.fontsize;

import com.claire.traveldiary.base.BasePresenter;
import com.claire.traveldiary.base.BaseView;

public interface FontContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);
    }
}
