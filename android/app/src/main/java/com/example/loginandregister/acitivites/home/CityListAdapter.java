package com.example.loginandregister.acitivites.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.model.Location;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    Context context;
    LocationSearchListener listener;

    ArrayList<Location> location;

    CityListAdapter(Context context, LocationSearchListener listener, ArrayList<Location> location) {
        this.context = context;
        this.listener = listener;
        this.location = location;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.boarding_point_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location item = location.get(position);
        holder.textView.setText(item.getName());
        holder.itemView.setOnClickListener(view -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return location.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvBoardingCity);
        }
    }
}
