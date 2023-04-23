package com.example.loginandregister.acitivites.listRoute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.MainActivity;
import com.example.loginandregister.R;

import com.example.loginandregister.acitivites.editRoute.EditRouteActivity;
import com.example.loginandregister.acitivites.manageUser.ManageUserAdapter;
import com.example.loginandregister.acitivites.route.RouteAdapter;
import com.example.loginandregister.acitivites.seat.SeatSelectionActivity;
import com.example.loginandregister.model.RouteModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListRouteActivity extends AppCompatActivity implements OnBusItemListener {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference busRef = db.collection("buses");
    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    // RoutesModel is buses model
    List<RouteModel> listOfRoute = new ArrayList<RouteModel>();
    String TAG = "Travel APP";

    ListRouteAdapter adapter;

    FloatingActionButton btnAddRoute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_route);
        adapter = new ListRouteAdapter(listOfRoute, this);
        btnAddRoute = findViewById(R.id.addRoute);

        btnAddRoute.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), EditRouteActivity.class)));
        getAllStore();
        RecyclerView recyclerView = findViewById(R.id.recyclerviewBus);
        recyclerView.setAdapter(adapter);

        TextView btnBack = findViewById(R.id.tvBusListHeading);
        btnBack.setOnClickListener(view -> finish());
    }

    private void getAllStore() {
        listOfRoute.clear();
        busRef.addSnapshotListener((value, error) -> {
            if (value != null && !value.isEmpty()) {
                for (QueryDocumentSnapshot doc : value) {
                    RouteModel model = doc.toObject(RouteModel.class);
                    listOfRoute.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onClick(RouteModel route) {
        Intent intent = new Intent(this, EditRouteActivity.class);
        intent.putExtra("busId", route.getId());
        startActivity(intent);
    }

    @Override
    public void btnDelete(RouteModel route) {
        userRef.get().addOnSuccessListener(query -> {
            List<DocumentSnapshot> docs = query.getDocuments();
            for (DocumentSnapshot doc : docs) {
                String bookingId = (String) doc.get("bookings");
                String userId = (String) doc.get("userId");
                if (bookingId != null) {
                    bookingRef.document(bookingId).get().addOnSuccessListener(docBus -> {
                        String busId = (String) docBus.get("busId");
                        if (busId.equals(route.getId())) ;
                        userRef.document(userId).update("bookings", null);
                    });
                }
            }
        });
        busRef.document(route.getId()).delete();
        Toast.makeText(ListRouteActivity.this, "Deleted successfull", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
