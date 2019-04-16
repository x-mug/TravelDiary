package com.claire.traveldiary.map.showdiary;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class ShowDiaryDialog extends BottomSheetDialogFragment implements ShowDiaryContract.View {

    private static final String TAG = "ShowDiaryDialog";

    private ShowDiaryContract.Presenter mPresenter;

    private BottomSheetBehavior mSheetBehavior;

    private ConstraintLayout mLayout;
    private ShowDiaryAdapter mShowDiaryAdapter;

    private List<Diary> mDiaries;


    public ShowDiaryDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mDatabase = DiaryDatabase.getIstance(getContext());
    }

    @Override
    public void setPresenter(ShowDiaryContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        setStyle(STYLE_NO_FRAME, R.style.ShowDiaryDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_popup, container, false);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mShowDiaryAdapter = new ShowDiaryAdapter(mPresenter,getContext());
        recyclerView.setAdapter(mShowDiaryAdapter);

        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void openDiaryDialogByPlace(Diary diary) {
//        if (mShowDiaryAdapter != null) {
//            Log.d(TAG,"showDialog adapter is null");
//
//            mShowDiaryAdapter = new ShowDiaryAdapter(mPresenter,getContext());
//            DiaryDAO diaryDAO = mDatabase.getDiaryDAO();
//
//            //mPlaces = diaryDAO.getDiarybyPlace(diary.getDiaryPlace());
//            Log.d(TAG,"how many diaries " + diaryDAO.getDiarybyPlace(diary.getDiaryPlace()));
//            mShowDiaryAdapter.showDiary(mDiaries);
//        } else {
//            Log.d(TAG,"showDialog adapter not null");
//        }
        DiaryDatabase diaryDatabase = DiaryDatabase.getIstance(getContext());


        Log.d(TAG,"how many diaries " + mDiaries.size());
        //mShowDiaryAdapter.showDiary(mDiaries);

    }
}
