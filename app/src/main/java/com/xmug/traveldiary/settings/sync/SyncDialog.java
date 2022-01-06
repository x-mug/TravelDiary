package com.xmug.traveldiary.settings.sync;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xmug.traveldiary.R;
import com.xmug.traveldiary.util.UserManager;



import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class SyncDialog extends BottomSheetDialogFragment implements SyncContract.View {

    private SyncContract.Presenter mPresenter;

    private ConstraintLayout mLayout;
    private TextView mSyncTextView;
    private Button mSync;


    public SyncDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setPresenter(SyncContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        setStyle(STYLE_NO_FRAME, R.style.ShowDiaryDialog);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_sync, container, false);
        this.getDialog().setCanceledOnTouchOutside(true);

        mLayout = dialogView.findViewById(R.id.sync_popup);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        mSync = dialogView.findViewById(R.id.btn_sync);
        mSyncTextView = dialogView.findViewById(R.id.tv_sync);

        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //click sync
        if (UserManager.getInstance().isLoggedIn()) {
            mSync.setOnClickListener(v ->
                    mPresenter.insertOrUpdateDataToFirebase());

        } else {
            mSync.setClickable(false);
            mSyncTextView.setText(R.string.dialog_tv_sync);
            mSync.setTextColor(getActivity().getResources().getColor(R.color.white));
        }
    }

    @Override
    public void successfullySyncUi() {
        Toast.makeText(getContext(), "Successfully Sync!", Toast.LENGTH_SHORT).show();
        mSync.setClickable(true);
        mSync.setTextColor(getActivity().getResources().getColor(R.color.quantum_black_100));
        dismiss();
    }

    @Override
    public void noDataSyncUi() {
        Toast.makeText(getContext(), "No diary can sync!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void dismiss() {
        //mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));
        new Handler().postDelayed(super::dismiss, 200);
    }
}
