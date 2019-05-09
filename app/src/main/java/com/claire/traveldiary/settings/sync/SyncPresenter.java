package com.claire.traveldiary.settings.sync;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


import com.claire.traveldiary.data.DeletedDiary;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.User;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class SyncPresenter implements SyncContract.Presenter {

    private static final String TAG = "SyncPresenter";

    private SyncContract.View mSyncView;

    private StorageReference mReference;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private DiaryDatabase mRoomDb;

    private List<Diary> mDiaries;
    private List<DeletedDiary> mDeletedDiaryList;
    private List<DiaryPlace> mPlaceList;
    private User mUser;

    public SyncPresenter(@NonNull SyncContract.View syncView, @NonNull DiaryDatabase roomDb,
                         @NonNull FirebaseFirestore firestore, @NonNull FirebaseStorage firebaseStorage,
                         @NonNull StorageReference storageReference) {
        mSyncView = checkNotNull(syncView, "syncView cannot be null!");
        mRoomDb = checkNotNull(roomDb, "roomDb cannot be null!");
        mFirestore = checkNotNull(firestore, "firestore cannot be null!");
        mStorage = checkNotNull(firebaseStorage, "firebaseStorage cannot be null!");
        mReference = checkNotNull(storageReference, "storageReference cannot be null!");
        mSyncView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void insertOrUpdateDataToFirebase() {
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
                mFirestore.collection("Users").document(userId).collection("Diaries").document(deletedId)
                        .delete()
                        .addOnSuccessListener(aVoid ->
                                Log.d(TAG, "successfully deleted diary from firebase!"))
                        .addOnFailureListener(e ->
                                Log.w(TAG, "Error deleting document", e));

                //delete places from firebase
                mFirestore.collection("Users").document(userId).collection("Places").document(deletedId)
                        .delete()
                        .addOnSuccessListener(aVoid ->
                                Log.d(TAG, "successfully deleted place from firebase!"))
                        .addOnFailureListener(e ->
                                Log.w(TAG, "Error deleting document", e));
            }
        } else {
            Log.d(TAG,"DeletedDiaryList is null ");
        }

        uploadDiaries(mDiaries, 0, userId, () ->
                uploadPlaces(mPlaceList, 0, userId, () -> {
                    mSyncView.successfullySyncUi();
                }));

    }

    private void uploadDiaries(List<Diary> diaryList, int i, String userId, UpLoadDiaryCallback upLoadDiaryCallback) {
        int j = i + 1;

        //upload images to storage
        ArrayList<String> images = new ArrayList<>();
        images = mDiaries.get(i).getImage();

        //int finalI = i;
        ArrayList<String> imageUrl = new ArrayList<>();

        uploadImage(images, imageUrl, 0, imagesUrl -> {
            Map<String, Object> diaries = new HashMap<>();
            diaries.put("id",mDiaries.get(i).getId());
            diaries.put("title",mDiaries.get(i).getTitle());
            diaries.put("date",mDiaries.get(i).getDate());
            diaries.put("place",mDiaries.get(i).getPlace());
            diaries.put("weather",mDiaries.get(i).getWeather());
            diaries.put("image", imagesUrl);
            diaries.put("content",mDiaries.get(i).getContent());
            diaries.put("tags",mDiaries.get(i).getTags());

            String diaryId = String.valueOf(mDiaries.get(i).getId());

            //sync all diaries to firebase
            mFirestore.collection("Users").document(userId).collection("Diaries").document(diaryId)
                    .set(diaries)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "All Diaries successfully written!");
                        if (j < diaryList.size()) {
                            uploadDiaries(diaryList, j, userId, upLoadDiaryCallback);
                        } else {
                            upLoadDiaryCallback.onCompleted();
                        }
                    })
                    .addOnFailureListener(e ->
                            Log.w(TAG, "Error writing document", e));
        });
    }

    private void uploadPlaces(List<DiaryPlace> diaryPlaceList, int i, String userId, UpLoadPlaceCallback upLoadPlaceCallback) {
        int j = i + 1;

        Map<String, Object> places = new HashMap<>();
        places.put("diaryId",mPlaceList.get(i).getDiaryId());
        places.put("placeId",mPlaceList.get(i).getPlaceId());
        places.put("placeName",mPlaceList.get(i).getPlaceName());
        places.put("country",mPlaceList.get(i).getCountry());
        places.put("lat",mPlaceList.get(i).getLat());
        places.put("lng",mPlaceList.get(i).getLng());

        String diaryId = String.valueOf(mPlaceList.get(i).getDiaryId());

        //sync all places to firebase
        mFirestore.collection("Users").document(userId).collection("Places").document(diaryId)
                .set(places)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "All Places successfully written!");
                    if (j < diaryPlaceList.size()) {
                        uploadPlaces(diaryPlaceList, j, userId, upLoadPlaceCallback);
                    } else {
                        upLoadPlaceCallback.onCompleted();
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error writing document", e));
    }

    private void uploadImage(ArrayList<String> images, ArrayList<String> imageUrl, int i, UpLoadImageCallback upLoadImageCallback) {
        int j = i + 1;

        if (images == null) {
            upLoadImageCallback.onCompleted(imageUrl);
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
                                    uploadImage(images, imageUrl, j, upLoadImageCallback);
                                } else {
                                    upLoadImageCallback.onCompleted(imageUrl);
                                }
                            });
                        }
                    });
        }
    }

    interface UpLoadImageCallback {

        void onCompleted(ArrayList<String> imagesUrl);
    }

    interface UpLoadDiaryCallback {

        void onCompleted();
    }

    interface UpLoadPlaceCallback {

        void onCompleted();
    }

    @Override
    public void start() {}
}
