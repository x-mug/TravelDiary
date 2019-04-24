package com.claire.traveldiary.settings.fontsize;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.claire.traveldiary.R;
import com.claire.traveldiary.util.CustomTextView;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class FontDialog extends DialogFragment implements FontContract.View {

    private static final String TAG = "FontDialog";

    private FontContract.Presenter mPresenter;

    private NumberPicker mNumberPicker;
    private Button mOK;
    private Button mCancel;

    public FontDialog() {
    }

    @Override
    public void setPresenter(FontContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_font, container, false);

        mNumberPicker = dialogView.findViewById(R.id.numberPicker);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(5);
        mNumberPicker.setValue(0);
        mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumberPicker.setDisplayedValues(new String[] {"Allura","Amatic","Blackjack","Brizel","Dancing",
                "Farsan","Hand Writing","Kaushan","Nanum Brush","Nunito"});

        mOK = dialogView.findViewById(R.id.btn_ok);
        mCancel = dialogView.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(v -> dismiss());

        return dialogView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mNumberPicker.getValue();
                switch (pos) {
                    case 0:
                        setFont(0);
                        dismiss();
                        break;
                    case 1:
                        setFont(1);
                        dismiss();
                        break;
                    case 2:
                        setFont(2);
                        dismiss();
                        break;
                    case 3:
                        setFont(3);
                        dismiss();
                        break;
                    case 4:
                        setFont(4);
                        dismiss();
                        break;
                    case 5:
                        setFont(5);
                        dismiss();
                        break;
                    case 6:
                        setFont(6);
                        dismiss();
                        break;
                    case 7:
                        setFont(7);
                        dismiss();
                        break;
                    case 8:
                        setFont(8);
                        dismiss();
                        break;
                    case 9:
                        setFont(9);
                        dismiss();
                        break;
                }
            }
        });

    }

    private void setFont(int font) {
        Log.d(TAG,"Fonnnnt" + font);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
