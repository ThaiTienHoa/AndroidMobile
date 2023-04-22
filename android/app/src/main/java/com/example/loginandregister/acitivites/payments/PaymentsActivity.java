package com.example.loginandregister.acitivites.payments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    CollectionReference busRef = db.collection("buses");

    TextView tvStartingPoint, tvEndingPoint, tvStartTime, tvEndingTime, textView5, tvPrice;
    Button btnDone;

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
        String busId = getIntent().getStringExtra("busId");
        getBusesData(busId);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Your order sucessfull", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getBusesData(String busId) {
        busRef.document(busId).get().addOnSuccessListener(doc -> {
            if (doc != null) {
                String from = (String) doc.getData().get("from");
                String to = (String) doc.getData().get("to");
                String travellingTime = (String) doc.getData().get("travellingTime");
                String price = (String) doc.getData().get("price");
                String timingStart = (String) doc.getData().get("timingStart");
                String timingEnd = (String) doc.getData().get("timingEnd");
                tvStartingPoint.setText(from);
                tvEndingPoint.setText(to);
                textView5.setText(travellingTime);
                tvStartTime.setText(timingStart);
                tvEndingTime.setText(timingEnd);
                tvPrice.setText(price);
            }
        });
    }

}
