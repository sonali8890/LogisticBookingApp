package com.sonali.portertestapp.utility;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonali
 */

public class RecentsManager {


    public static void addRecentItem(PreferenceManager perf, GooglePlacesSearchResult.Prediction place
            , boolean isPickupPoint) {
        if (place == null) {
            return;
        }

        ArrayList<GooglePlacesSearchResult.Prediction> recentItemList = getRecentItemList(perf, isPickupPoint);
        if (!recentItemList.isEmpty()) {
            if (recentItemList.contains(place)) {
                recentItemList.remove(place);
            }
            recentItemList.add(0, place);
        } else {
            recentItemList.add(place);
        }
        updateListInToPreference(perf, recentItemList, isPickupPoint);

    }

    public static ArrayList<GooglePlacesSearchResult.Prediction> getRecentItemList(PreferenceManager perf, boolean isPickupPoint) {
        Type listType = new TypeToken<List<GooglePlacesSearchResult.Prediction>>() {
        }.getType();

        ArrayList<GooglePlacesSearchResult.Prediction> recentList = null;
        String recentString = isPickupPoint ? perf.getString(perf.RECENT_PICKUP_POINT_SEARCH, null) :
                perf.getString(perf.RECENT_DROP_POINT_SEARCH, null);
        if (!TextUtils.isEmpty(recentString)) {
            recentList = new Gson().fromJson(recentString, listType);
        } else {
            recentList = new ArrayList<>();
            updateListInToPreference(perf, recentList, isPickupPoint);
        }
        return recentList;
    }


    private static void updateListInToPreference(PreferenceManager perf, ArrayList<GooglePlacesSearchResult.Prediction> list, boolean isPickupPoint) {
        Type listType = new TypeToken<ArrayList<GooglePlacesSearchResult.Prediction>>() {
        }.getType();
        if(isPickupPoint) {
            perf.setString(perf.RECENT_PICKUP_POINT_SEARCH, new Gson().toJson(list, listType));
        }
        else{
            perf.setString(perf.RECENT_DROP_POINT_SEARCH, new Gson().toJson(list, listType));
        }
    }
}
