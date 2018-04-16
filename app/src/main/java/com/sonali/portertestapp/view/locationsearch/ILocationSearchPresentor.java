package com.sonali.portertestapp.view.locationsearch;

import com.sonali.portertestapp.domain.model.GoogleLocationDataBean;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;
import com.sonali.portertestapp.view.base.BasePresentor;
import com.sonali.portertestapp.view.base.BaseView;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by Sonali
 */
public interface ILocationSearchPresentor extends BasePresentor {

    interface ISearchView extends BaseView {

        PublishSubject<String> getSearchObseravble();

        void invalidateAdapter();

        void onPlaceSelection(GooglePlacesSearchResult.Prediction place);

        void sendResultBack(GooglePlacesSearchResult.Prediction place);

        boolean isPickupPointRequest();

        void displayRecentSearch(boolean flag);
    }

    void onDetach();

    List<GooglePlacesSearchResult.Prediction> getPlaceList();

    void getLAtLngUsingPlaceId(GooglePlacesSearchResult.Prediction place);
}
