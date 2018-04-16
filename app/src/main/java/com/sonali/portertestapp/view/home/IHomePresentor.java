package com.sonali.portertestapp.view.home;

import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;
import com.sonali.portertestapp.domain.model.Vehicle;
import com.sonali.portertestapp.view.base.BasePresentor;
import com.sonali.portertestapp.view.base.BaseView;

/**
 * Created by Sonali
 */

public interface IHomePresentor extends BasePresentor {

    interface IHomeView extends BaseView {
        void updateVehicleCost(Vehicle v);
        void updateSelectionPoint(GooglePlacesSearchResult.Prediction place, boolean isPickupPointChanged);
    }

    void onDetach();
    void getVehicalCostAndEta(String lat, String lng);
    void handlePickupPointChangeRequest();
    void handleDropPointChangeRequest();
}
