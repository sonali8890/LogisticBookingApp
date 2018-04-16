package com.sonali.portertestapp.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sonali.portertestapp.domain.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sonali
 */
public class Vehicle extends BaseResponse {


    @SerializedName("cost")
    @Expose
    private int cost;
    @SerializedName("eta")
    @Expose
    private int eta;

    private String locationName;

    public int getCost() {
        return cost;
    }

    public int getEta() {
        return eta;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public void setLocationName(String locationname) {
        locationName = locationname;
    }

    public String getLocationName() {
        return locationName;
    }

}

