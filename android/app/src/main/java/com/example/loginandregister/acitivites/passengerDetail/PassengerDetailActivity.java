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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PassengerDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    CollectionReference busRef = db.collection("buses");
    Button btnProcess;

    EditText etPassengerName, etPassengerEmail, etPassengerAge, etEnterPassengerMobile;

    TextView tvBookingPrice, tvFrom, tvName, tvTotalTime, tvDepArrTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        btnProcess = findViewById(R.id.btnProceed);
        etPassengerName = findViewById(R.id.etPassengerName);
        etPassengerEmail = findViewById(R.id.etPassengerEmail);
        etPassengerAge = findViewById(R.id.idDescription);
        etEnterPassengerMobile = findViewById(R.id.etEnterPassengerMobile);
        tvBookingPrice = findViewById(R.id.tvBookingPrice);
        tvFrom = findViewById(R.id.tvFrom);
        tvName = findViewById(R.id.tvName);

        String busId = getIntent().getStringExtra("busId");
        String seatNo = getIntent().getStringExtra("seat_no");
        getBusesData(busId);
        btnProcess.setOnClickListener(view -> getBookingId(busId, seatNo));
    }

    private void getBookingId(String busId, String seatNo) {
        setBookingsData(busId, seatNo);
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

    private void setBookingsData(String busId, String seatNo) {

        String name = etPassengerName.getText().toString();
        String age = etPassengerAge.getText().toString();
        String email = etPassengerEmail.getText().toString();
        String phone = etEnterPassengerMobile.getText().toString();

        Intent intent = new Intent(this, PaymentsActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("mobile", phone);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        intent.putExtra("busId", busId);
        intent.putExtra("seat_no", seatNo);
        startActivity(intent);
    }
}
