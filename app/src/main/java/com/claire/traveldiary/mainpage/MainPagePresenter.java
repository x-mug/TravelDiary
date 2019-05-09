package com.claire.traveldiary.mainpage;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.claire.traveldiary.data.DeletedDiary;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class MainPagePresenter implements MainPageContract.Presenter {

    private MainPageContract.View mView;

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
    public void openEdit(Diary diary) {
        mView.openEditPage(diary);
    }

    @Override
    public void deleteDiary(int id) {
        mDiaryDAO = mRoomDb.getDiaryDAO();

        //insert deleted diary
        DeletedDiary deletedDiary = new DeletedDiary();
        deletedDiary.setId(id);
        mDiaryDAO.insertDeletedDiary(deletedDiary);
        Log.d("TAG","Deleted diary " + mDiaryDAO.getAllDeletedDiariesId().size());

        //delete diary from room
        mDiaryDAO.deleteDiarybyId(id);
        mView.deleteDiaryUi();
    }

    @Override
    public void shareDiary(Diary diary) {
        mView.shareDiaryUi(diary);
    }

    @Override
    public void loadSearchData(List<Diary> diaries) {
        mView.loadSearchDataUi(diaries);
    }

    @Override
    public void refreshSearchStatus() {
        mView.refreshSearchStatusUi();
    }

    @Override
    public void changeLayout(int status) {
        mView.changeLayoutUi(status);
    }

    @Override
    public void setFontType(TextView title, TextView date) {
        mView.setFontTypeUi(title, date);
    }

    @Override
    public void sortDiaryByDate(List<Diary> diaries) {
        mView.sortDiaryByDateUi(diaries);
    }

}
