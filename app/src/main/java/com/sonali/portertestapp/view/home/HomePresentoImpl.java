package com.sonali.portertestapp.view.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.sonali.portertestapp.AppConstants;
import com.sonali.portertestapp.MyApplication;
import com.sonali.portertestapp.domain.model.Vehicle;
import com.sonali.portertestapp.domain.repository.CommonRepositoryImpl;
import com.sonali.portertestapp.domain.repository.GoogleRepositoryImpl;
import com.sonali.portertestapp.view.base.BaseView;
import com.sonali.portertestapp.view.locationsearch.LocationSearchActivity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;

/**
 * Created by Sonali
 */

public class HomePresentoImpl implements IHomePresentor {

    private IHomeView mView;
    @Inject
    GoogleRepositoryImpl repository;

    @Inject
    CommonRepositoryImpl commonRepo;

    public HomePresentoImpl(@NonNull BaseView view) {
        mView = (IHomeView) view;
    }

    public Observer<Vehicle> getSubscriber() {
        return new Observer<Vehicle>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(Vehicle vehicle) {
                mView.updateVehicleCost(vehicle);
            }

            @Override
            public void onError(Throwable e) {
                mView.updateVehicleCost(null);
            }

            @Override
            public void onComplete() {
            }
        };
    }

    @SuppressLint("CheckResult")
    @Override
    public void getVehicalCostAndEta(String lat, String lng) {
        Observable.zip(repository.getGLNameByGeoCoder(mView.getActivityContext(), lat, lng),
                commonRepo.getVehicleCost(lat, lng),
                commonRepo.getVehicleETA(lat, lng), new Function3<String, Vehicle, Vehicle, Vehicle>() {
                    @Override
                    public Vehicle apply(String locationName, Vehicle cost, Vehicle eta) throws Exception {
                        if (cost != null && eta != null) {
                            cost.setEta(eta.getEta());
                            cost.setLocationName(locationName);
                            return cost;
                        }
                        return null;
                    }
                }).subscribeWith(getSubscriber());
    }

    @Override
    public void handlePickupPointChangeRequest() {
        Intent intent = new Intent(mView.getActivityContext(), LocationSearchActivity.class);
        intent.putExtra(AppConstants.IS_PICKUP_POINT_REQUEST, true);
        mView.getActivityContext().startActivityForResult(intent, AppConstants.PICKUP_LOCATION_REQUEST);
    }

    @Override
    public void handleDropPointChangeRequest() {
        Intent intent = new Intent(mView.getActivityContext(), LocationSearchActivity.class);
        intent.putExtra(AppConstants.IS_PICKUP_POINT_REQUEST, false);
        mView.getActivityContext().startActivityForResult(intent, AppConstants.DROP_LOCATION_REQUEST);
    }

    @Override
    public void initialize() {
        MyApplication.getApplicationInstance().getmRepositoryComponent().inject(this);

    }


    @Override
    public void onDetach() {
    }
}
