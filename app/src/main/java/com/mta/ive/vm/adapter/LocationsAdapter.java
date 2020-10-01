package com.mta.ive.vm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;
import com.mta.ive.pages.location.ActivityManageLocations;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.pages.location.AddLocationFragment;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationViewHolder> {

    Context context;
    List<UserLocation> alluserLocations;

    public LocationsAdapter(Context context, List<UserLocation> tasks){
        this.context = context;
        this.alluserLocations = tasks;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LocationViewHolder(LayoutInflater.from( parent.getContext())
                .inflate(R.layout.location_item, parent, false));
    }

    // Enter UI fieds values here:
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder locationViewHolder, int position) {
        locationViewHolder.locationName.setText(alluserLocations.get(position).getName());
        locationViewHolder.locationAddress.setText(alluserLocations.get(position).getAddress());

        locationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String locationId = alluserLocations.get(position).getId();

                Bundle bundle = new Bundle();
                bundle.putString("locationId", locationId);

                Intent editLocationPage = new Intent(context, AddLocationFragment.class);
                editLocationPage.putExtras(bundle);
                ((ActivityManageLocations)context).startActivityForResult(editLocationPage, 79);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.alluserLocations.size();
    }
}