package com.sonali.portertestapp.domain.repository;

import com.sonali.portertestapp.MyApplication;
import com.sonali.portertestapp.domain.IRestApis;
import com.sonali.portertestapp.domain.model.Vehicle;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by Sonali
 */

public class CommonRepositoryImpl {

    @Inject
    @Named("BaseUrl")
    Retrofit retrofit;

    public CommonRepositoryImpl() {
        injectDependency();
    }

    public void injectDependency() {
        MyApplication.getApplicationInstance().getmNetworkComponent().inject(this);
    }

    public Observable<Vehicle> getVehicleCost(String lat, String longi) {
        IRestApis api = retrofit.create(IRestApis.class);
        return api.getVehicleCost(lat, longi)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Vehicle> getVehicleETA(String lat, String longi) {
        IRestApis api = retrofit.create(IRestApis.class);
        return api.getVehicleETA(lat, longi)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
