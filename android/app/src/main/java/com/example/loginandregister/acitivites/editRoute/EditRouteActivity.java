package com.example.loginandregister.acitivites.editRoute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.MainActivity;
import com.example.loginandregister.R;
import com.example.loginandregister.acitivites.home.HomeActivity;
import com.example.loginandregister.acitivites.home.LocationSearchActivity;
import com.example.loginandregister.model.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditRouteActivity extends AppCompatActivity {

    EditText idStartPoint, idEndPoint, idAdrress;

    EditText idBusName, idSeatNumber, idDescription, idPrice;

    EditText idStartTime, idEndTime;

    MaterialButton btnProcess;
    Location sourceLocation = new Location("", "");
    Location destinationLocation = new Location("", "");


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference busRef = db.collection("buses");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);
        idStartPoint = findViewById(R.id.idStartPoint);
        idEndPoint = findViewById(R.id.idEndPoint);
        idAdrress = findViewById(R.id.idAdrress);
        idBusName = findViewById(R.id.idBusName);
        idSeatNumber = findViewById(R.id.idSeatNumber);
        idDescription = findViewById(R.id.idDescription);
        idPrice = findViewById(R.id.idPrice);
        idStartTime = findViewById(R.id.idStartTime);
        idEndTime = findViewById(R.id.idEndTime);
        btnProcess = findViewById(R.id.btnProceed);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRote();
            }
        });

        String flag = getIntent().getStringExtra("flag");
        String idLocationS = getIntent().getStringExtra("idS");
        String nameLocationS = getIntent().getStringExtra("nameS");
        String idLocationD = getIntent().getStringExtra("idD");
        String nameLocationD = getIntent().getStringExtra("nameD");

        if (flag != null) {
            sourceLocation = new Location(idLocationS, nameLocationS);
            destinationLocation = new Location(idLocationD, nameLocationD);
            idStartPoint.setText(sourceLocation.getName());
            idEndPoint.setText(destinationLocation.getName());
        }

        idStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRouteActivity.this, LocationSearchActivity.class);
                intent.putExtra("flag", "source");
                intent.putExtra("screen", "edit");
                intent.putExtra("idS", sourceLocation.getId());
                intent.putExtra("nameS", sourceLocation.getName());
                intent.putExtra("idD", destinationLocation.getId());
                intent.putExtra("nameD", destinationLocation.getName());
                startActivity(intent);
            }
        });

        idEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRouteActivity.this, LocationSearchActivity.class);
                intent.putExtra("flag", "destination");
                intent.putExtra("screen", "edit");
                intent.putExtra("idS", sourceLocation.getId());
                intent.putExtra("nameS", sourceLocation.getName());
                intent.putExtra("idD", destinationLocation.getId());
                intent.putExtra("nameD", destinationLocation.getName());
                startActivity(intent);
            }
        });
    }

    private void saveRote() {
        String idCollection = busRef.document().getId();
        HashMap<String, Object> data = new HashMap<>();
        data.put("address", idAdrress.getText().toString());
        data.put("description", idDescription.getText().toString());
        data.put("description", idDescription.getText().toString());
        data.put("from", idStartPoint.getText().toString());
        data.put("to", idEndPoint.getText().toString());
        data.put("id", idCollection);
        data.put("name", idBusName.getText().toString());
        data.put("price", idPrice.getText().toString());
        data.put("timingEnd", idStartTime.getText().toString());
        data.put("timingStart", idEndTime.getText().toString());
        int travelTime = Integer.parseInt(idStartTime.getText().toString()) - Integer.parseInt(idEndTime.getText().toString());
        data.put("travellingTime", Math.abs(travelTime) + "");

        int seatNumber = Integer.parseInt(idSeatNumber.getText().toString());
        List<HashMap<String, String>> seats = new ArrayList<>();
        for (int i = 0; i < seatNumber; i++) {
            HashMap<String, String> seatItem = new HashMap<>();
            seatItem.put("available", "yes");
            seatItem.put("seat_no", i + "");
            seats.add(seatItem);
        }
        data.put("seats", seats);
        busRef.document(idCollection).set(data)
                .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(),
                        "Add route complete!!!", Toast.LENGTH_SHORT).show());

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
