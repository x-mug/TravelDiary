package com.claire.traveldiary;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.claire.traveldiary.base.BaseActivity;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.claire.traveldiary.edit.EditFragment;
import com.claire.traveldiary.edit.EditPresenter;
import com.claire.traveldiary.edit.chooseweather.WeatherDialog;
import com.claire.traveldiary.edit.chooseweather.WeatherPresenter;
import com.claire.traveldiary.mainpage.MainPageFragment;
import com.claire.traveldiary.mainpage.MainPagePresenter;
import com.claire.traveldiary.map.MapFragment;
import com.claire.traveldiary.map.MapPresenter;
import com.claire.traveldiary.map.showdiary.ShowDiaryDialog;
import com.claire.traveldiary.map.showdiary.ShowDiaryPresenter;
import com.claire.traveldiary.settings.SettingsFragment;
import com.claire.traveldiary.settings.SettingsPresenter;
import com.claire.traveldiary.settings.download.DownloadDialog;
import com.claire.traveldiary.settings.download.DownloadPresenter;
import com.claire.traveldiary.settings.sync.SyncDialog;
import com.claire.traveldiary.settings.sync.SyncPresenter;
import com.claire.traveldiary.util.UserManager;
import com.facebook.internal.CallbackManagerImpl;

import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final int REQUEST = 100;

    private MainPagePresenter mMainPagePresenter;
    private EditPresenter mEditPresenter;
    private SettingsPresenter mSettingsPresenter;
    private MapPresenter mMapPresenter;
    private WeatherPresenter mWeatherPresenter;
    private ShowDiaryPresenter mShowDiaryPresenter;
    private SyncPresenter mSyncPresenter;
    private DownloadPresenter mDownloadPresenter;

    //BottomNavigation
    private BottomNavigationView mBottomNavigation;

    //Toolbar
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private SearchView mToolbarSearch;
    private ImageButton mToolbarMenu;
    private ImageButton mToolbarBack;
    private Button mToolbarEdit;
    private Button mToolbarDone;

    private DiaryDatabase mDatabase;

    //empty diary
    private Diary mDiary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LaunchActivity.class));
        init();

    }

    private void init() {
        setContentView(R.layout.activity_main);
        mDatabase = DiaryDatabase.getIstance(this);
        setToolbar();
        setBottomNavigation();
        openMainPage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarSearch = findViewById(R.id.toolbar_search);
        mToolbarSearch.setOnQueryTextListener(onQueryTextListener);

        mToolbarBack = findViewById(R.id.toolbar_back);
        mToolbarBack.setOnClickListener(this);

        mToolbarMenu = findViewById(R.id.toolbar_menu);
        mToolbarMenu.setOnClickListener(this);

        mToolbarEdit = findViewById(R.id.toolbar_edit);
        mToolbarEdit.setOnClickListener(this);

        mToolbarDone = findViewById(R.id.toolbar_done);
        mToolbarDone.setOnClickListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toolbar_back:
                openMainPage();
                updateMapToolbar(getResources().getString(R.string.toolbar_title));
                break;
            case R.id.toolbar_menu:
                openSettings();
                updateMapToolbar("");
                break;
            case R.id.toolbar_edit:
                clickEditDiary();
                mToolbarEdit.setVisibility(View.GONE);
                mToolbarDone.setVisibility(View.VISIBLE);
                break;
            case R.id.toolbar_done:
                clickSaveDiary();
                mToolbarEdit.setVisibility(View.VISIBLE);
                mToolbarDone.setVisibility(View.GONE);
                break;
        }

    }

    private void setBottomNavigation() {
        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            getTagsFromDb(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            getTagsFromDb(newText);
            return true;
        }

        private void getTagsFromDb(String searchText) {
            searchText = "%" + searchText + "%";
            List<Diary> diaries = mDatabase.getDiaryDAO().getDiariesBySearch(searchText, searchText);
            if (diaries != null) {
                mMainPagePresenter.loadSearchData(diaries);
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_main:
                openMainPage();
                updateMapToolbar(getResources().getString(R.string.toolbar_title));
                return true;

            case R.id.navigation_edit:
                openEdit(mDiary);
                updateEditToolbar("");
                return true;

            case R.id.navigation_map:
                openMap();
                updateMapToolbar("");
                return true;

            default:
                return false;
        }
    };


    private void openMainPage() {
        MainPageFragment mainPageFragment = (MainPageFragment) getSupportFragmentManager().findFragmentByTag("MainPage");

        if (mainPageFragment == null) {
            MainPageFragment fragment = MainPageFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment, "MainPage").commit();
            mMainPagePresenter = new MainPagePresenter(fragment);
            fragment.setPresenter(mMainPagePresenter);
        }

        showBottomNavigation();
    }

    public void openEdit(Diary diary) {

        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentByTag("Edit");

        if (editFragment == null) {
            EditFragment fragment = EditFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment, "Edit").commit();
            mEditPresenter = new EditPresenter(fragment);
            fragment.setPresenter(mEditPresenter);

            if (diary == null) {
                updateEditToolbar("");
            } else {
                mEditPresenter.loadDiaryData(diary);
                updateEditToolbarFromMainPage("");
            }
        }
        hideBottomNavigation();

    }

    public void openWeatherDialog() {
        WeatherDialog dialog =
                (WeatherDialog) (this.getSupportFragmentManager().findFragmentByTag("WeatherDialog"));
        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentByTag("Edit");

        if (dialog == null) {

            dialog = new WeatherDialog();
            mWeatherPresenter = new WeatherPresenter(dialog);
            dialog.setPresenter(mWeatherPresenter);

            dialog.setTargetFragment(editFragment, REQUEST);
            dialog.show((this.getSupportFragmentManager()),"WeatherDialog");

        } else if (!dialog.isAdded()) {

            dialog.show(this.getSupportFragmentManager(), "WeatherDialog");
        }
    }

    public void openShowDiaryDialog(double lat, double lng) {
        ShowDiaryDialog dialog =
                (ShowDiaryDialog) (this.getSupportFragmentManager().findFragmentByTag("ShowDiaryDialog"));

        if (dialog == null) {

            dialog = new ShowDiaryDialog();
            mShowDiaryPresenter = new ShowDiaryPresenter(dialog);
            dialog.setPresenter(mShowDiaryPresenter);
            mShowDiaryPresenter.loadDiaryByPlace(lat, lng);

            dialog.show((this.getSupportFragmentManager()),"ShowDiaryDialog");

        } else if (!dialog.isAdded()) {

            dialog.show(this.getSupportFragmentManager(), "ShowDiaryDialog");
        }
    }


    private void openMap() {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("Map");

        if (mapFragment == null) {
            MapFragment fragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment, "Map").commit();
            mMapPresenter = new MapPresenter(fragment);
            fragment.setPresenter(mMapPresenter);
        }

        hideBottomNavigation();
    }


    private void openSettings() {

        //hideBottomNavigation();
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("Settings");

        if (settingsFragment == null) {
            SettingsFragment fragment = SettingsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment, "Settings").commit();
            mSettingsPresenter = new SettingsPresenter(fragment);
        }

        hideBottomNavigation();
    }

    public void openSyncDialog() {
        SyncDialog dialog =
                (SyncDialog) (this.getSupportFragmentManager().findFragmentByTag("SyncDialog"));

        if (dialog == null) {

            dialog = new SyncDialog();
            mSyncPresenter = new SyncPresenter(dialog);
            dialog.setPresenter(mSyncPresenter);

            dialog.show((this.getSupportFragmentManager()),"SyncDialog");

        } else if (!dialog.isAdded()) {

            dialog.show(this.getSupportFragmentManager(), "SyncDialog");
        }
    }

    public void openDownloadDialog() {
        DownloadDialog dialog =
                (DownloadDialog) (this.getSupportFragmentManager().findFragmentByTag("DownloadDialog"));

        if (dialog == null) {

            dialog = new DownloadDialog();
            mDownloadPresenter = new DownloadPresenter(dialog);
            dialog.setPresenter(mDownloadPresenter);

            dialog.show((this.getSupportFragmentManager()),"DownloadDialog");

        } else if (!dialog.isAdded()) {

            dialog.show(this.getSupportFragmentManager(), "DownloadDialog");
        }

    }

    private void updateMapToolbar(String title) {

        if ("".equals(title)) {
            mToolbarTitle.setVisibility(View.GONE);
            mToolbarSearch.setVisibility(View.GONE);
            mToolbarBack.setVisibility(View.VISIBLE);
            mToolbarMenu.setVisibility(View.GONE);


        } else {
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarSearch.setVisibility(View.VISIBLE);
            mToolbarBack.setVisibility(View.GONE);
            mToolbarMenu.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
            mToolbarDone.setVisibility(View.GONE);
            mToolbarEdit.setVisibility(View.GONE);

        }
    }

    public void updateEditToolbar(String title) {

        if ("".equals(title)) {
            mToolbarTitle.setVisibility(View.GONE);
            mToolbarSearch.setVisibility(View.GONE);
            mToolbarBack.setVisibility(View.VISIBLE);
            mToolbarMenu.setVisibility(View.GONE);
            mToolbarEdit.setVisibility(View.GONE);
            mToolbarDone.setVisibility(View.VISIBLE);

        } else {
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarSearch.setVisibility(View.VISIBLE);
            mToolbarBack.setVisibility(View.GONE);
            mToolbarMenu.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
            mToolbarDone.setVisibility(View.GONE);
            mToolbarEdit.setVisibility(View.GONE);
        }
    }

    public void updateEditToolbarFromMainPage(String title) {

        if ("".equals(title)) {
            mToolbarTitle.setVisibility(View.GONE);
            mToolbarSearch.setVisibility(View.GONE);
            mToolbarBack.setVisibility(View.VISIBLE);
            mToolbarMenu.setVisibility(View.GONE);
            mToolbarDone.setVisibility(View.GONE);
            mToolbarEdit.setVisibility(View.VISIBLE);

        } else {
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarSearch.setVisibility(View.VISIBLE);
            mToolbarBack.setVisibility(View.GONE);
            mToolbarMenu.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
            mToolbarDone.setVisibility(View.GONE);
            mToolbarEdit.setVisibility(View.GONE);
        }
    }


    public void hideBottomNavigation() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }

    public void clickSaveDiary() {
        mEditPresenter.clickSaveDiary();
    }

    public void clickEditDiary() {
        mEditPresenter.clickEditDiary();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }


}
