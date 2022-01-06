package com.xmug.traveldiary.mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmug.traveldiary.MainActivity;
import com.xmug.traveldiary.R;
import com.xmug.traveldiary.component.GridSpacingItemDecoration;
import com.xmug.traveldiary.component.SpacesItemDecoration;
import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.room.DiaryDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class MainPageFragment extends Fragment implements MainPageContract.View {

    private static final String TAG = "MainPageFragment";

    private MainPageContract.Presenter mPresenter;
    private MainPageAdapter mMainPageAdapter;

    private Diary mNewDiary;

    private DiaryDatabase mRoomDb;

    private RecyclerView mRecyclerView;
    private ImageButton mAddDiary;
    private ImageButton mDownload;
    private TextView mExplain;

    private int mLayoutStatus;


    public MainPageFragment() {
    }

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(MainPageContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoomDb = DiaryDatabase.getIstance(getContext());
        mMainPageAdapter = new MainPageAdapter(mPresenter,getContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.loadDiaryData();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mainpage, container, false);

        mAddDiary = root.findViewById(R.id.btn_add_diary);
        mDownload = root.findViewById(R.id.btn_cloud_download);
        mExplain = root.findViewById(R.id.tv_mainpage_explain);

        mRecyclerView = root.findViewById(R.id.recycler_mainPage);
        mRecyclerView.setAdapter(mMainPageAdapter);
        mRecyclerView.setPadding(40,0,40,0);
        mRecyclerView.setHasFixedSize(true);


        if (mLayoutStatus == 0) {
            setWaterfallLayout();
        }
        if (mLayoutStatus == 1) {
            setLinearLayout();
        }

        if (mRoomDb.getDiaryDAO().getAllDiaries().size() > 0) {
            mAddDiary.setVisibility(View.GONE);
            mDownload.setVisibility(View.GONE);
            mExplain.setVisibility(View.GONE);
        } else {
            mAddDiary.setVisibility(View.VISIBLE);
            mDownload.setVisibility(View.VISIBLE);
            mExplain.setVisibility(View.VISIBLE);
            mAddDiary.setOnClickListener(v -> {
                ((MainActivity) getActivity()).openEdit(mNewDiary);
            });
            mDownload.setOnClickListener(v ->
                    ((MainActivity) getActivity()).openSettings());
        }

        return root;
    }

    private void setWaterfallLayout() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        if (mRecyclerView.getItemDecorationCount() > 0) {
            mRecyclerView.removeItemDecorationAt(0);
        }
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(40));
        mMainPageAdapter.getItemViewType(0);
        mMainPageAdapter.notifyDataSetChanged();
    }

    private void setLinearLayout() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mRecyclerView.getItemDecorationCount() > 0) {
            mRecyclerView.removeItemDecorationAt(0);
        }
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(40));
        mMainPageAdapter.getItemViewType(1);
        mMainPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadDiaryDataUi(List<Diary> diaries) {
        mMainPageAdapter.updateData(diaries);
    }

    @Override
    public void openEditPage(Diary diary) {
        ((MainActivity) getActivity()).openEdit(diary);
    }

    @Override
    public void removeDiaryUi() {
        mMainPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void shareDiaryUi(Diary diary) {
        //image
        ArrayList<Uri> imageUri = new ArrayList<>();
        for (int i = 0; i < diary.getImage().size(); i++) {
            Uri uriFromLocal = Uri.parse("file://" + diary.getImage().get(i));
            imageUri.add(uriFromLocal);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUri);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, diary.getTitle());
            sendIntent.putExtra(Intent.EXTRA_TEXT, diary.getContent());
            sendIntent.setType("*/*");
            startActivity(Intent.createChooser(sendIntent, "Share Diary To Your Friends!"));
        }
    }

    @Override
    public void loadSearchDataUi(List<Diary> diaries) {
        mMainPageAdapter.updateSearchData(diaries);
    }

    @Override
    public void updateSearchStatusUi(List<Diary> diaries) {
        mMainPageAdapter.updateSearchDataStatus(diaries);
    }


    @Override
    public void changeLayoutUi(int status) {
        if (status == 0) {
            mLayoutStatus = status;
            mMainPageAdapter.changeLayout(0);
            setWaterfallLayout();
        } else {
            mLayoutStatus = status;
            mMainPageAdapter.changeLayout(1);
            setLinearLayout();
        }
    }

    @Override
    public void setFontTypeUi(TextView title, TextView date) {
        ((MainActivity) getActivity()).setFontType(title, date);
    }

    @Override
    public void sortDiaryByDateUi(List<Diary> diaries) {
        ((MainActivity) getActivity()).sortDiaryByDate(diaries);
    }
}
