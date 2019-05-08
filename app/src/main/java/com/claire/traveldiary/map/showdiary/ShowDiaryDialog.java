package com.claire.traveldiary.map.showdiary;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.claire.traveldiary.MainActivity;
import com.claire.traveldiary.R;
import com.claire.traveldiary.component.SpacesItemDecoration;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class ShowDiaryDialog extends BottomSheetDialogFragment implements ShowDiaryContract.View {

    private static final String TAG = "ShowDiaryDialog";

    private ShowDiaryContract.Presenter mPresenter;

    private ConstraintLayout mLayout;
    private ShowDiaryAdapter mShowDiaryAdapter;

    private List<DiaryPlace> mPlaceList;
    private List<Diary> mDiaryList;


    public ShowDiaryDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View dialogView = inflater.inflate(R.layout.dialog_showdiary, container, false);

        mLayout = dialogView.findViewById(R.id.layout_popup);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));


        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mShowDiaryAdapter = new ShowDiaryAdapter(mPresenter,getContext(),mDiaryList);
        recyclerView.setAdapter(mShowDiaryAdapter);
        recyclerView.setPadding(0,0,0,40);
        recyclerView.addItemDecoration(new SpacesItemDecoration(6));

        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void openDiaryDialogByPlace(double lat, double lng) {

        DiaryDatabase diaryDatabase = DiaryDatabase.getIstance(getContext());

        //find all diaries by placeName in placeObject
        mPlaceList = diaryDatabase.getDiaryDAO().getPlacebyLatlng(lat, lng);

        mDiaryList = new ArrayList<>();

        for (int i = 0; i < mPlaceList.size(); i++) {
            Diary diary = new Diary();
            diary  = diaryDatabase.getDiaryDAO().getDiarybyId(mPlaceList.get(i).getDiaryId());
            mDiaryList.add(diary);

            Log.d(TAG, "diary size: " + mDiaryList.size());
        }

        Log.d(TAG,"how many diaries in that place " + mPlaceList.size());
        Log.d(TAG, "diary place: " + mPlaceList.get(0).getPlaceName());

        if(mShowDiaryAdapter == null) {
            mShowDiaryAdapter = new ShowDiaryAdapter(mPresenter,getContext(),mDiaryList);
            mShowDiaryAdapter.showDiary(mDiaryList);
        } else {
            mShowDiaryAdapter.showDiary(mDiaryList);
        }
    }

    @Override
    public void openEditUi(Diary diary) {
        ((MainActivity) getActivity()).openEdit(diary);
    }

    @Override
    public void closePopupUi() {
        dismiss();
    }

    @Override
    public void setFontTypeUi(TextView title, TextView date) {
        ((MainActivity) getActivity()).setFontType(title, date);
    }

    @Override
    public void dismiss() {
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));
        new Handler().postDelayed(super::dismiss, 100);
    }


}
