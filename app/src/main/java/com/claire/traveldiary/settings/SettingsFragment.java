package com.claire.traveldiary.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claire.traveldiary.MainActivity;
import com.claire.traveldiary.R;
import com.claire.traveldiary.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;


public class SettingsFragment extends Fragment implements SettingsContract.View {

    private static final String TAG = "SettingsFragment";

    private SettingsContract.Presenter mPresenter;
    private SettingsAdapter mSettingsAdapter;


    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsAdapter = new SettingsAdapter(mPresenter, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_settings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mSettingsAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void openSyncDialogUi() {
        ((MainActivity) getActivity()).openSyncDialog();
    }

    @Override
    public void openDownloadDialogUi() {
        ((MainActivity) getActivity()).openDownloadDialog();
    }

    @Override
    public void openFontDialogUi() {
        ((MainActivity) getActivity()).openFontDialog();
    }

    @Override
    public void loginFacebookUi() {
        UserManager.getInstance().loginDiaryByFacebook(getActivity(), new UserManager.LoadCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Success Login! ");
                mSettingsAdapter.updateView();
            }

            @Override
            public void onFail(String errorMessage) {
                Log.d(TAG,"fail Login : ");
            }

            @Override
            public void onInvalidToken(String errorMessage) {
                Log.d(TAG,"onInvalidToken! : ");
            }
        });
    }

    @Override
    public void logoutFacebookUi() {
        UserManager.getInstance().logoutDiaryFromFacebook(new UserManager.LoadCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Success Logout! ");
                mSettingsAdapter.updateView();
            }

            @Override
            public void onFail(String errorMessage) {
                Log.d(TAG,"fail Logout : ");
            }

            @Override
            public void onInvalidToken(String errorMessage) {

            }
        });
    }

}
