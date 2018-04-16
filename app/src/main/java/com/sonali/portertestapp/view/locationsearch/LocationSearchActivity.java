package com.sonali.portertestapp.view.locationsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.sonali.portertestapp.AppConstants;
import com.sonali.portertestapp.MyApplication;
import com.sonali.portertestapp.R;
import com.sonali.portertestapp.databinding.ActivityLocationSearchBinding;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;
import com.sonali.portertestapp.utility.PreferenceManager;
import com.sonali.portertestapp.utility.RecentsManager;
import com.sonali.portertestapp.view.base.AbstractBaseActivity;
import com.sonali.portertestapp.view.base.BasePresentor;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;

public class LocationSearchActivity extends AbstractBaseActivity implements ILocationSearchPresentor.ISearchView {

    private LocationSearchPresentoImpl mPresentor;
    private ActivityLocationSearchBinding mBinder;
    private PublishSubject<String> mObserver = PublishSubject.create();
    private boolean isPickupPointRequest;
    String TAG = "Log";

    @Inject
    PreferenceManager mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = (ActivityLocationSearchBinding)addChildLayout(R.layout.activity_location_search);
        attachPresentor(new LocationSearchPresentoImpl(this));
        MyApplication.getApplicationInstance().getmRepositoryComponent().inject(this);
        isPickupPointRequest = getIntent().getBooleanExtra(AppConstants.IS_PICKUP_POINT_REQUEST, false);
        initialize();

    }

    private void initialize() {
        showTitle(isPickupPointRequest ? getString(R.string.enter_pickup_location) : getString(R.string.enter_drop_location));
        mBinder.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinder.recyclerView.setAdapter(new PlacesAdapter(this, mPresentor.getPlaceList(), false));
        mBinder.recentItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinder.recentItemRecyclerView.setAdapter(new PlacesAdapter(this, RecentsManager.getRecentItemList(mPref, isPickupPointRequest), true));
        displayRecentSearch(true);
        mBinder.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                                                                @Override
                                                                public void onTextChanged(CharSequence text, int start, int before, int count) {
                                                                    mObserver.onNext(text.toString());
                                                                }

                                                                @Override
                                                                public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                                                                }

                                                                @Override
                                                                public void afterTextChanged(Editable s) {
                                                                }
                                                            }
        );
    }


    @Override
    public void attachPresentor(BasePresentor p) {
        mPresentor = (LocationSearchPresentoImpl) p;
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
    public PublishSubject<String> getSearchObseravble() {
        return mObserver;
    }

    @Override
    public void invalidateAdapter() {
        mBinder.recyclerView.getAdapter().notifyDataSetChanged();
        displayRecentSearch(false);
    }

    @Override
    public void onPlaceSelection(GooglePlacesSearchResult.Prediction place) {
        mPresentor.getLAtLngUsingPlaceId(place);
    }

    @Override
    public void sendResultBack(GooglePlacesSearchResult.Prediction place) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.SELECTED_PLACE_INFO, place);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean isPickupPointRequest() {
        return isPickupPointRequest;
    }

    @Override
    public void displayRecentSearch(boolean flag) {
        mBinder.recentItemRecyclerView.setVisibility(flag ? View.VISIBLE : View.GONE);
        mBinder.recyclerView.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresentor.onDetach();
    }
}
