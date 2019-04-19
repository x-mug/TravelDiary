package com.claire.traveldiary.settings.sync;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.claire.traveldiary.MainActivity;
import com.claire.traveldiary.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class SyncDialog extends BottomSheetDialogFragment implements SyncContract.View {

    private static final String TAG = "SyncDialog";

    private SyncContract.Presenter mPresenter;

    private ConstraintLayout mLayout;

    private CallbackManager mCallbackManager;
    private LoginButton mLoginButton;

    private FirebaseAuth mAuth;

    public SyncDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

        //facebook login
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = dialogView.findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("email");
        // If using in a fragment
        mLoginButton.setFragment(this);

        // Callback registration
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG","Success token : " + loginResult.getAccessToken().getToken());
                //handleFacebookAccessToken(loginResult.getAccessToken());
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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
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
