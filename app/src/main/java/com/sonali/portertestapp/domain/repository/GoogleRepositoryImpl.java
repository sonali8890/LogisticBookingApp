package com.sonali.portertestapp.domain.repository;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.sonali.portertestapp.AppConstants;
import com.sonali.portertestapp.MyApplication;
import com.sonali.portertestapp.domain.IRestApis;
import com.sonali.portertestapp.domain.model.GoogleLocationDataBean;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Sonali
 */

public class GoogleRepositoryImpl {

    @Inject
    @Named("GoogleLocation")
    Retrofit googleLocationRetrofit;

    @Inject
    @Named("GooglePlaces")
    Retrofit GooglePlacesRetrofit;

    public GoogleRepositoryImpl() {
        injectDependency();
    }

    public void injectDependency() {
        MyApplication.getApplicationInstance().getmNetworkComponent().inject(this);
    }

    public Observable<String> getGLNameByGeoCoder(final Context mContext, final String latitudeStr, final String longtitudeStr) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String locationName = "";
                Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
                try {
                    if (!TextUtils.isEmpty(latitudeStr) && !TextUtils.isEmpty(longtitudeStr)) {
                        double latitude = Double.valueOf(latitudeStr);
                        double longtitude = Double.valueOf(longtitudeStr);
                        List<Address> addresses = null;
                        addresses = gcd.getFromLocation(latitude, longtitude, 2);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(addresses.size() - 1);
                            int maxIndex = address.getMaxAddressLineIndex();

                            for (int index = 0; index <= maxIndex; index++) {
                                locationName += addresses.get(addresses.size() - 1).getAddressLine(index) + "\n";
                            }
                            emitter.onNext(locationName);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<GooglePlacesSearchResult> getGooglePlaceSearch(String input) {
        IRestApis api = GooglePlacesRetrofit.create(IRestApis.class);
        return api.getGooglePlaces(AppConstants.GOOGLE_CONSOLE_API_KEY, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<GoogleLocationDataBean> getLatLngUsingPlaceId(String placeId) {
        IRestApis api = googleLocationRetrofit.create(IRestApis.class);
        return api.getLatLongUsingLocId(placeId, AppConstants.GOOGLE_CONSOLE_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
