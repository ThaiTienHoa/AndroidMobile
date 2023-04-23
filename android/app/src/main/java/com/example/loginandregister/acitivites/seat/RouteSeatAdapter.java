package com.example.loginandregister.acitivites.seat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.model.Seats;

import java.util.List;

public class RouteSeatAdapter extends RecyclerView.Adapter<RouteSeatAdapter.ViewHolder> {

    List<Seats> listOfSeats;
    OnSeatClickListener onSeatClickListener;

    public RouteSeatAdapter(List<Seats> listOfSeats, OnSeatClickListener onSeatClickListener) {
        this.listOfSeats = listOfSeats;
        this.onSeatClickListener = onSeatClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_seat_item_layout, parent, false), onSeatClickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Seats seats = listOfSeats.get(position);
        holder.setData(seats);
    }

    @Override
    public int getItemCount() {
        return listOfSeats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSeat;
        LinearLayout seatItem;

        OnSeatClickListener listener;

        public ViewHolder(@NonNull View itemView, OnSeatClickListener listener) {
            super(itemView);
            imageSeat = itemView.findViewById(R.id.imageSeat);
            seatItem = itemView.findViewById(R.id.seatItem);
            this.listener = listener;
        }

        public void setData(Seats seats) {
            setSeatIcon(seats, imageSeat, seatItem);
            seatItem.setClickable(false);

            seatItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (seatItem.isSelected()) {
                        seatItem.setSelected(false);
                        imageSeat.setBackgroundResource(R.drawable.ic_seat_selected);
                        listener.onSeatSelected(seats);
                    } else {
                        seatItem.setSelected(true);
                        setSeatIcon(seats, imageSeat, seatItem);
                        listener.onSeatDeselected(seats);
                    }
                }
            });
        }

        private void setSeatIcon(Seats seats, ImageView seatIcon, LinearLayout seat) {
            if (seats.getAvailable().equals("yes")) {
                seatIcon.setBackgroundResource(R.drawable.ic_seat_available);
            } else {
                seat.setClickable(false);
                seatIcon.setClickable(false);
                seatIcon.setBackgroundResource(R.drawable.ic_seat_booked);
            }
        }
    }
}
