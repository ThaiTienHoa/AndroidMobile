package com.example.loginandregister.acitivites.route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.model.RouteModel;

import org.w3c.dom.Text;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    List<RouteModel> listOfRoute;
    OnBusItemListener listener;


    public RouteAdapter(List<RouteModel> listOfRoute, OnBusItemListener listener) {
        this.listOfRoute = listOfRoute;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.itemlayout_bus,
                        parent, false
                ), listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteModel routeModel = listOfRoute.get(position);
        holder.setData(routeModel);
    }

    @Override
    public int getItemCount() {
        return listOfRoute.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusName, tvBusDes, tvBusBoarding, tvBusDestination, tvDepArrTime, tvTotalTime, tvPrice;
        LinearLayout busBookingCard;

        OnBusItemListener onBusItemListener;

        public ViewHolder(@NonNull View itemView, OnBusItemListener onBusItemListener) {
            super(itemView);
            tvBusName = itemView.findViewById(R.id.tvBusName);
            tvBusDes = itemView.findViewById(R.id.tvBusDesc);
            tvBusBoarding = itemView.findViewById(R.id.tvBusBoarding);
            tvBusDestination = itemView.findViewById(R.id.tvBusDestination);
            tvDepArrTime = itemView.findViewById(R.id.tvDepArrTime);
            tvTotalTime = itemView.findViewById(R.id.tvTotalTime);
            tvPrice = itemView.findViewById(R.id.tvPrise);
            busBookingCard = itemView.findViewById(R.id.busBookingCard);
            this.onBusItemListener = onBusItemListener;
        }

        public void setData(RouteModel routeModel) {
            tvBusName.setText(routeModel.getName());
            tvBusDes.setText(routeModel.getDescription());
            tvBusBoarding.setText(routeModel.getFrom());
            tvBusDestination.setText(routeModel.getTo());
            tvDepArrTime.setText(routeModel.getTiming());
            tvTotalTime.setText(routeModel.getTravellingTime());
            tvPrice.setText(routeModel.getPrice());
            busBookingCard.setOnClickListener(view -> onBusItemListener.onClick(routeModel));
        }
    }
}
