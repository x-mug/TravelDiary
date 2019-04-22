package com.claire.traveldiary.settings.sync;

import android.content.Intent;
import android.os.Bundle;
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
import com.claire.traveldiary.data.User;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.claire.traveldiary.util.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class SyncDialog extends BottomSheetDialogFragment implements SyncContract.View {

    private static final String TAG = "SyncDialog";

    private SyncContract.Presenter mPresenter;

    private ConstraintLayout mLayout;

    private TextView mSyncTextView;
    private Button mSync;

    private FirebaseFirestore mFirebaseDb;
    private DiaryDatabase mRoomDb;

    private List<Diary> mDiaries;
    private User mUser;


    public SyncDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDb = FirebaseFirestore.getInstance();
        mRoomDb = DiaryDatabase.getIstance(getContext());

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

        mSync = dialogView.findViewById(R.id.btn_sync);
        mSyncTextView = dialogView.findViewById(R.id.tv_sync);

        //click sync
        if (UserManager.getInstance().isLoggedIn()) {
            mSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertOrUpdateDataToFirebase();
                }
            });
        } else {
            mSync.setClickable(false);
            mSyncTextView.setText(getActivity().getResources().getString(R.string.dialog_tv_sync));
            mSync.setTextColor(getActivity().getResources().getColor(R.color.white));
        }


        return dialogView;
    }


    private void insertOrUpdateDataToFirebase() {
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();
        mFirebaseDb = FirebaseFirestore.getInstance();
        mDiaries = diaryDAO.getAllDiaries();
        mUser = diaryDAO.getUser();
        String userId = String.valueOf(mUser.getId());

        //query all documents from db and delete not exist diaries from firebase
        mFirebaseDb.collection("Users").document(userId).collection("Diaries")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        //then save data to firebase
        for (int i = 0; i < mDiaries.size(); i++) {
            Map<String, Object> diaries = new HashMap<>();
            diaries.put("id",mDiaries.get(i).getId());
            diaries.put("title",mDiaries.get(i).getTitle());
            diaries.put("date",mDiaries.get(i).getDate());
            diaries.put("place",mDiaries.get(i).getDiaryPlace());
            diaries.put("weather",mDiaries.get(i).getWeather());
            diaries.put("image",mDiaries.get(i).getImages());
            diaries.put("content",mDiaries.get(i).getContent());
            diaries.put("tags",mDiaries.get(i).getTags());

            String diaryId = String.valueOf(mDiaries.get(i).getId());

            //sync all diaries to firebase
            mFirebaseDb.collection("Users").document(userId).collection("Diaries").document(diaryId)
                    .set(diaries)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "All Diaries successfully written!");
                        dismiss();
                    })
                    .addOnFailureListener(e ->
                            Log.w(TAG, "Error writing document", e));
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void dismiss() {
        //mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));
        new Handler().postDelayed(super::dismiss, 200);
    }

}
