package com.mta.ive.vm.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;

class LocationViewHolder extends  RecyclerView.ViewHolder{

    TextView locationName, locationAddress;
    ImageView locationLine;

    public LocationViewHolder(@NonNull View itemView) {
        super(itemView);

        locationName = (TextView) itemView.findViewById(R.id.locationTitleText);
        locationAddress = (TextView) itemView.findViewById(R.id.locationAddressText);
    }

}