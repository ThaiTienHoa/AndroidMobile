package com.example.loginandregister.acitivites.manageUser;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManagerUserActivity extends AppCompatActivity {

    TextView tvBusListHeading;


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);
        tvBusListHeading = findViewById(R.id.tvBusListHeading);
        tvBusListHeading.setOnClickListener(view -> finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerviewUserManager);

        userRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot query) {
                List<DocumentSnapshot> docs = query.getDocuments();
                List<Map<String, Object>> users = new ArrayList<>();
                for (DocumentSnapshot doc : docs) {
                    Map<String, Object> user = doc.getData();
                    users.add(user);
                }
                ManageUserAdapter adapter = new ManageUserAdapter(users);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
