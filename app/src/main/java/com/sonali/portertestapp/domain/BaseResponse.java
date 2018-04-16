package com.sonali.portertestapp.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by Sonali
 */
public abstract class BaseResponse implements Serializable {

    @SerializedName("incomplete_results")
    @Expose
    public boolean incomplete_results ;

    public boolean isIncomplete_results() {
        return incomplete_results;
    }
}
