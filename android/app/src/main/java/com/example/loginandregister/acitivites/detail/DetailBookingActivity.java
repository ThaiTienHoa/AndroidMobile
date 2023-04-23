package com.example.loginandregister.acitivites.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.MainActivity;
import com.example.loginandregister.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class DetailBookingActivity extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference busRef = db.collection("buses");
    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    TextView tvBusBoarding, tvBusDestination, tvDepArrTime, tvTotalTime;
    TextView tvBusName, tvEmail, tvMobile, tvAge, tvName, tvSeatNo, tvToolbarTitle;

    Button btnCancel;

    String busId, seatNo;

    LinearLayout busBookingCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_detail_booking);
        tvBusBoarding = findViewById(R.id.tvBusBoarding);
        tvBusDestination = findViewById(R.id.tvBusDestination);
        tvDepArrTime = findViewById(R.id.tvDepArrTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvSeatNo = findViewById(R.id.tvSeatNo);
        tvBusName = findViewById(R.id.tvBusName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvAge = findViewById(R.id.tvAge);
        tvName = findViewById(R.id.tvName);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnCancel = findViewById(R.id.btnCancel);
        busBookingCard = findViewById(R.id.busBookingCard);

        String id = auth.getCurrentUser().getUid();

        btnCancel.setOnClickListener(view -> {
            userRef.document(id).update("bookings", null);
            updateSeat(busId, seatNo);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        tvToolbarTitle.setOnClickListener(view -> finish());

        userRef.document(id).get().addOnSuccessListener(doc -> {
            if (doc.getData().get("bookings") != null) {
                busBookingCard.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                String bookingId = doc.getData().get("bookings").toString();
                bookingRef.document(bookingId).get().addOnSuccessListener(doc1 -> {
                    busId = (String) doc1.getData().get("busId");
                    String email = (String) doc1.getData().get("email");
                    String mobile = (String) doc1.getData().get("mobile");
                    String from = (String) doc1.getData().get("from");
                    String to = (String) doc1.getData().get("to");
                    seatNo = (String) doc1.getData().get("seat_no");
                    tvEmail.setText("Email: " + email);
                    tvMobile.setText("Mobile phone: " + mobile);
                    tvBusBoarding.setText(from);
                    tvBusDestination.setText(to);
                    tvSeatNo.setText("Seat number: " + seatNo);

                    String age = (String) doc1.getData().get("age");
                    String name = (String) doc1.getData().get("name");
                    tvAge.setText("Age: " + age);
                    tvName.setText("Name: " + name);
                    assert busId != null;
                    busRef.document(busId).get().addOnSuccessListener(busDoc -> {
                        String busName = (String) busDoc.get("name");
                        tvBusName.setText(busName);
                    });
                });
            } else {
                busBookingCard.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        });

    }

    private void updateSeat(String busId, String seatNo) {
        busRef.document(busId).get().addOnSuccessListener(doc -> {
            if (doc != null) {
                int index = -1;
                List<HashMap<String, Object>> seats = (List<HashMap<String, Object>>) doc.getData().get("seats");
                for (int i = 0; i < seats.size(); i++) {
                    HashMap<String, Object> seat = seats.get(i);
                    if (seat.get("seat_no").equals(seatNo)) {
                        index = i;
                        HashMap<String, Object> newData = new HashMap<>();
                        newData.put("available", "yes");
                        newData.put("seat_no", seatNo);
                        seats.set(i, newData);
                        break;
                    }
                }
                if (index != -1) {
                    busRef.document(busId).update("seats", seats)
                            .addOnSuccessListener(aVoid -> Log.d("Update seats", "Cập nhật thành công"))
                            .addOnFailureListener(e -> Log.w("Update seats", "Cập nhật thất bại", e));
                }
            }
        });
    }
}
