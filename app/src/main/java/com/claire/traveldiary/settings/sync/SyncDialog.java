package com.claire.traveldiary.settings.sync;

import android.net.Uri;
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
import android.widget.Toast;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.DeletedDiary;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.User;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.claire.traveldiary.util.UserManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.util.ArrayList;
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

    private StorageReference mReference;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirebaseDb;
    private DiaryDatabase mRoomDb;

    private List<Diary> mDiaries;
    private List<DeletedDiary> mDeletedDiaryList;
    private List<DiaryPlace> mPlaceList;
    private User mUser;


    public SyncDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDb = FirebaseFirestore.getInstance();
        mRoomDb = DiaryDatabase.getIstance(getContext());
        mStorage = FirebaseStorage.getInstance();
        mReference = mStorage.getReferenceFromUrl("gs://traveldiary-236516.appspot.com/");

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
        this.getDialog().setCanceledOnTouchOutside(true);

        mLayout = dialogView.findViewById(R.id.sync_popup);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        mSync = dialogView.findViewById(R.id.btn_sync);
        mSyncTextView = dialogView.findViewById(R.id.tv_sync);

        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //click sync
        if (UserManager.getInstance().isLoggedIn()) {
            mSync.setOnClickListener(v ->
                    insertOrUpdateDataToFirebase());
        } else {
            mSync.setClickable(false);
            mSyncTextView.setText(R.string.dialog_tv_sync);
            mSync.setTextColor(getActivity().getResources().getColor(R.color.white));
        }
    }


    private void insertOrUpdateDataToFirebase() {
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();
        mDiaries = diaryDAO.getAllDiaries();
        mDeletedDiaryList = diaryDAO.getAllDeletedDiariesId();
        mPlaceList = diaryDAO.getAllPlaces();
        mUser = diaryDAO.getUser();
        String userId = String.valueOf(mUser.getId());

        //delete not exist diaries from firebase
        if (mDeletedDiaryList != null) {
            for (int j = 0; j < mDeletedDiaryList.size(); j++) {
                String deletedId = String.valueOf(mDeletedDiaryList.get(j).getId());

                //remove from room
                diaryDAO.removeDeletedDiary(mDeletedDiaryList.get(j).getId());
                Log.d(TAG,"DeletedDiaryList size " + diaryDAO.getAllDeletedDiariesId().size());

                //delete diary from firebase
                mFirebaseDb.collection("Users").document(userId).collection("Diaries").document(deletedId)
                        .delete()
                        .addOnSuccessListener(aVoid ->
                                Log.d(TAG, "successfully deleted diary from firebase!"))
                        .addOnFailureListener(e ->
                                Log.w(TAG, "Error deleting document", e));

                //delete places from firebase
                mFirebaseDb.collection("Users").document(userId).collection("Places").document(deletedId)
                        .delete()
                        .addOnSuccessListener(aVoid ->
                                Log.d(TAG, "successfully deleted place from firebase!"))
                        .addOnFailureListener(e ->
                                Log.w(TAG, "Error deleting document", e));
            }
        } else {
            Log.d(TAG,"DeletedDiaryList is null ");
        }


        //then save new diaries to firebase
        for (int i = 0; i < mDiaries.size(); i++) {
            //upload images to storage
            ArrayList<String> images = new ArrayList<>();
            images = mDiaries.get(i).getImage();

            int finalI = i;
            ArrayList<String> imageUrl = new ArrayList<>();

            uploadImage(images, imageUrl, 0, new UpLoadCallback() {
                @Override
                public void onCompleted(ArrayList<String> imagesUrl) {
                    Map<String, Object> diaries = new HashMap<>();
                    diaries.put("id",mDiaries.get(finalI).getId());
                    diaries.put("title",mDiaries.get(finalI).getTitle());
                    diaries.put("date",mDiaries.get(finalI).getDate());
                    diaries.put("place",mDiaries.get(finalI).getPlace());
                    diaries.put("weather",mDiaries.get(finalI).getWeather());
                    diaries.put("image", imagesUrl);
                    diaries.put("content",mDiaries.get(finalI).getContent());
                    diaries.put("tags",mDiaries.get(finalI).getTags());

                    String diaryId = String.valueOf(mDiaries.get(finalI).getId());

                    //sync all diaries to firebase
                    mFirebaseDb.collection("Users").document(userId).collection("Diaries").document(diaryId)
                            .set(diaries)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "All Diaries successfully written!");

                            })
                            .addOnFailureListener(e ->
                                    Log.w(TAG, "Error writing document", e));
                }
            });
        }

        //then save new places to firebase
        for (int j = 0; j < mPlaceList.size(); j++) {
            String diaryId = String.valueOf(mDiaries.get(j).getId());
            Map<String, Object> places = new HashMap<>();
            places.put("diaryId",mPlaceList.get(j).getDiaryId());
            places.put("placeId",mPlaceList.get(j).getPlaceId());
            places.put("placeName",mPlaceList.get(j).getPlaceName());
            places.put("country",mPlaceList.get(j).getCountry());
            places.put("lat",mPlaceList.get(j).getLat());
            places.put("lng",mPlaceList.get(j).getLng());

            //sync all places to firebase
            mFirebaseDb.collection("Users").document(userId).collection("Places").document(diaryId)
                    .set(places)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "All Places successfully written!");
                        dismiss();
                        Toast.makeText(getContext(), "Successfully Sync!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Log.w(TAG, "Error writing document", e));
        }

    }

    private void uploadImage(ArrayList<String> images, ArrayList<String> imageUrl, int i, UpLoadCallback upLoadCallback) {
        int j = i + 1;

        if (images == null) {
            upLoadCallback.onCompleted(imageUrl);
        } else {
            Uri file = Uri.fromFile(new File(images.get(i)));
            StorageReference storageRef = mStorage.getReference().child(file.getLastPathSegment());
            storageRef.putFile(file)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String URL = uri.toString();
                                Log.d(TAG,"Image Url" + URL);
                                imageUrl.add(URL);
                                //This is your image url do whatever you want with it.
                                if (j < images.size()) {
                                    uploadImage(images, imageUrl, j, upLoadCallback);
                                } else {
                                    upLoadCallback.onCompleted(imageUrl);
                                }
                            });
                        }
                    });
        }
    }

    interface UpLoadCallback {

        void onCompleted(ArrayList<String> imagesUrl);
    }


    @Override
    public void dismiss() {
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));
        new Handler().postDelayed(super::dismiss, 200);
    }


}
