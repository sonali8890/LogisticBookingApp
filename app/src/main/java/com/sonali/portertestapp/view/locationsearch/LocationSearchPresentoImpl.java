package com.sonali.portertestapp.view.locationsearch;

import android.support.annotation.NonNull;

import com.sonali.portertestapp.MyApplication;
import com.sonali.portertestapp.domain.ApiCallbackWrapper;
import com.sonali.portertestapp.domain.model.GoogleLocationDataBean;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;
import com.sonali.portertestapp.domain.repository.GoogleRepositoryImpl;
import com.sonali.portertestapp.utility.PreferenceManager;
import com.sonali.portertestapp.utility.RecentsManager;
import com.sonali.portertestapp.view.base.BaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonali
 */
public class LocationSearchPresentoImpl implements ILocationSearchPresentor {

    private ILocationSearchPresentor.ISearchView mView;
    private CompositeDisposable mDisposable;
    private List<GooglePlacesSearchResult.Prediction> list = new ArrayList<>();

    @Inject
    GoogleRepositoryImpl repository;

    @Inject
    PreferenceManager mPref;

    public LocationSearchPresentoImpl(@NonNull BaseView view) {
        mView = (ISearchView) view;
        mDisposable = new CompositeDisposable();
    }


    @Override
    public void onDetach() {
        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

    @Override
    public List<GooglePlacesSearchResult.Prediction> getPlaceList() {
        return list;
    }

    @Override
    public void getLAtLngUsingPlaceId(final GooglePlacesSearchResult.Prediction place) {
        mView.showProgress();
        repository.getLatLngUsingPlaceId(place.getPlaceId())
                .subscribe(new SingleObserver<GoogleLocationDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(GoogleLocationDataBean googleLocationDataBean) {
                        mView.hideProgress();
                        googleLocationDataBean.setLatLong(place);
                        mView.sendResultBack(place);
                        RecentsManager.addRecentItem(mPref, place, mView.isPickupPointRequest());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.displayError(e.getMessage());
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void initialize() {
        MyApplication.getApplicationInstance().getmRepositoryComponent().inject(this);
        mDisposable.add(mView.getSearchObseravble()
//                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty()) {
                            mView.displayRecentSearch(true);
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .switchMap(new Function<String, ObservableSource<GooglePlacesSearchResult>>() {
                    @Override
                    public ObservableSource<GooglePlacesSearchResult> apply(String s) throws Exception {
                        return repository.getGooglePlaceSearch(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ApiCallbackWrapper<GooglePlacesSearchResult>(mView) {
                    @Override
                    protected void onSuccess(GooglePlacesSearchResult result) {
                        list.clear();
                        if (result.getPredictions() != null && !result.getPredictions().isEmpty()) {
                            list.addAll(result.getPredictions());
                        }
                        mView.invalidateAdapter();
                    }
                }));
    }


}
