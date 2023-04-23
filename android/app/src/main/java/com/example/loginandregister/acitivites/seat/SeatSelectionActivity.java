package com.example.loginandregister.acitivites.seat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.acitivites.passengerDetail.PassengerDetailActivity;
import com.example.loginandregister.model.RouteModel;
import com.example.loginandregister.model.Seats;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity implements OnSeatClickListener {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference busRef = db.collection("buses");
    List<Seats> listOfRoute = new ArrayList<>();
    RecyclerView recyclerView;

    RouteSeatAdapter seatAdapter;

    Button btnSignUpFrag;

    ImageButton btnBack;

    TextView tvTravelAgencyDetails, tvBusDetails;

    Seats seats;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        recyclerView = findViewById(R.id.recyclerViewBus);
        btnSignUpFrag = findViewById(R.id.btnSignupFrag);

        btnBack = findViewById(R.id.btnBack);
        tvTravelAgencyDetails = findViewById(R.id.tvTravelAgencyDetails);
        tvBusDetails = findViewById(R.id.tvBusDetails);

        btnBack.setOnClickListener(view -> finish());
        seatAdapter = new RouteSeatAdapter(listOfRoute, this);
        String busId = getIntent().getStringExtra("busId");

        getBusesData(busId);
        btnSignUpFrag.setOnClickListener(view -> {
            if (seats != null) {
                Intent intent = new Intent(getApplicationContext(), PassengerDetailActivity.class);
                intent.putExtra("busId", busId);
                intent.putExtra("seat_no", seats.getSeat_no());
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Select your seat!", Toast.LENGTH_SHORT).show();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        recyclerView.setAdapter(seatAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getBusesData(String busId) {
        busRef.document(busId).get().addOnSuccessListener(doc -> {
            if (doc != null) {
                ArrayList<HashMap<String, String>> seats = (ArrayList<HashMap<String, String>>) doc.getData().get("seats");
                for (int i = 0; i < seats.size(); i++) {
                    String available = seats.get(i).get("available");
                    String seat_no = seats.get(i).get("seat_no");
                    Seats item = new Seats(available, seat_no);
                    listOfRoute.add(item);
                }
                String name = (String) doc.getData().get("name");
                String from = (String) doc.getData().get("from");
                String to = (String) doc.getData().get("to");
                tvTravelAgencyDetails.setText(name);
                tvBusDetails.setText("From " + from + " to " + to);
                seatAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSeatSelected(Seats seats) {
        this.seats = seats;
        btnSignUpFrag.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSeatDeselected(Seats seats) {
        this.seats = null;
        btnSignUpFrag.setVisibility(View.GONE);
    }
}
