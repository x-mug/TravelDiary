package com.claire.traveldiary.edit;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.claire.traveldiary.R;
import com.claire.traveldiary.TravelDiaryApplication;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mabbas007.tagsedittext.TagsEditText;


public class EditAdapter extends RecyclerView.Adapter {


    private static final String TAG = "EditAdapter";

    private RecyclerView.ViewHolder mHolder;

    private Diary mDiary;
    private Diary mEditDiary;

    private EditContract.Presenter mPresenter;
    private GalleryAdapter mGalleryAdapter;

    private Context mContext;
    private DiaryDatabase mDatabase;

    //view holder
    private TextView mLocation;
    private CardView mCardView;


    //data to room
    private String mEditTitle;
    private String mStringDate;
    private String mWeatherUri;
    private ArrayList<String> mImagesList;
    private String mEditContent;
    private List<String> mTagsList;

    private String mPlaceId;
    private String mPlaceName;
    private String mCountry;
    private double mLat;
    private double mLng;

    private boolean isEdit = false;
    private boolean isSearchPlace = false;


    public EditAdapter(EditContract.Presenter presenter, Context context, Diary diary) {
        mPresenter = presenter;
        mContext = context;
        mDiary = diary;
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecyclerGallery;
        private EditText mTitle;
        private TextView mDate;
        private ImageButton mWeather;
        private EditText mContent;
        private TagsEditText mTags;
        private TextView mShowTags;

        public EditViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecyclerGallery = itemView.findViewById(R.id.recycler_gallery);
            mLocation = itemView.findViewById(R.id.tv_my_location);
            mCardView = itemView.findViewById(R.id.autocomplete_card);
            mTitle = itemView.findViewById(R.id.edit_diary_title);
            mDate = itemView.findViewById(R.id.edit_diary_date);
            mContent = itemView.findViewById(R.id.edit_diary_content);
            mWeather = itemView.findViewById(R.id.choose_weather);
            mTags = itemView.findViewById(R.id.edit_tags);
            mShowTags = itemView.findViewById(R.id.show_tags);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EditViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_edit, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mHolder = holder;

        if (holder instanceof EditViewHolder) {

            if (isEdit == false) {
                if (mDiary != null) {
                    Log.d(TAG, "diary not null!" + mDiary.getTitle());

                    //can't editable
                    mCardView.setVisibility(View.GONE);
                    mLocation.setClickable(false);
                    ((EditViewHolder) holder).mTitle.setFocusableInTouchMode(false);
                    ((EditViewHolder) holder).mDate.setClickable(false);
                    ((EditViewHolder) holder).mWeather.setClickable(false);
                    ((EditViewHolder) holder).mContent.setFocusableInTouchMode(false);
                    ((EditViewHolder) holder).mTags.setVisibility(View.GONE);
                    ((EditViewHolder) holder).mShowTags.setVisibility(View.VISIBLE);


                    //images
                    if (mDiary.getImages() != null) {
                        mGalleryAdapter = new GalleryAdapter(mPresenter, mDiary.getImages());
                        ((EditViewHolder) holder).mRecyclerGallery.setLayoutManager(new LinearLayoutManager(TravelDiaryApplication.getAppContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        if ((((EditViewHolder) holder).mRecyclerGallery.getOnFlingListener() == null)) {
                            new LinearSnapHelper().attachToRecyclerView((((EditViewHolder) holder).mRecyclerGallery));
                        }
                        ((EditViewHolder) holder).mRecyclerGallery.setAdapter(mGalleryAdapter);
                    } else {
                        mGalleryAdapter = new GalleryAdapter(mPresenter, mImagesList);
                        ((EditViewHolder) holder).mRecyclerGallery.setLayoutManager(new LinearLayoutManager(TravelDiaryApplication.getAppContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        if ((((EditViewHolder) holder).mRecyclerGallery.getOnFlingListener() == null)) {
                            new LinearSnapHelper().attachToRecyclerView((((EditViewHolder) holder).mRecyclerGallery));
                        }
                        ((EditViewHolder) holder).mRecyclerGallery.setAdapter(mGalleryAdapter);
                    }

                    //title
                    ((EditViewHolder) holder).mTitle.setText(mDiary.getTitle());

                    //date
                    ((EditViewHolder) holder).mDate.setText(mDiary.getDate());

                    //weather
                    if (mDiary.getWeather() != null) {
                        ((EditViewHolder) holder).mWeather.setImageURI(Uri.parse(mDiary.getWeather()));
                    }

                    //places
                    if (mDiary.getDiaryPlace() != null) {
                        mLocation.setText(mDiary.getDiaryPlace().getPlaceName());
                    }

                    //content
                    ((EditViewHolder) holder).mContent.setText(mDiary.getContent());

                    //tags
                    if (mDiary.getTags() != null) {
                        Log.d(TAG, "tags size" + mDiary.getTags().size() + mDiary.getTags().toString());
                        //((EditViewHolder) holder).mTags.setTags(mDiary.getTags().toString().replace("[","").replace("]","").split("\\,"));
                        ((EditViewHolder) holder).mShowTags.setText(mDiary.getTags().toString().replace("[","").replace("]",""));
                    } else {
                        //((EditViewHolder) holder).mTags.setHint("");
                        ((EditViewHolder) holder).mShowTags.setText("");
                    }

                } else {
                    Log.d(TAG, "diary is null!");
                    Log.d(TAG, "new diary!");

                    //can edit
                    mLocation.setClickable(true);
                    ((EditViewHolder) holder).mTitle.setFocusableInTouchMode(true);
                    ((EditViewHolder) holder).mDate.setClickable(true);
                    //location can't input haven't done
                    ((EditViewHolder) holder).mContent.setFocusableInTouchMode(true);


                    //Gallery
                    mGalleryAdapter = new GalleryAdapter(mPresenter, mImagesList);
                    ((EditViewHolder) holder).mRecyclerGallery.setLayoutManager(new LinearLayoutManager(TravelDiaryApplication.getAppContext(),
                            LinearLayoutManager.HORIZONTAL, false));
                    if ((((EditViewHolder) holder).mRecyclerGallery.getOnFlingListener() == null)) {
                        new LinearSnapHelper().attachToRecyclerView((((EditViewHolder) holder).mRecyclerGallery));
                    }
                    ((EditViewHolder) holder).mRecyclerGallery.setAdapter(mGalleryAdapter);

                    //edit title
                    mEditTitle = ((EditViewHolder) holder).mTitle.getText().toString();

                    //Choose date
                    ((EditViewHolder) holder).mDate.setOnClickListener(v -> {
                        mPresenter.openDatePicker();
                    });
                    ((EditViewHolder) holder).mDate.setText(mStringDate);

                    //Choose Weather
                    ((EditViewHolder) holder).mWeather.setOnClickListener(v -> {
                        mPresenter.openWeatherDialog();
                    });
                    if (mWeatherUri == null) {
                        ((EditViewHolder) holder).mWeather.setImageResource(R.mipmap.ic_sunny);
                    } else {
                        ((EditViewHolder) holder).mWeather.setImageURI(Uri.parse(mWeatherUri));
                    }

                    //edit content
                    mEditContent = ((EditViewHolder) holder).mContent.getText().toString();

                    //Choose Location
                    mLocation.setOnClickListener(v -> {
                        mCardView.setVisibility(View.VISIBLE);
                        chooseLocation();
                    });

                    //set tags
                    ((EditViewHolder) holder).mTags.getTags();
                }

            } else {
                Log.d(TAG, "edit diary!" + mEditDiary.getId());

                //can edit
                ((EditViewHolder) holder).mTitle.setFocusableInTouchMode(true);
                ((EditViewHolder) holder).mDate.setClickable(true);
                ((EditViewHolder) holder).mWeather.setClickable(true);
                //location can't input haven't done
                ((EditViewHolder) holder).mContent.setFocusableInTouchMode(true);
                ((EditViewHolder) holder).mTags.setVisibility(View.VISIBLE);
                ((EditViewHolder) holder).mShowTags.setVisibility(View.GONE);


                //Gallery
                if (mEditDiary.getImages() != null) {
                    mGalleryAdapter = new GalleryAdapter(mPresenter, mEditDiary.getImages());
                    ((EditViewHolder) holder).mRecyclerGallery.setLayoutManager(new LinearLayoutManager(TravelDiaryApplication.getAppContext(),
                            LinearLayoutManager.HORIZONTAL, false));
                    if ((((EditViewHolder) holder).mRecyclerGallery.getOnFlingListener() == null)) {
                        new LinearSnapHelper().attachToRecyclerView((((EditViewHolder) holder).mRecyclerGallery));
                    }
                    ((EditViewHolder) holder).mRecyclerGallery.setAdapter(mGalleryAdapter);
                } else {
                    mGalleryAdapter = new GalleryAdapter(mPresenter, mImagesList);
                    ((EditViewHolder) holder).mRecyclerGallery.setLayoutManager(new LinearLayoutManager(TravelDiaryApplication.getAppContext(),
                            LinearLayoutManager.HORIZONTAL, false));
                    if ((((EditViewHolder) holder).mRecyclerGallery.getOnFlingListener() == null)) {
                        new LinearSnapHelper().attachToRecyclerView((((EditViewHolder) holder).mRecyclerGallery));
                    }
                    ((EditViewHolder) holder).mRecyclerGallery.setAdapter(mGalleryAdapter);
                }

                //edit title
                mEditTitle = ((EditViewHolder) holder).mTitle.getText().toString();

                //choose date
                ((EditViewHolder) holder).mDate.setOnClickListener(v -> {
                    mPresenter.openDatePicker();
                });
                if (mStringDate == null) {
                    ((EditViewHolder) holder).mTitle.setText(mEditDiary.getTitle());
                } else {
                    ((EditViewHolder) holder).mDate.setText(mStringDate);
                }

                //Choose Weather
                ((EditViewHolder) holder).mWeather.setOnClickListener(v -> {
                    mPresenter.openWeatherDialog();
                });
                if (mWeatherUri == null) {
                    ((EditViewHolder) holder).mWeather.setImageURI(Uri.parse(mEditDiary.getWeather()));
                } else {
                    ((EditViewHolder) holder).mWeather.setImageURI(Uri.parse(mWeatherUri));
                }

                //Choose Location
                chooseLocation();
                if (mEditDiary.getDiaryPlace() != null) {
                    mLocation.setText(mEditDiary.getDiaryPlace().getPlaceName());
                }

                //edit content
                mEditContent = ((EditViewHolder) holder).mContent.getText().toString();

                //edit tags
                ((EditViewHolder) holder).mTags.setTags(mEditDiary.getTags().toString().replace("[","").replace("]","").split("\\,"));
                mTagsList = ((EditViewHolder) holder).mTags.getTags();

            }
        }
    }


    public void updateWeather(String imageUri) {
        mWeatherUri = imageUri;
        notifyDataSetChanged();
    }

    public void updateImage(ArrayList<String> images) {
        mImagesList = images;
        notifyDataSetChanged();
    }

    public void updateDate(String date) {
        mStringDate = date;
        notifyDataSetChanged();
    }


    private void chooseLocation() {
        // Initialize Places.
        Places.initialize(mContext, "AIzaSyBJPVkA_f-Tp2PvzTWQ2p-_IOetu6g-9Q0");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(mContext);

        if (!Places.isInitialized()) {
            Places.initialize(TravelDiaryApplication.getAppContext(), "AIzaSyBJPVkA_f-Tp2PvzTWQ2p-_IOetu6g-9Q0");
        }

        AutocompleteSupportFragment supportFragment = (AutocompleteSupportFragment)
                ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        supportFragment.setHint("Search");
        supportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS));

        supportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng() + ", " + place.getAddressComponents());

                String getCountry = "";
                String getLocality = "";

                for (int i = 0; i < place.getAddressComponents().asList().size(); i++) {

                    if (place.getAddressComponents().asList().get(i).getTypes().get(0).equals("country")) {
                        getCountry = place.getAddressComponents().asList().get(i).getName();
                        Log.d(TAG,"get country" + getCountry);
                    }

                    if (place.getAddressComponents().asList().get(i).getTypes().get(0).equals("locality")) {
                        getLocality = place.getAddressComponents().asList().get(i).getName();
                        Log.d(TAG,"get locality" + getLocality);
                    }
                }

                mPlaceId = place.getId();
                mPlaceName = place.getName();
                mCountry = getCountry;
                mLat = place.getLatLng().latitude;
                mLng = place.getLatLng().longitude;

                mLocation.setText(place.getName());
                mCardView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void saveDiaryDataToRoom() {

        mEditTitle = ((EditViewHolder) mHolder).mTitle.getText().toString();
        mStringDate = ((EditViewHolder) mHolder).mDate.getText().toString();
        mEditContent = ((EditViewHolder) mHolder).mContent.getText().toString();
        mTagsList = ((EditViewHolder) mHolder).mTags.getTags();

        notifyDataSetChanged();

        //database
        mDatabase = DiaryDatabase.getIstance(mContext);
        DiaryDAO diaryDAO = mDatabase.getDiaryDAO();

        //random diary id
        Random random = new Random();
        int id = random.nextInt(10000000);

        //save diary to room
        Diary newOrUpdateDiary = new Diary();
        DiaryPlace diaryPlace = new DiaryPlace();

        if (mEditDiary != null) {
            Log.d(TAG, "have id");

            //Place object
            diaryPlace.setPlaceId(mEditDiary.getDiaryPlace().getPlaceId());
            diaryPlace.setDiaryId(mEditDiary.getDiaryPlace().getDiaryId());
            diaryPlace.setPlaceName(mEditDiary.getDiaryPlace().getPlaceName());
            diaryPlace.setCountry(mEditDiary.getDiaryPlace().getCountry());
            diaryPlace.setLat(mEditDiary.getDiaryPlace().getLat());
            diaryPlace.setLng(mEditDiary.getDiaryPlace().getLng());

            //diary object
            newOrUpdateDiary.setId(mEditDiary.getId());
            newOrUpdateDiary.setTitle(mEditTitle);
            newOrUpdateDiary.setDate(mStringDate);
            newOrUpdateDiary.setDiaryPlace(mEditDiary.getDiaryPlace());
            newOrUpdateDiary.setWeather(mWeatherUri);
            newOrUpdateDiary.setImages(mEditDiary.getImages());
            newOrUpdateDiary.setContent(mEditContent);
            newOrUpdateDiary.setTags(mTagsList);
        } else {
            Log.d(TAG, "no id");

            //Place object
            diaryPlace.setPlaceId(mPlaceId);
            diaryPlace.setDiaryId(id);
            diaryPlace.setPlaceName(mPlaceName);
            diaryPlace.setCountry(mCountry);
            diaryPlace.setLat(mLat);
            diaryPlace.setLng(mLng);

            //diary object
            newOrUpdateDiary.setId(id);
            newOrUpdateDiary.setTitle(mEditTitle);
            newOrUpdateDiary.setDate(mStringDate);
            newOrUpdateDiary.setDiaryPlace(diaryPlace);
            newOrUpdateDiary.setWeather(mWeatherUri);
            newOrUpdateDiary.setImages(mImagesList);
            newOrUpdateDiary.setContent(mEditContent);
            newOrUpdateDiary.setTags(mTagsList);

            Log.d(TAG, "tagssss" + newOrUpdateDiary.getTags());
        }

        diaryDAO.insertOrUpdateDiary(newOrUpdateDiary);
        diaryDAO.insertOrUpdatePlace(diaryPlace);
        showDiary(newOrUpdateDiary);

        Log.d(TAG, "Diary size" + diaryDAO.getAllDiaries().size());
        Log.d(TAG, "Place size" + diaryDAO.getAllPlaces().size());
    }


    public void showDiary(Diary diary) {
        Log.i(TAG, "Show! ");
        mDiary = diary;
        notifyDataSetChanged();

        isEdit = false;
    }


    public void editDiary(Diary diary) {
        Log.i(TAG, "Edit! ");
        mEditDiary = diary;
        notifyDataSetChanged();

        showDiary(mEditDiary);
        isEdit = true;
    }

    public void editNewDiary(Diary diary) {
        Log.i(TAG, "new Edit! ");
        mEditDiary = mDiary;
        notifyDataSetChanged();

        isEdit = true;
    }


    @Override
    public int getItemCount() {
            return 1;
    }


}
