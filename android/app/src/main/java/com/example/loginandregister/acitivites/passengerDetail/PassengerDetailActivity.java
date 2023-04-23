package com.example.loginandregister.acitivites.passengerDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.R;
import com.example.loginandregister.acitivites.payments.PaymentsActivity;
import com.example.loginandregister.acitivites.seat.SeatSelectionActivity;
import com.example.loginandregister.model.Seats;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class PassengerDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    CollectionReference busRef = db.collection("buses");
    Button btnProcess;

    EditText etPassengerName, etPassengerEmail, etPassengerAge, etEnterPassengerMobile;

    TextView tvBookingPrice, tvFrom, tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        btnProcess = findViewById(R.id.btnProceed);
        etPassengerName = findViewById(R.id.etPassengerName);
        etPassengerEmail = findViewById(R.id.etPassengerEmail);
        etPassengerAge = findViewById(R.id.etPassengerAge);
        etEnterPassengerMobile = findViewById(R.id.etEnterPassengerMobile);
        tvBookingPrice = findViewById(R.id.tvBookingPrice);
        tvFrom = findViewById(R.id.tvFrom);
        tvName = findViewById(R.id.tvName);

        String busId = getIntent().getStringExtra("busId");
        String seatNo = getIntent().getStringExtra("seat_no");
        getBusesData(busId);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBookingId(busId, seatNo);
            }
        });
    }

    private void getBookingId(String busId, String seatNo) {
        String id = auth.getCurrentUser().getUid();
        userRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc.getData().get("bookings") != null) {
                    String bookingId = doc.getData().get("bookings").toString();
                    setBookingsData(bookingId, busId, seatNo);
                }
            }
        });
    }

    private void getBusesData(String busId) {
        busRef.document(busId).get().addOnSuccessListener(doc -> {
            if (doc != null) {
                String from = (String) doc.getData().get("from");
                String name = (String) doc.getData().get("name");
                String price = (String) doc.getData().get("price");
                tvFrom.setText(from);
                tvName.setText(name);
                tvBookingPrice.setText(price);
            }
        });
    }

    private void setBookingsData(String bookingId, String busId, String seatNo) {

        String name = etPassengerName.getText().toString();
        String age = etPassengerAge.getText().toString();
        String email = etPassengerEmail.getText().toString();
        String phone = etEnterPassengerMobile.getText().toString();

        bookingRef.document(bookingId).update("email", email);
        bookingRef.document(bookingId).update("mobile", phone);
        bookingRef.document(bookingId).update("seat_no", seatNo);
        bookingRef.document(bookingId).update("name", name);
        bookingRef.document(bookingId).update("age", age);

        Intent intent = new Intent(this, PaymentsActivity.class);
        intent.putExtra("busId", busId);
        intent.putExtra("seat_no", seatNo);
        startActivity(intent);
    }
}
