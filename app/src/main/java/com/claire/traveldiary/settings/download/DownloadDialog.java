package com.claire.traveldiary.settings.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.TextView;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.User;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.claire.traveldiary.util.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class DownloadDialog extends BottomSheetDialogFragment implements DownloadContract.View {

    private static final String TAG = "DownloadDialog";

    private DownloadContract.Presenter mPresenter;

    private FirebaseFirestore mFirebaseDb;
    private DiaryDatabase mRoomDb;

    private ConstraintLayout mLayout;
    private TextView mDownloadTextView;
    private TextView mDownloadNotice;
    private Button mDownload;

    private User mUser;

    public DownloadDialog() {
    }

    @Override
    public void setPresenter(DownloadContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        setStyle(STYLE_NO_FRAME, R.style.ShowDiaryDialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDb = FirebaseFirestore.getInstance();
        mRoomDb = DiaryDatabase.getIstance(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_download, container, false);

        mLayout = dialogView.findViewById(R.id.download_popup);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        mDownload = dialogView.findViewById(R.id.btn_download);
        mDownloadTextView = dialogView.findViewById(R.id.tv_download);
        mDownloadNotice = dialogView.findViewById(R.id.download_notice);


        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //click download
        if (UserManager.getInstance().isLoggedIn()) {
            mDownloadNotice.setVisibility(View.VISIBLE);
            mDownload.setOnClickListener(v ->{
                ReadDataFromFirebase();
                    });
        } else {
            mDownload.setClickable(false);
            mDownloadTextView.setText(getActivity().getResources().getString(R.string.dialog_tv_download));
            mDownload.setTextColor(getActivity().getResources().getColor(R.color.white));
            mDownloadNotice.setVisibility(View.GONE);
        }
    }

    private void ReadDataFromFirebase() {
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();
        mUser = diaryDAO.getUser();
        String userId = String.valueOf(mUser.getId());


        //query users all diary and save to roomdb
        mFirebaseDb.collection("Users").document(userId).collection("Diaries")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    //save image to local
                                    Diary diary = document.toObject(Diary.class);
                                    for (int i = 0; i < diary.getImage().size(); i++) {

                                    }

                                    diaryDAO.insertOrUpdateDiary(diary);
                                    Log.d(TAG, "diary size : " + diaryDAO.getAllDiaries().size());

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });

        //then query users all place and save to roomdb
        mFirebaseDb.collection("Users").document(userId).collection("Places")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    DiaryPlace diaryPlace = document.toObject(DiaryPlace.class);
                                    diaryDAO.insertOrUpdatePlace(diaryPlace);
                                    Log.d(TAG, "place size : " + diaryDAO.getAllPlaces().size());
                                    dismiss();
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });
    }



    @Override
    public void dismiss() {
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));
        new Handler().postDelayed(super::dismiss, 200);
    }
}
