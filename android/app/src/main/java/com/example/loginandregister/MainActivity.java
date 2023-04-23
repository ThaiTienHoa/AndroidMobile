package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.acitivites.detail.DetailBookingActivity;
import com.example.loginandregister.acitivites.editRoute.EditRouteActivity;
import com.example.loginandregister.acitivites.home.HomeActivity;
import com.example.loginandregister.acitivites.listRoute.ListRouteActivity;
import com.example.loginandregister.acitivites.listRoute.ListRouteAdapter;
import com.example.loginandregister.acitivites.loginAndResgiter.Login;
import com.example.loginandregister.acitivites.manageUser.ManagerUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    ImageButton ibDatVe, imgBtnXemthongtin, ibQuanLyTuyen, imgQuanLyUser;

    TextView tv_timtuyen, tv_quanLyUser;
    String emailRoot = "admin@gmail.com";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bookings = db.collection("bookings");
    CollectionReference users = db.collection("users");
    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btn_logout);
        textView = findViewById(R.id.tv_username);
        ibDatVe = findViewById(R.id.imgBtn_datve);
        imgBtnXemthongtin = findViewById(R.id.imgbtn_xemthongtin);
        ibQuanLyTuyen = findViewById(R.id.imgBtn_quanLyTuyen);
        imgQuanLyUser = findViewById(R.id.imgBtn_quanLyUser);
        tv_timtuyen = findViewById(R.id.tv_timtuyen);
        tv_quanLyUser = findViewById(R.id.tv_quanLyUser);

        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        if (!user.getEmail().equals(emailRoot)) {
            ibQuanLyTuyen.setVisibility(View.GONE);
            imgQuanLyUser.setVisibility(View.GONE);
            tv_timtuyen.setVisibility(View.GONE);
            tv_quanLyUser.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        ibDatVe.setOnClickListener(view -> {
            if(!checkBooking()) {
                Toast.makeText(getApplicationContext(), "You had ticket!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });

        imgBtnXemthongtin.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), DetailBookingActivity.class)));

        ibQuanLyTuyen.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ListRouteActivity.class)));
        imgQuanLyUser.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ManagerUserActivity.class)));
    }


    private boolean checkBooking() {
        String id = auth.getCurrentUser().getUid();
        users.document(id).get().addOnSuccessListener(doc -> {
            if (doc.getData().get("bookings") == null) {
                result = true;
            }
        });

        return result;
    }

}