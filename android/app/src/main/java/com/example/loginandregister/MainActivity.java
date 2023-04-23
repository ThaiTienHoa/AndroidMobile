package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginandregister.acitivites.detail.DetailBookingActivity;
import com.example.loginandregister.acitivites.editRoute.EditRouteActivity;
import com.example.loginandregister.acitivites.home.HomeActivity;
import com.example.loginandregister.acitivites.loginAndResgiter.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    ImageButton ibDatVe, imgBtnXemthongtin, ibQuanLyTuyen;

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

        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
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

        ibDatVe.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), HomeActivity.class)));

        imgBtnXemthongtin.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), DetailBookingActivity.class)));

        ibQuanLyTuyen.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), EditRouteActivity.class)));
    }
}