package com.claire.traveldiary.settings.sync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.claire.traveldiary.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import java.util.Arrays;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class SyncDialog extends BottomSheetDialogFragment implements SyncContract.View {

    private static final String TAG = "SyncDialog";

    private SyncContract.Presenter mPresenter;

    private ConstraintLayout mLayout;

    private CallbackManager mCallbackManager;
    private Button mLoginFaceBook;
    private Button mSync;


    public SyncDialog() {
    }

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

        mLayout = dialogView.findViewById(R.id.sync_popup);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        mLoginFaceBook = dialogView.findViewById(R.id.btn_login);
        mSync = dialogView.findViewById(R.id.btn_sync);

        if (isLoggedIn()) {
            //facebook button gone
            //sync button show
        } else {

        }

        //facebook login
        mCallbackManager = CallbackManager.Factory.create();
        mLoginFaceBook.setOnClickListener(v ->
                LoginManager.getInstance().logInWithReadPermissions(getActivity(),Arrays.asList("email")));


        // Callback registration
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG","Success token : " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        return dialogView;
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
