package com.xmug.traveldiary.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmug.traveldiary.MainActivity;
import com.xmug.traveldiary.R;
import com.xmug.traveldiary.data.DiaryPlace;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class MapFragment extends Fragment implements OnMapReadyCallback, MapContract.View {

    private static final String TAG = "MapFragment";

    private GoogleMap mMap;

    private MapContract.Presenter mPresenter;


    public MapFragment() {}

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //map style
        try {
            // Customise the styling of the base map using a JSON object defined
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.mapstyle_retro));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mPresenter.loadDiaryOnMap();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).showBottomNavigation();
        ((MainActivity) getActivity()).updateMapToolbar(getResources().getString(R.string.toolbar_title));
    }


    @Override
    public void loadDiaryOnMapUi(List<DiaryPlace> places) {
        for (int i = 0; i < places.size(); i++ ) {
            LatLng location = new LatLng(places.get(i).getLat(), places.get(i).getLng());

            if (places.get(i).getLat() != 0.0 && places.get(i).getLng() != 0.0) {
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin))
                        .anchor(0.0f, 1.0f)
                        .title(places.get(i).getPlaceName()));

                mMap.setOnInfoWindowClickListener(marker -> {
                    Double latitude = marker.getPosition().latitude;
                    Double longitude = marker.getPosition().longitude;

                    ((MainActivity) getActivity()).openShowDiaryOnMap(latitude, longitude);
                });
            }
        }
    }
}
