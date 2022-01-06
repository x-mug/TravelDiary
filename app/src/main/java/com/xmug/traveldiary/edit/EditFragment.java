package com.xmug.traveldiary.edit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xmug.traveldiary.MainActivity;
import com.xmug.traveldiary.R;
import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.edit.weather.WeatherDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static androidx.core.util.Preconditions.checkNotNull;

public class EditFragment extends Fragment implements EditContract.View{

    private static final String TAG = "EditFragment";

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 300;
    public static final int REQUEST = 100;
    private static final int PICK_IMAGE_MULTIPLE = 5;

    private EditContract.Presenter mPresenter;

    private RecyclerView mRecyclerEdit;
    private EditAdapter mEditAdapter;

    //gallery
    private ArrayList<String> mImagesList;
    String imageEncoded;
    ArrayList<String> imagesEncodedList;

    private Diary mDiary;
    private Diary mNewDiary;

    public EditFragment() {
    }

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(EditContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditAdapter = new EditAdapter(mPresenter,getContext(),mDiary);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        mRecyclerEdit = root.findViewById(R.id.recycler_edit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerEdit.setLayoutManager(layoutManager);
        mRecyclerEdit.setAdapter(mEditAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditAdapter.showDiary(mDiary);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode);

        //receive weather data
        if (requestCode == REQUEST && resultCode == RESULT_OK && null != data) {
            String imageName = data.getStringExtra(WeatherDialog.IMAGE);
            Log.d(TAG, "weather icon uri: " + imageName);
            mEditAdapter.updateWeather(imageName);
        } else {
            Log.d(TAG, "You haven't choose weather" );
        }

        //receive image data
        try {
             //When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

                if(data.getData()!=null) {
                    mImagesList = new ArrayList<String>();

                    // Get the Image from data
                    Uri mImageUri = data.getData();
                    Log.d(TAG, "onActivityResult: " + mImageUri);

                    //real path
                    String realPath = getRealPathFromURI(mImageUri);
                    Log.d(TAG, "I get real path" + realPath);

                    mImagesList.add(realPath);
                    mEditAdapter.updateImage(mImagesList);
                }

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    ArrayList<String> mArrayPath = new ArrayList<String>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        String realPath = getRealPathFromURI(uri);

                        mArrayPath.add(realPath);
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        imagesEncodedList = new ArrayList<String>();
                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded  = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();
                    }
                    Log.d(TAG, "Selected Images" + mArrayPath.size());
                    mEditAdapter.updateImage(mArrayPath);
                }

            } else {
                Log.d(TAG, "You haven't picked Image" );
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong.....", Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.autocomplete_fragment);

        if(fragment != null && getActivity() != null && !getActivity().isFinishing()) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void openWeatherDialogUi() {
        ((MainActivity) getActivity()).openWeatherDialog();
    }


    @Override
    public void openGalleryUi() {

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        openGalleryAfterPermission();
    }

    private void openGalleryAfterPermission() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @Override
    public void openDatePickerUi() {
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog, (view, year, month, dayOfMonth) -> {

            String monthEng = "";

            if ((month+1) == 1) {
                monthEng = "January";
            }
            if ((month+1) == 2) {
                monthEng = "February";
            }
            if ((month+1) == 3) {
                monthEng = "March";
            }
            if ((month+1) == 4) {
                monthEng = "April";
            }
            if ((month+1) == 5) {
                monthEng = "May";
            }
            if ((month+1) == 6) {
                monthEng = "June";
            }
            if ((month+1) == 7) {
                monthEng = "July";
            }
            if ((month+1) == 8) {
                monthEng = "August";
            }
            if ((month+1) == 9) {
                monthEng = "September";
            }
            if ((month+1) == 10) {
                monthEng = "October";
            }
            if ((month+1) == 11) {
                monthEng = "November";
            }
            if ((month+1) == 12) {
                monthEng = "December";
            }

            String date = dayOfMonth + " " + monthEng + " " + year;
            mEditAdapter.updateDate(date);
        },yy, mm, dd);

        pickerDialog.show();
    }

    @Override
    public void clickSaveDiaryUi() {
        mEditAdapter.saveDiaryDataToRoom();
    }

    @Override
    public void openEditPageUi(Diary diary) {
        mDiary = diary;
        if(mEditAdapter == null) {
            mEditAdapter = new EditAdapter(mPresenter,getContext(),diary);
            mEditAdapter.showDiary(diary);
        } else {
            mEditAdapter.showDiary(diary);
        }
    }

    @Override
    public void clickEditDiaryUi() {
        if (mDiary != null) {
            mEditAdapter.editDiary(mDiary);
            Log.d(TAG,"edit diary by id ! " + mDiary.getId());
        } else {
            mEditAdapter.editNewDiary(mNewDiary);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            ((MainActivity) getActivity()).updateMapToolbar("");
        } else {
            ((MainActivity) getActivity()).showBottomNavigation();
            ((MainActivity) getActivity()).updateMapToolbar(getResources().getString(R.string.toolbar_title));
        }
    }
}
