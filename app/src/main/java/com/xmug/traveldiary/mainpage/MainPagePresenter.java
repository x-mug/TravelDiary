package com.xmug.traveldiary.mainpage;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.xmug.traveldiary.data.DeletedDiary;
import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.room.DiaryDAO;
import com.xmug.traveldiary.data.room.DiaryDatabase;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class MainPagePresenter implements MainPageContract.Presenter {

    private MainPageContract.View mView;

    private List<Diary> mDiaryList;

    private DiaryDatabase mRoomDb;
    private DiaryDAO mDiaryDAO;

    @SuppressLint("RestrictedApi")
    public MainPagePresenter(@NonNull MainPageContract.View view, @NonNull DiaryDatabase roomDb) {
        mView = checkNotNull(view, "view cannot be null!");
        mRoomDb = checkNotNull(roomDb, "roomDb cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {}

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void loadDiaryData() {
        mDiaryList = mRoomDb.getDiaryDAO().getAllDiaries();
        mView.loadDiaryDataUi(mDiaryList);
        mView.sortDiaryByDateUi(mDiaryList);
    }

    @Override
    public void openEdit(Diary diary) {
        mView.openEditPage(diary);
    }

    @Override
    public void removeDiary(int id) {
        mDiaryDAO = mRoomDb.getDiaryDAO();

        //insert deleted diary
        DeletedDiary deletedDiary = new DeletedDiary();
        deletedDiary.setId(id);
        mDiaryDAO.insertDeletedDiary(deletedDiary);
        Log.d("TAG","Deleted diary " + mDiaryDAO.getAllDeletedDiariesId().size());

        //delete diary from room
        mDiaryDAO.removeDiarybyId(id);
        mView.removeDiaryUi();
    }

    @Override
    public void shareDiary(Diary diary) {
        mView.shareDiaryUi(diary);
    }

    @Override
    public void loadSearchData(String searchText1, String searchText2) {
        List<Diary> diaries = mRoomDb.getDiaryDAO().getDiariesBySearch(searchText1, searchText2);
            if (diaries != null) {
                mView.loadSearchDataUi(diaries);
            }
    }

    @Override
    public void updateSearchStatus() {
        mDiaryList = mRoomDb.getDiaryDAO().getAllDiaries();
        mView.updateSearchStatusUi(mDiaryList);
        mView.sortDiaryByDateUi(mDiaryList);
    }

    @Override
    public void changeLayout(int status) {
        mView.changeLayoutUi(status);
    }

    @Override
    public void setFontType(TextView title, TextView date) {
        mView.setFontTypeUi(title, date);
    }

}
