package com.claire.traveldiary.mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.claire.traveldiary.MainActivity;
import com.claire.traveldiary.R;
import com.claire.traveldiary.component.SpacesItemDecoration;
import com.claire.traveldiary.data.DeletedDiary;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.claire.traveldiary.edit.EditPresenter;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class MainPageFragment extends Fragment implements MainPageContract.View {

    private static final String TAG = "MainPageFragment";

    private MainPageContract.Presenter mPresenter;
    private MainPageAdapter mMainPageAdapter;

    private EditPresenter mEditPresenter;

    private Diary mNewDiary;

    private DiaryDatabase mRoomDb;

    private ImageButton mAddDiary;


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
        mMainPageAdapter = new MainPageAdapter(mPresenter,getContext());

        mRoomDb = DiaryDatabase.getIstance(getContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPageAdapter.refreshAfterDownload();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mainpage, container, false);

        mAddDiary = root.findViewById(R.id.btn_add_diary);

        if (mRoomDb.getDiaryDAO().getAllDiaries().size() > 0) {
            mAddDiary.setVisibility(View.GONE);
        } else {
            mAddDiary.setVisibility(View.VISIBLE);
            mAddDiary.setOnClickListener(v -> {
                ((MainActivity) getActivity()).openEdit(mNewDiary);
            });
        }

        RecyclerView recyclerView = root.findViewById(R.id.recycler_main_page);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mMainPageAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setPadding(40,0,40,0);
        recyclerView.addItemDecoration(new SpacesItemDecoration(6));


        return root;
    }

    @Override
    public void openEditPage(Diary diary) {
        ((MainActivity) getActivity()).openEdit(diary);
    }

    @Override
    public void deleteDiaryUi(int id) {
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();

        //insert deleted diary
        DeletedDiary deletedDiary = new DeletedDiary();
        deletedDiary.setId(id);
        diaryDAO.insertDeletedDiary(deletedDiary);
        Log.d(TAG,"Deleted diary " + diaryDAO.getAllDeletedDiariesId().size());

        //delete diary from room
        diaryDAO.deleteDiarybyId(id);
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
        mMainPageAdapter.refreshUi(diaries);
    }

    @Override
    public void refreshSearchStatusUi() {
        mMainPageAdapter.refreshSearchStatus();
    }
}
