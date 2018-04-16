package com.sonali.portertestapp.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sonali.portertestapp.AppConstants;

import io.reactivex.subjects.PublishSubject;


/**
 * Created by Housejoy on 30-07-2017.
 */

public class LocationObserver {

    String TAG = "Log";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private AppCompatActivity mContext;
    LocationUpdateListner listner;
    private boolean isLocationUpdatePaused = false;

    public interface LocationUpdateListner{
        void onLocationDenied();
        void onLocationUpdate(LocationResult result);
    }

    public LocationObserver(Context con, LocationUpdateListner listener){
        mContext = (AppCompatActivity) con;
        this.listner = listener;
        createLocationCallback();
        checkGPSSettings();
    }


    public void checkGPSSettings() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((mContext), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        requestForLocationUpdate();
                    }
                })
                .addOnFailureListener(mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(mContext, AppConstants.REQUEST_GPS_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    listner.onLocationDenied();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                listner.onLocationDenied();
                                break;
                        }

                    }
                });
    }

    @SuppressLint("MissingPermission")
    public void requestForLocationUpdate(){
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
    }


    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(!isLocationUpdatePaused)
                listner.onLocationUpdate(locationResult);
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void pauseLocationUpdate(){
        isLocationUpdatePaused = true;
    }

    public void stopLocationUpdates() {
        if(mLocationCallback == null || mFusedLocationClient == null)
            return;

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(mContext, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    public void handleActivityResultData(int requestCode, int resultCode, Intent data){
        switch (resultCode) {
            case Activity.RESULT_OK:
                requestForLocationUpdate();
                break;
            case Activity.RESULT_CANCELED:
                listner.onLocationDenied();
                break;
        }
    }
}
