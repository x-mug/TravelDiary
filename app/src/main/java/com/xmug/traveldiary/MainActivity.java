package com.xmug.traveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.xmug.traveldiary.base.BaseActivity;
import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.room.DiaryDatabase;
import com.xmug.traveldiary.edit.EditFragment;
import com.xmug.traveldiary.edit.EditPresenter;
import com.xmug.traveldiary.edit.weather.WeatherDialog;
import com.xmug.traveldiary.edit.weather.WeatherPresenter;
import com.xmug.traveldiary.mainpage.MainPageFragment;
import com.xmug.traveldiary.mainpage.MainPagePresenter;
import com.xmug.traveldiary.map.MapFragment;
import com.xmug.traveldiary.map.MapPresenter;
import com.xmug.traveldiary.map.showdiary.ShowDiaryDialog;
import com.xmug.traveldiary.map.showdiary.ShowDiaryPresenter;
import com.xmug.traveldiary.settings.SettingsFragment;
import com.xmug.traveldiary.settings.SettingsPresenter;
import com.xmug.traveldiary.settings.download.DownloadDialog;
import com.xmug.traveldiary.settings.download.DownloadPresenter;
import com.xmug.traveldiary.settings.font.FontDialog;
import com.xmug.traveldiary.settings.font.FontPresenter;
import com.xmug.traveldiary.settings.language.LanguageDialog;
import com.xmug.traveldiary.settings.language.LanguagePresenter;
import com.xmug.traveldiary.settings.sync.SyncDialog;
import com.xmug.traveldiary.settings.sync.SyncPresenter;
import com.xmug.traveldiary.util.Constants;
import com.xmug.traveldiary.util.UserManager;
import com.crashlytics.android.Crashlytics;
import com.facebook.internal.CallbackManagerImpl;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST = 100;

    private MainPagePresenter mMainPagePresenter;
    private EditPresenter mEditPresenter;
    private SettingsPresenter mSettingsPresenter;
    private MapPresenter mMapPresenter;
    private WeatherPresenter mWeatherPresenter;
    private ShowDiaryPresenter mShowDiaryPresenter;
    private SyncPresenter mSyncPresenter;
    private DownloadPresenter mDownloadPresenter;
    private FontPresenter mFontPresenter;
    private LanguagePresenter mLanguagePresenter;

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

    //empty diary
    private Diary mDiary;

    private boolean isDefaultLayout = true;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Crashlytics
        // Fabric.with(this, new Crashlytics());
        startActivity(new Intent(this, LaunchActivity.class));
        init();
    }

    private void init() {
        setContentView(R.layout.activity_main);
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
        mToolbarTitle = findViewById(R.id.tv_toolbar_title);
        mToolbarSearch = findViewById(R.id.searchView);
        mToolbarSearch.setOnQueryTextListener(onQueryTextListener);
        mToolbarSearch.setOnCloseListener(() -> {
            mMainPagePresenter.loadDiaryData();
            return false;
        });

        mToolbarBack = findViewById(R.id.imgBtn_back);
        mToolbarBack.setOnClickListener(this);

        mToolbarMenu = findViewById(R.id.imgBtn_menu);
        mToolbarMenu.setOnClickListener(this);

        mToolbarEdit = findViewById(R.id.btn_edit);
        mToolbarEdit.setOnClickListener(this);

        mToolbarDone = findViewById(R.id.btn_done);
        mToolbarDone.setOnClickListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                getSupportFragmentManager().popBackStack();
                break;
            case R.id.imgBtn_menu:
                openSettings();
                updateMapToolbar("");
                break;
            case R.id.btn_edit:
                clickEditDiary();
                mToolbarEdit.setVisibility(View.GONE);
                mToolbarDone.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_done:
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
            if (query.equals("")) {
                mMainPagePresenter.updateSearchStatus();
            } else {
                getTagsFromDb(query);
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (newText.equals("")) {
                mMainPagePresenter.updateSearchStatus();
            } else {
                getTagsFromDb(newText);
            }
            return true;
        }

        private void getTagsFromDb(String searchText) {
            searchText = "%" + searchText + "%";
            mMainPagePresenter.loadSearchData(searchText, searchText);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_main:
                openMainPage();
                if (isDefaultLayout) {
                    item.setIcon(R.mipmap.ic_linear_layout);
                    isDefaultLayout = false;
                    mMainPagePresenter.changeLayout(1);
                } else {
                    item.setIcon(R.mipmap.ic_dashboard);
                    isDefaultLayout = true;
                    mMainPagePresenter.changeLayout(0);
                }
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
        MainPageFragment mainPageFragment = (MainPageFragment) getSupportFragmentManager().findFragmentByTag(Constants.MAINPAGE);

        if (mainPageFragment == null) {
            mainPageFragment = MainPageFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, mainPageFragment, Constants.MAINPAGE).commit();
        mMainPagePresenter = new MainPagePresenter(mainPageFragment, DiaryDatabase.getIstance(this));
        mainPageFragment.setPresenter(mMainPagePresenter);

        showBottomNavigation();
    }


    public void openEdit(Diary diary) {
        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentByTag(Constants.EDIT);

        if (editFragment == null) {
            editFragment = EditFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, editFragment, Constants.EDIT).addToBackStack(null).commit();

            mEditPresenter = new EditPresenter(editFragment, DiaryDatabase.getIstance(this));
            editFragment.setPresenter(mEditPresenter);

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
                (WeatherDialog) (this.getSupportFragmentManager().findFragmentByTag(Constants.WEATHER));
        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentByTag(Constants.EDIT);

        if (dialog == null) {
            dialog = new WeatherDialog();
            mWeatherPresenter = new WeatherPresenter(dialog);
            dialog.setPresenter(mWeatherPresenter);
            dialog.setTargetFragment(editFragment, REQUEST);
            dialog.show((this.getSupportFragmentManager()), Constants.WEATHER);
        } else if (!dialog.isAdded()) {
            dialog.show(this.getSupportFragmentManager(), Constants.WEATHER);
        }
    }

    public void openShowDiaryOnMap(double lat, double lng) {
        ShowDiaryDialog dialog =
                (ShowDiaryDialog) (this.getSupportFragmentManager().findFragmentByTag(Constants.SHOWDIARYONMAP));

        if (dialog == null) {
            dialog = new ShowDiaryDialog();
            mShowDiaryPresenter = new ShowDiaryPresenter(dialog, DiaryDatabase.getIstance(this));
            mShowDiaryPresenter.loadDiaryByPlace(lat, lng);
            dialog.setPresenter(mShowDiaryPresenter);
            dialog.show((this.getSupportFragmentManager()), Constants.SHOWDIARYONMAP);
        } else if (!dialog.isAdded()) {
            dialog.show(this.getSupportFragmentManager(), Constants.SHOWDIARYONMAP);
        }
    }


    private void openMap() {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(Constants.MAP);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, mapFragment, Constants.MAP).addToBackStack(Constants.MAP).commit();
        mMapPresenter = new MapPresenter(mapFragment, DiaryDatabase.getIstance(this));
        mapFragment.setPresenter(mMapPresenter);

        hideBottomNavigation();
    }


    public void openSettings() {
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(Constants.SETTINGS);

        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, settingsFragment, Constants.SETTINGS).addToBackStack(null).commit();
        mSettingsPresenter = new SettingsPresenter(settingsFragment);
        settingsFragment.setPresenter(mSettingsPresenter);

        hideBottomNavigation();
    }

    public void openSyncDialog() {
        SyncDialog dialog =
                (SyncDialog) (this.getSupportFragmentManager().findFragmentByTag(Constants.SYNC));

        if (dialog == null) {
            dialog = new SyncDialog();
            mSyncPresenter = new SyncPresenter(dialog, DiaryDatabase.getIstance(this), FirebaseFirestore.getInstance(), FirebaseStorage.getInstance(),
                    FirebaseStorage.getInstance().getReferenceFromUrl(this.getResources().getString(R.string.firebase_storage)));
            dialog.setPresenter(mSyncPresenter);
            dialog.show((this.getSupportFragmentManager()), Constants.SYNC);
        } else if (!dialog.isAdded()) {
            dialog.show(this.getSupportFragmentManager(), Constants.SYNC);
        }
    }

    public void openDownloadDialog() {
        DownloadDialog dialog =
                (DownloadDialog) (this.getSupportFragmentManager().findFragmentByTag(Constants.DOWNLOAD));

        if (dialog == null) {
            dialog = new DownloadDialog();
            mDownloadPresenter = new DownloadPresenter(dialog, DiaryDatabase.getIstance(this), FirebaseFirestore.getInstance(), FirebaseStorage.getInstance(),
                    FirebaseStorage.getInstance().getReferenceFromUrl(this.getResources().getString(R.string.firebase_storage)));
            dialog.setPresenter(mDownloadPresenter);
            dialog.show((this.getSupportFragmentManager()), Constants.DOWNLOAD);
        } else if (!dialog.isAdded()) {
            dialog.show(this.getSupportFragmentManager(), Constants.DOWNLOAD);
        }
    }

    public void openFontDialog() {
        FontDialog dialog =
                (FontDialog) (this.getSupportFragmentManager().findFragmentByTag(Constants.FONT));

        if (dialog == null) {
            dialog = new FontDialog();
            mFontPresenter = new FontPresenter(dialog);
            dialog.setPresenter(mFontPresenter);
            dialog.show((this.getSupportFragmentManager()), Constants.FONT);
        } else if (!dialog.isAdded()) {
            dialog.show(this.getSupportFragmentManager(), Constants.FONT);
        }
    }

    public void openLanguageDialog() {
        LanguageDialog dialog =
                (LanguageDialog) (this.getSupportFragmentManager().findFragmentByTag(Constants.LANGUAGE));

        if (dialog == null) {
            dialog = new LanguageDialog();
            mLanguagePresenter = new LanguagePresenter(dialog);
            dialog.setPresenter(mLanguagePresenter);
            dialog.show((this.getSupportFragmentManager()), Constants.LANGUAGE);
        } else if (!dialog.isAdded()) {
            dialog.show(this.getSupportFragmentManager(), Constants.LANGUAGE);
        }
    }

    public void updateMapToolbar(String title) {

        if ("".equals(title)) {
            mToolbarTitle.setVisibility(View.GONE);
            mToolbarSearch.setVisibility(View.GONE);
            mToolbarBack.setVisibility(View.VISIBLE);
            mToolbarMenu.setVisibility(View.GONE);
            mToolbarEdit.setVisibility(View.GONE);
            mToolbarDone.setVisibility(View.GONE);

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

    public void sortDiaryByDate(List<Diary> diaries) {
        Collections.sort(diaries, new Comparator<Diary>() {
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ROOT);
            @Override
            public int compare(Diary o1, Diary o2) {
                try {
                    return dateFormat.parse(o1.getDate()).compareTo(dateFormat.parse(o2.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        Collections.reverse(diaries);
    }


    public void setFontType(TextView title, TextView date) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SharedPreferences sharedPreferences = getSharedPreferences("FONT", Context.MODE_PRIVATE);
            String fontType = sharedPreferences.getString("fontValue", "");
            switch (fontType) {
                case "allura":
                    mTypeface = getResources().getFont(R.font.allura_regular);
                    setTypefaceMid(mTypeface, title, date);
                    break;
                case "amatic":
                    mTypeface = getResources().getFont(R.font.amatic_regular);
                    setTypefaceBig(mTypeface, title, date);
                    break;
                case "blackjack":
                    mTypeface = getResources().getFont(R.font.blackjack);
                    setTypefaceMid(mTypeface, title, date);
                    break;
                case "brizel":
                    mTypeface = getResources().getFont(R.font.brizel);
                    setTypefaceMid(mTypeface, title, date);
                    break;
                case "dancing":
                    mTypeface = getResources().getFont(R.font.dancing_regular);
                    setTypeface(mTypeface, title, date);
                    break;
                case "farsan":
                    mTypeface = getResources().getFont(R.font.farsan_regular);
                    setTypefaceMid(mTypeface, title, date);
                    break;
                case "handwriting":
                    mTypeface = getResources().getFont(R.font.justan_regular);
                    setTypefaceBig(mTypeface, title, date);
                    break;
                case "kaushan":
                    mTypeface = getResources().getFont(R.font.kaushan_regular);
                    setTypeface(mTypeface, title, date);
                    break;
                case"default":
                    title.setTypeface(Typeface.SERIF);
                    date.setTypeface(Typeface.SERIF);
                    break;
            }
        }
    }

    private void setTypeface(Typeface mTypeface, TextView title, TextView date) {
        title.setTypeface(mTypeface);
        date.setTypeface(mTypeface);
        title.setTextSize(24);
        date.setTextSize(18);
    }

    private void setTypefaceMid(Typeface mTypeface, TextView title, TextView date) {
        title.setTypeface(mTypeface);
        date.setTypeface(mTypeface);
        title.setTextSize(26);
        date.setTextSize(20);
    }

    private void setTypefaceBig(Typeface mTypeface, TextView title, TextView date) {
        title.setTypeface(mTypeface);
        date.setTypeface(mTypeface);
        title.setTextSize(32);
        date.setTextSize(26);
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ) {
            getSupportFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_content)
                    .setIcon(R.mipmap.ic_sad_face)
                    .setPositiveButton(R.string.dialog_leave, (dialog, which) -> finish())
                    .setNegativeButton(R.string.dialog_stay,
                            (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
        }
    }
}
