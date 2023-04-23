package com.example.loginandregister.acitivites.seat;

import com.example.loginandregister.model.Seats;

interface OnSeatClickListener {
    void onSeatSelected(Seats seats);

    void onSeatDeselected(Seats seats);
}
