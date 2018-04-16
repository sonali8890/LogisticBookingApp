package com.sonali.portertestapp.view.locationsearch;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sonali.portertestapp.R;
import com.sonali.portertestapp.databinding.GooglePlacesItemviewBinding;
import com.sonali.portertestapp.domain.model.GooglePlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.LocationAutoSuggestViewHolder> {
    private ILocationSearchPresentor.ISearchView mView;
    private ArrayList<GooglePlacesSearchResult.Prediction> list;
    private boolean isRecentItems;

    public PlacesAdapter(Context context, List<GooglePlacesSearchResult.Prediction> locationAutoSuggestList, boolean isRecentItems) {
        mView = (ILocationSearchPresentor.ISearchView) context;
        list = (ArrayList<GooglePlacesSearchResult.Prediction>) locationAutoSuggestList;
        this.isRecentItems = isRecentItems;
    }

    public class LocationAutoSuggestViewHolder extends RecyclerView.ViewHolder {
        GooglePlacesItemviewBinding binder;

        public LocationAutoSuggestViewHolder(GooglePlacesItemviewBinding b) {
            super(b.getRoot());
            binder = b;
            binder.icon.setImageResource(isRecentItems ? R.mipmap.recent : R.mipmap.combined_shape);
            binder.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.onPlaceSelection(list.get(getAdapterPosition()));
                }
            });

        }

        public GooglePlacesItemviewBinding getBinder() {
            return binder;
        }
    }

    @Override
    public LocationAutoSuggestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GooglePlacesItemviewBinding binder = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.google_places_itemview, parent, false);
        return new LocationAutoSuggestViewHolder(binder);
    }

    @Override
    public void onBindViewHolder(LocationAutoSuggestViewHolder holder, final int position) {
        GooglePlacesItemviewBinding binder = holder.getBinder();
        binder.placeName.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}