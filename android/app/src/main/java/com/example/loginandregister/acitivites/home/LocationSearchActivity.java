package com.example.loginandregister.acitivites.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.model.Location;

import java.util.ArrayList;

public class LocationSearchActivity extends AppCompatActivity implements LocationSearchListener {

    private RecyclerView recyclerView;

    private ImageButton btnBack;

    private ArrayList<Location> cityList = new ArrayList<>();

    private String flag;
    private String idS;
    private String nameS;
    private String idD;
    private String nameD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        recyclerView = findViewById(R.id.recyclerViewSearchBoarding);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> finish());

        flag = getIntent().getStringExtra("flag");
        idS = getIntent().getStringExtra("idS");
        nameS = getIntent().getStringExtra("nameS");
        idD = getIntent().getStringExtra("idD");
        nameD = getIntent().getStringExtra("nameD");

        recommendCityList();
    }

    private void recommendCityList() {
        cityList.add(new Location("1", "Hồ Chí Minh"));
        cityList.add(new Location("2", "Cần Thơ"));
        cityList.add(new Location("3", "Ninh Bình"));
        cityList.add(new Location("4", "Phú Yên"));
        CityListAdapter adapter = new CityListAdapter(LocationSearchActivity.this, this, cityList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onClick(Location location) {
        Log.d("###", "CLick");
        Intent intent = new Intent(LocationSearchActivity.this, HomeActivity.class);
        if (flag.equals("source")) {
            idS = location.getId();
            nameS = location.getName();
        } else {
            idD = location.getId();
            nameD = location.getName();
        }
        intent.putExtra("flag", flag);
        intent.putExtra("idS", idS);
        intent.putExtra("nameS", nameS);
        intent.putExtra("idD", idD);
        intent.putExtra("nameD", nameD);
        startActivity(intent);
        finish();
    }
}
