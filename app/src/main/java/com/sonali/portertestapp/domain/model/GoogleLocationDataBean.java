package com.sonali.portertestapp.domain.model;

/**
 * Created by Sonali
 */

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoogleLocationDataBean implements Serializable {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    @SerializedName("result")
    @Expose
    private Result latLongResult;


    public String getLocationName() {
        if (results != null && !results.isEmpty()) {
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i) != null && !TextUtils.isEmpty(results.get(i).getFormattedAddress())) {
                    return results.get(i).getFormattedAddress();
                }
            }
        }
        return null;
    }


    public void setLatLong(GooglePlacesSearchResult.Prediction place) {
        if(latLongResult != null && latLongResult.getGeometry() != null && latLongResult.getGeometry().getLocation() != null) {
            place.setLat(latLongResult.getGeometry().getLocation().getLat());
            place.setLng(latLongResult.getGeometry().getLocation().getLng());
        }else if(results != null && !results.isEmpty()){
            for (Result result : results) {
                if(result != null && result.getGeometry() != null && result.getGeometry().getLocation() != null) {
                    place.setLat(result.getGeometry().getLocation().getLat());
                    place.setLng(result.getGeometry().getLocation().getLng());
                    break;
                }
            }
        }
    }
}

class Result implements Serializable {

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents = null;

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }


    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }


    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

}

class Geometry implements Serializable {

    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}


class AddressComponent implements Serializable {

    @SerializedName("long_name")
    @Expose
    private String longName;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}