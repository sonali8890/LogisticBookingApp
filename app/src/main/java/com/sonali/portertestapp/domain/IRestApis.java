package com.sonali.portertestapp.domain;

import com.sonali.portertestapp.domain.model.GoogleLocationDataBean;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;
import com.sonali.portertestapp.domain.model.UserModel;
import com.sonali.portertestapp.domain.model.Vehicle;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sonali
 */

/**
 * Created by Sonali
 */
public interface IRestApis {

    @GET("vehicles/cost")
    Observable<Vehicle> getVehicleCost(@Query("lat") String lat, @Query("lng") String lng);

    @GET("vehicles/eta")
    Observable<Vehicle> getVehicleETA(@Query("lat") String lat, @Query("lng") String lng);

    @GET("autocomplete/json")
    Observable<GooglePlacesSearchResult> getGooglePlaces(@Query("key") String key, @Query("input") String input);

    @GET("place/details/json")
    Single<GoogleLocationDataBean> getLatLongUsingLocId(@Query("placeid") String placeid, @Query("key") String key);



}
