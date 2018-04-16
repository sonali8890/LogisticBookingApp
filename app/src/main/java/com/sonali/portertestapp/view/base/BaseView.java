package com.sonali.portertestapp.view.base;

import android.app.Activity;

/**
 * Created by Sonali
 */
public interface BaseView {

    void attachPresentor(BasePresentor p);
    void displayError(String error);
    void showProgress();
    void hideProgress();
    Activity getActivityContext();
}
