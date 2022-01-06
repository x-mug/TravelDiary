package com.xmug.traveldiary.settings.download;

import androidx.annotation.NonNull;
import android.util.Log;

import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.DiaryPlace;
import com.xmug.traveldiary.data.User;
import com.xmug.traveldiary.data.room.DiaryDAO;
import com.xmug.traveldiary.data.room.DiaryDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class DownloadPresenter implements DownloadContract.Presenter {
    private static final String TAG = "DownloadPresenter";

    private DownloadContract.View mDownloadView;

    private StorageReference mReference;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private DiaryDatabase mRoomDb;

    private User mUser;

    public DownloadPresenter(@NonNull DownloadContract.View downloadView, @NonNull DiaryDatabase roomDb,
                             @NonNull FirebaseFirestore firestore, @NonNull FirebaseStorage firebaseStorage,
                             @NonNull StorageReference storageReference) {
        mDownloadView = checkNotNull(downloadView, "downloadView cannot be null!");
        mRoomDb = checkNotNull(roomDb, "roomDb cannot be null!");
        mFirestore = checkNotNull(firestore, "firestore cannot be null!");
        mStorage = checkNotNull(firebaseStorage, "firebaseStorage cannot be null!");
        mReference = checkNotNull(storageReference, "storageReference cannot be null!");
        mDownloadView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void ReadDataFromFirebase() {
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();
        mUser = diaryDAO.getUser();
        String userId = String.valueOf(mUser.getId());


        //query users all diaries and save to roomdb
        mFirestore.collection("Users").document(userId).collection("Diaries")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if(task.getResult().isEmpty()) {
                                mDownloadView.noDataInFirebaseUi();
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Diary diary = document.toObject(Diary.class);
                                    diaryDAO.insertOrUpdateDiary(diary);
                                    Log.d(TAG, "diary size : " + diaryDAO.getAllDiaries().size());

                                    //update image to local
                                    ArrayList<String> imageUrl = (ArrayList<String>) document.get("image");
                                    ArrayList<String> imageLocalPath = new ArrayList<>();
                                    downloadImage(imageUrl, imageLocalPath, 0, image ->
                                            diaryDAO.updateImageFromFirebase(image, Integer.parseInt(document.getId())));
                                    mDownloadView.successfullyDownloadUi();
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //query users all places and save to roomdb
        mFirestore.collection("Users").document(userId).collection("Places")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DiaryPlace diaryPlace = document.toObject(DiaryPlace.class);
                                diaryDAO.insertOrUpdatePlace(diaryPlace);
                                Log.d(TAG, "place size : " + diaryDAO.getAllPlaces().size());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void downloadImage(ArrayList<String> imageUrl, ArrayList<String> imageLocalPath, int i, DownloadImageCallback downloadImageCallback) {
        int j = i + 1;

        if (imageUrl.size() == 0) {
            downloadImageCallback.onCompleted(imageLocalPath);
        } else {
            StorageReference reference = mStorage.getReferenceFromUrl(imageUrl.get(i));
            try {
                File localFile = File.createTempFile("images", "jpg");
                reference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    String imageFullPath = localFile.getAbsolutePath();
                    imageLocalPath.add(imageFullPath);
                    Log.d(TAG, "image download from firebase " + imageFullPath);
                    if (j < imageUrl.size()) {
                        downloadImage(imageUrl, imageLocalPath, j, downloadImageCallback);
                    } else {
                        downloadImageCallback.onCompleted(imageLocalPath);
                    }
                }).addOnFailureListener(exception -> {
                    Log.d(TAG, "image download fail ");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    interface DownloadImageCallback {
        void onCompleted(ArrayList<String> image);
    }

    @Override
    public void start() {}
}
