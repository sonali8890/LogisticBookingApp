package com.sonali.portertestapp.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sonali.portertestapp.domain.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sonali
 */
public class GooglePlacesSearchResult extends BaseResponse {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Prediction implements Serializable{

        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("place_id")
        @Expose
        private String placeId;

        double lat;
        double lng;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        @Override
        public boolean equals(Object obj) {
            Prediction p = (Prediction) obj;
            if(this == p)
                return true;
            if(p.placeId.equalsIgnoreCase(this.placeId))
                return true;

            return super.equals(obj);
        }
    }
}






