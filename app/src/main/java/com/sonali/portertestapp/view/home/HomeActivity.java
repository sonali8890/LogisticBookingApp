package com.sonali.portertestapp.view.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sonali.portertestapp.AppConstants;
import com.sonali.portertestapp.R;
import com.sonali.portertestapp.databinding.ActivityHomeBinding;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;
import com.sonali.portertestapp.domain.model.Vehicle;
import com.sonali.portertestapp.view.LocationObserver;
import com.sonali.portertestapp.view.base.AbstractBaseActivity;
import com.sonali.portertestapp.view.base.BasePresentor;

import java.text.MessageFormat;

/**
 * Created by Sonali
 */

public class HomeActivity extends AbstractBaseActivity implements IHomePresentor.IHomeView, OnMapReadyCallback, View.OnClickListener {

    private HomePresentoImpl mPresentor;
    private ActivityHomeBinding mBinder;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFrament;
    private LocationObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = (ActivityHomeBinding) addChildLayout(R.layout.activity_home);
        attachPresentor(new HomePresentoImpl(this));
        initializeViews();
    }

    private void initializeViews() {
        showCenterLogo();
        mMapFrament = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMap);
        mMapFrament.getMapAsync(this);
        mBinder.fromTextTitle.setOnClickListener(this);
        mBinder.ToTextTitle.setOnClickListener(this);
        ActivityCompat.requestPermissions(this,
                new String[]{AppConstants.ACCESS_FINE_LOCATION}, AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

    }

    @Override
    public void attachPresentor(BasePresentor p) {
        mPresentor = (HomePresentoImpl) p;
        mPresentor.initialize();
    }

    @Override
    public void displayError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        mDialog.hide();
    }

    @Override
    public Activity getActivityContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.PICKUP_LOCATION_REQUEST || requestCode == AppConstants.DROP_LOCATION_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                GooglePlacesSearchResult.Prediction place = (GooglePlacesSearchResult.Prediction) data.getSerializableExtra(AppConstants.SELECTED_PLACE_INFO);
                updateSelectionPoint(place, requestCode == AppConstants.PICKUP_LOCATION_REQUEST);

            }
        }
        if (requestCode == AppConstants.PICKUP_LOCATION_REQUEST && resultCode == RESULT_OK && data != null) {
        } else if (requestCode == AppConstants.DROP_LOCATION_REQUEST && resultCode == RESULT_OK && data != null) {
        } else {
            observer.handleActivityResultData(requestCode, resultCode, data);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                showProgress();
                String latitude = String.valueOf(mGoogleMap.getCameraPosition().target
                        .latitude);
                String longitude = String.valueOf(mGoogleMap.getCameraPosition().target.longitude);
                mPresentor.getVehicalCostAndEta(latitude, longitude);
            }
        });

    }

    private void resetMapLocation(double latitude, double longitude) {
        mGoogleMap.clear();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            observer = new LocationObserver(HomeActivity.this, new LocationObserver.LocationUpdateListner() {
                @Override
                public void onLocationDenied() {
                }

                @Override
                public void onLocationUpdate(LocationResult result) {
                    observer.pauseLocationUpdate();
                    resetMapLocation(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude());
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (observer != null)
            observer.stopLocationUpdates();
    }

    @Override
    public void updateVehicleCost(Vehicle v) {
        hideProgress();
        if (v == null) {
            return;
        }
        if (!TextUtils.isEmpty(v.getLocationName())) {
            mBinder.fromTextView.setVisibility(View.VISIBLE);
            mBinder.fromTextView.setText(v.getLocationName());
        } else {
            mBinder.fromTextView.setVisibility(View.GONE);
        }

        mBinder.costTextView.setText(MessageFormat.format(getString(R.string.cost_text), new Object[]{v.getCost(), v.getEta()}));

    }

    @Override
    public void updateSelectionPoint(GooglePlacesSearchResult.Prediction place, boolean isPickupPointChanged) {
        if (isPickupPointChanged) {
            resetMapLocation(place.getLat(), place.getLng());
        } else if (!TextUtils.isEmpty(place.getDescription())) {
            mBinder.ToTextView.setVisibility(View.VISIBLE);
            mBinder.ToTextView.setText(place.getDescription());
        } else {
            mBinder.ToTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fromTextTitle) {
            mPresentor.handlePickupPointChangeRequest();
        } else if (view.getId() == R.id.ToTextTitle) {
            mPresentor.handleDropPointChangeRequest();
        } else {
            super.onClick(view);
        }
    }
}
