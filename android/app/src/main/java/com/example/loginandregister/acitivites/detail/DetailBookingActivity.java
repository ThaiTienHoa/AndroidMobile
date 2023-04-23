package com.example.loginandregister.acitivites.detail;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DetailBookingActivity extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference busRef = db.collection("buses");
    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    TextView tvBusBoarding, tvBusDestination, tvDepArrTime, tvTotalTime, tvNoOfSeats;
    TextView tvBusName, tvEmail, tvMobile, tvAge, tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_detail_booking);
        tvBusBoarding = findViewById(R.id.tvBusBoarding);
        tvBusDestination = findViewById(R.id.tvBusDestination);
        tvDepArrTime = findViewById(R.id.tvDepArrTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvNoOfSeats = findViewById(R.id.tvNoOfSeats);
        tvBusName = findViewById(R.id.tvBusName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvAge = findViewById(R.id.tvAge);
        tvName = findViewById(R.id.tvName);

        String id = auth.getCurrentUser().getUid();
        userRef.document(id).get().addOnSuccessListener(doc -> {
            if (doc.getData().get("bookings") != null) {
                String bookingId = doc.getData().get("bookings").toString();
                bookingRef.document(bookingId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        String busId = (String) doc.getData().get("busId");
                        String email = (String) doc.getData().get("email");
                        String mobile = (String) doc.getData().get("mobile");
                        String from = (String) doc.getData().get("from");
                        String to = (String) doc.getData().get("to");
                        tvEmail.setText(email);
                        tvMobile.setText(mobile);
                        tvBusBoarding.setText(from);
                        tvBusDestination.setText(to);
                    }
                });
                bookingRef.document(bookingId).collection("passenger").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        String age = (String) doc.getData().get("age");
                        String name = (String) doc.getData().get("name");
                    }
                });
            }
        });

    }
}
