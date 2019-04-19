package com.claire.traveldiary.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claire.traveldiary.MainActivity;
import com.claire.traveldiary.R;

import static android.support.v4.util.Preconditions.checkNotNull;

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
        mSettingsAdapter = new SettingsAdapter(mPresenter);
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
    public void openSyncDialogUi() {
        ((MainActivity) getActivity()).openSyncDialog();
    }
}
