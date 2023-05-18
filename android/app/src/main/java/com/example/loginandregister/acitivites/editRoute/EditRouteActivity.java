package com.example.loginandregister.acitivites.editRoute;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.MainActivity;
import com.example.loginandregister.R;
import com.example.loginandregister.acitivites.home.LocationSearchActivity;
import com.example.loginandregister.model.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditRouteActivity extends AppCompatActivity {

    EditText idStartPoint, idEndPoint, idAdrress;

    EditText idBusName, idSeatNumber, idDescription, idPrice;

    EditText idDateStart, idDateEnd, idStartTime, idEndTime;

    MaterialButton btnProcess;

    TextView btnBack;
    Location sourceLocation = new Location("", "");
    Location destinationLocation = new Location("", "");


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("users");

    CollectionReference busRef = db.collection("buses");

    String dateStart = "";
    String dateEnd = "";
    String timeStart = "";
    String timeEnd = "";

    boolean isUpdate = false;

    String busIdCurrent = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);
        idStartPoint = findViewById(R.id.idStartPoint);
        idEndPoint = findViewById(R.id.idEndPoint);
        idAdrress = findViewById(R.id.idAdrress);
        idBusName = findViewById(R.id.idBusName);
        idSeatNumber = findViewById(R.id.idSeatNumberEdit);
        idDescription = findViewById(R.id.idDescription);
        idPrice = findViewById(R.id.idPrice);
        idDateStart = findViewById(R.id.idDateStart);
        idDateEnd = findViewById(R.id.idDateEnd);
        idStartTime = findViewById(R.id.idTimeStart);
        idEndTime = findViewById(R.id.idTimeEnd);
        btnProcess = findViewById(R.id.btnProceed);
        btnBack = findViewById(R.id.tvToolbarTitle);
        btnBack.setOnClickListener(view -> finish());

        btnProcess.setOnClickListener(view -> saveRote());

        String busId = getIntent().getStringExtra("busId");
        if (busId != null) {
            busIdCurrent = busId;
            fetchBus(busId);
        }

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

        idDateStart.setOnClickListener(view -> {
            openDatePicker(value -> {
                dateStart = value;
                idDateStart.setText(value);
            });
        });
        idStartTime.setOnClickListener(view -> {
            openTimePicker(value -> {
                timeStart = value;
                idStartTime.setText(value);
            });
        });
        idDateEnd.setOnClickListener(view -> {
            openDatePicker(value -> {
                dateEnd = value;
                idDateEnd.setText(value);
            });
        });
        idEndTime.setOnClickListener(view -> {
            openTimePicker(value -> {
                timeEnd = value;
                idEndTime.setText(value);
            });
        });
    }

    private void saveRote() {
        String idCollection = busRef.document().getId();
        HashMap<String, Object> data = new HashMap<>();
        data.put("address", idAdrress.getText().toString());
        data.put("description", idDescription.getText().toString());
        data.put("from", idStartPoint.getText().toString());
        data.put("to", idEndPoint.getText().toString());
        data.put("id", isUpdate ? busIdCurrent : idCollection);
        data.put("name", idBusName.getText().toString());
        data.put("price", idPrice.getText().toString());
        data.put("dateStart", dateStart);
        data.put("dateEnd", dateEnd);
        data.put("timeStart", timeStart);
        data.put("timeEnd", timeEnd);

        int seatNumber = Integer.parseInt(idSeatNumber.getText().toString());
        List<HashMap<String, String>> seats = new ArrayList<>();
        for (int i = 0; i < seatNumber; i++) {
            HashMap<String, String> seatItem = new HashMap<>();
            seatItem.put("available", "yes");
            seatItem.put("seat_no", i + "");
            seats.add(seatItem);
        }
        data.put("seats", seats);
        if (isUpdate) {
            busRef.document(busIdCurrent).delete();
            busRef.document(busIdCurrent).set(data)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(),
                                "Add route complete!!!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            busRef.document(idCollection).set(data)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(),
                                "Add route complete!!!", Toast.LENGTH_SHORT).show();
                    });
        }


        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void fetchBus(String busId) {
        busRef.document(busId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                String id = (String) doc.get("id");
                String address = (String) doc.get("address");
                String description = (String) doc.get("description");
                String from = (String) doc.get("from");
                String to = (String) doc.get("to");
                String busName = (String) doc.get("name");
                String price = (String) doc.get("price");
                String dateStart2 = (String) doc.get("dateStart");
                String dateEnd2 = (String) doc.get("dateEnd");
                String timeStart2 = (String) doc.get("timeStart");
                String timeEnd2 = (String) doc.get("timeEnd");

                dateStart = dateStart2;
                dateEnd = dateEnd2;
                timeStart = timeStart2;
                timeEnd = timeEnd2;
                isUpdate = true;

                List<HashMap<String, String>> seats = (List<HashMap<String, String>>) doc.get("seats");

                idAdrress.setText(address);
                idDescription.setText(description);
                idStartPoint.setText(from);
                idEndPoint.setText(to);
                idDateStart.setText(dateStart2);
                idDateEnd.setText(dateEnd2);
                idStartTime.setText(timeStart2);
                idEndTime.setText(timeEnd2);
                idPrice.setText(price);
                idBusName.setText(busName);
                idSeatNumber.setText(seats.size() + "");
            }
        });
    }

    private void openDatePicker(DateTimeCallBack callBack) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (datePicker, year, month, day) -> {

            //Showing the picked value in the textView
            //textView.setText();
            String value = String.valueOf(year) + "." + String.valueOf(month + 1) + "." + String.valueOf(day);
            callBack.callback(value);

        }, 2023, 01, 20);

        datePickerDialog.show();
    }


    private void openTimePicker(DateTimeCallBack callBack) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme,
                (TimePickerDialog.OnTimeSetListener) (timePicker, hour, minute) -> {
                    //Showing the picked value in the textView
                    //textView.setText();
                    String value = String.valueOf(hour) + ":" + String.valueOf(minute);
                    callBack.callback(value);
                }, 15, 30, false);

        timePickerDialog.show();
    }

}
