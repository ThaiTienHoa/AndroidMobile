package com.example.loginandregister.acitivites.route;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.acitivites.seat.SeatSelectionActivity;
import com.example.loginandregister.model.RouteModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RouteListActivity extends AppCompatActivity implements OnBusItemListener {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference busRef = db.collection("buses");
    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    // RoutesModel is buses model
    List<RouteModel> listOfRoute = new ArrayList<RouteModel>();
    String TAG = "Travel APP";

    RouteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");

        adapter = new RouteAdapter(listOfRoute, this);
        getAllStore(from, to);
        RecyclerView recyclerView = findViewById(R.id.recyclerviewBus);
        recyclerView.setAdapter(adapter);

        TextView btnBack = findViewById(R.id.tvBusListHeading);
        btnBack.setOnClickListener(view -> finish());
    }

    private void getAllStore(String from, String to) {
        listOfRoute.clear();
        busRef.addSnapshotListener((value, error) -> {
            if (value != null && !value.isEmpty()) {
                for (QueryDocumentSnapshot doc : value) {
                    RouteModel model = doc.toObject(RouteModel.class);
                    if (model.getFrom().equals(from) && model.getTo().equals(to)) {
                        listOfRoute.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(RouteModel route) {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra("busId", route.getId());
        startActivity(intent);
    }
}
