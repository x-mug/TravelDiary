package com.claire.traveldiary.settings.download;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.claire.traveldiary.R;
import com.claire.traveldiary.util.UserManager;



import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class DownloadDialog extends BottomSheetDialogFragment implements DownloadContract.View {

    private static final String TAG = "DownloadDialog";

    private DownloadContract.Presenter mPresenter;


    private ConstraintLayout mLayout;
    private TextView mDownloadTextView;
    private TextView mDownloadNotice;
    private Button mDownload;


    public DownloadDialog() {
    }

    @Override
    public void setPresenter(DownloadContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        setStyle(STYLE_NO_FRAME, R.style.ShowDiaryDialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_download, container, false);
        this.getDialog().setCanceledOnTouchOutside(true);

        mLayout = dialogView.findViewById(R.id.download_popup);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        mDownload = dialogView.findViewById(R.id.btn_download);
        mDownloadTextView = dialogView.findViewById(R.id.tv_download);
        mDownloadNotice = dialogView.findViewById(R.id.tv_download_notice);

        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //click download
        if (UserManager.getInstance().isLoggedIn()) {
            mDownloadNotice.setVisibility(View.VISIBLE);
            mDownload.setOnClickListener(v ->{
                mPresenter.ReadDataFromFirebase();
                    });
        } else {
            mDownload.setClickable(false);
            mDownloadTextView.setText(getActivity().getResources().getString(R.string.dialog_tv_download));
            mDownload.setTextColor(getActivity().getResources().getColor(R.color.white));
            mDownloadNotice.setVisibility(View.GONE);
        }
    }

    @Override
    public void successfullyDownloadUi() {
        mDownload.setClickable(false);
        mDownload.setTextColor(getActivity().getResources().getColor(R.color.white));
        Toast.makeText(getContext(), "Successfully Download!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void dismiss() {
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));
        new Handler().postDelayed(super::dismiss, 200);
    }
}
