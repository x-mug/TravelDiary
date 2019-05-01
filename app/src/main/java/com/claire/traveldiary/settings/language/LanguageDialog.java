package com.claire.traveldiary.settings.language;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;

import com.claire.traveldiary.R;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class LanguageDialog extends DialogFragment implements LanguageContract.View {

    private static final String TAG = "LanguageDialog";

    private LanguageContract.Presenter mPresenter;

    private RadioButton mRadioButton;

    public LanguageDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setPresenter(LanguageContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        setStyle(STYLE_NO_FRAME, R.style.ShowDiaryDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_language, container, false);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);


        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
