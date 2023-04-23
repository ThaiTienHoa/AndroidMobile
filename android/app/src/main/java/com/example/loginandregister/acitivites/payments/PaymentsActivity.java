package com.example.loginandregister.acitivites.payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.MainActivity;
import com.example.loginandregister.R;
import com.example.loginandregister.model.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    CollectionReference busRef = db.collection("buses");

    TextView tvStartingPoint, tvEndingPoint, tvStartTime, tvEndingTime, textView5, tvPrice;
    Button btnDone;

    String from, to, travellingTime, price, timingStart, timingEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_payment);
        tvStartingPoint = findViewById(R.id.tvStartingPoint);
        tvEndingPoint = findViewById(R.id.tvEndingPoint);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndingTime = findViewById(R.id.tvEndingTime);
        textView5 = findViewById(R.id.textView5);
        tvPrice = findViewById(R.id.tvPrice);
        btnDone = findViewById(R.id.btnDone);
        String email = getIntent().getStringExtra("email");
        String mobile = getIntent().getStringExtra("mobile");
        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");
        String busId = getIntent().getStringExtra("busId");
        String seatNo = getIntent().getStringExtra("seat_no");
        getBusesData(busId);
        btnDone.setOnClickListener(view -> {
            String idBooking = createOrder(from, to);
            updateSeat(busId, seatNo);
            updateBusIdForUser(busId);
            setBookingData(idBooking, busId, email, mobile, name, age, seatNo);
            Toast.makeText(getApplicationContext(), "Your order sucessfull", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }

    private void getBusesData(String busId) {
        busRef.document(busId).get().addOnSuccessListener(doc -> {
            if (doc != null) {
                from = (String) doc.getData().get("from");
                to = (String) doc.getData().get("to");
                travellingTime = (String) doc.getData().get("travellingTime");
                price = (String) doc.getData().get("price");
                timingStart = (String) doc.getData().get("timingStart");
                timingEnd = (String) doc.getData().get("timingEnd");
                tvStartingPoint.setText(from);
                tvEndingPoint.setText(to);
                textView5.setText(travellingTime);
                tvStartTime.setText(timingStart);
                tvEndingTime.setText(timingEnd);
                tvPrice.setText(price);
            }
        });
    }

    private String createOrder(String from, String to) {
        String idCollection = bookingRef.document().getId();
        BookingModel bookingModel = new BookingModel(
                from,
                to,
                idCollection,
                ""
        );
        bookingRef.document(idCollection).set(bookingModel);
        setBookingId(idCollection);
        return idCollection;
    }

    private void updateBusIdForUser(String busId) {
        String id = auth.getCurrentUser().getUid();
        userRef.document(id).get().addOnSuccessListener(doc -> {
            if (doc.getData().get("bookings") != null) {
                String bookingId = doc.getData().get("bookings").toString();
                bookingRef.document(bookingId).update("busId", busId);
            }
        });
    }

    private void setBookingId(String bookingId) {
        String id = auth.getCurrentUser().getUid();
        userRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc.getData().get("bookings") != null) {
                    userRef.document(id).update("bookings", bookingId);
                } else {
                    userRef.document(id).update("bookings", bookingId);
                }
            }
        });
    }

    private void setBookingData(String bookingId, String busId, String email, String phone, String name, String age, String seatNo) {
        bookingRef.document(bookingId).update("email", email);
        bookingRef.document(bookingId).update("mobile", phone);
        bookingRef.document(bookingId).update("seat_no", seatNo);
        bookingRef.document(bookingId).update("name", name);
        bookingRef.document(bookingId).update("age", age);
        bookingRef.document(bookingId).update("busId", busId);
    }

    private void updateSeat(String busId, String seatNo) {
        busRef.document(busId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc != null) {
                    int index = -1;
                    List<HashMap<String, Object>> seats = (List<HashMap<String, Object>>) doc.getData().get("seats");
                    for (int i = 0; i < seats.size(); i++) {
                        HashMap<String, Object> seat = seats.get(i);
                        if (seat.get("seat_no").equals(seatNo)) {
                            index = i;
                            HashMap<String, Object> newData = new HashMap<>();
                            newData.put("available", "no");
                            newData.put("seat_no", seatNo);
                            seats.set(i, newData);
                            break;
                        }
                    }
                    if (index != -1) {
                        busRef.document(busId).update("seats", seats)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Update seats", "Cập nhật thành công");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.w("Update seats", "Cập nhật thất bại", e);
                                    }
                                });
                    }
                }
            }
        });
    }


}
