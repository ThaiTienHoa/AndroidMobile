package com.example.loginandregister.acitivites.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginandregister.R;
import com.example.loginandregister.acitivites.route.RouteListActivity;
import com.example.loginandregister.model.BookingModel;
import com.example.loginandregister.model.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    EditText etEnterSource;
    EditText etEnterDestination;
    EditText etDate;
    Button btnSearch;
    TextView tvToDay;
    TextView tvTomorrow;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bookings = db.collection("bookings");
    CollectionReference users = db.collection("users");

    Calendar calendar;
    int currentMonth, currentDay, dayOfWeek;

    Location sourceLocation = new Location("", "");
    Location destinationLocation = new Location("", "");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_home);

        etEnterSource = findViewById(R.id.etEnterSource);
        etEnterDestination = findViewById(R.id.etEnterDestination);
        etDate = findViewById(R.id.etDate);
        btnSearch = findViewById(R.id.btnSearch);
        tvToDay = findViewById(R.id.tvToday);
        tvTomorrow = findViewById(R.id.tvTomorrow);
        btnSearch = findViewById(R.id.btnSearch);

        calendar = Calendar.getInstance();
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        String flag = getIntent().getStringExtra("flag");
        String idLocationS = getIntent().getStringExtra("idS");
        String nameLocationS = getIntent().getStringExtra("nameS");
        String idLocationD = getIntent().getStringExtra("idD");
        String nameLocationD = getIntent().getStringExtra("nameD");

        if (flag != null) {
            sourceLocation = new Location(idLocationS, nameLocationS);
            destinationLocation = new Location(idLocationD, nameLocationD);
            etEnterSource.setText(sourceLocation.getName());
            etEnterDestination.setText(destinationLocation.getName());
        }

        etEnterSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LocationSearchActivity.class);
                intent.putExtra("flag", "source");
                intent.putExtra("idS", sourceLocation.getId());
                intent.putExtra("nameS", sourceLocation.getName());
                intent.putExtra("idD", destinationLocation.getId());
                intent.putExtra("nameD", destinationLocation.getName());
                startActivity(intent);
            }
        });

        etEnterDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LocationSearchActivity.class);
                intent.putExtra("flag", "destination");
                intent.putExtra("idS", sourceLocation.getId());
                intent.putExtra("nameS", sourceLocation.getName());
                intent.putExtra("idD", destinationLocation.getId());
                intent.putExtra("nameD", destinationLocation.getName());
                startActivity(intent);
            }
        });

        List<String> monthName = List.of("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec");
        List<String> weekName = List.of("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        tvToDay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int day = (dayOfWeek - 1) % 7;
                String dayName = weekName.get(day);
                String month_Name = monthName.get(currentMonth);
                etDate.setText(dayName + ", " + currentDay + " " + month_Name);
            }
        });

        tvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = (dayOfWeek) % 7;
                String dayName = weekName.get(day);
                String month_Name = monthName.get(currentMonth);
                etDate.setText(dayName + ", " + currentDay + " " + month_Name);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idCollection = bookings.document().getId();

                BookingModel bookingModel = new BookingModel(
                        etDate.getText().toString(),
                        etEnterDestination.getText().toString(),
                        etEnterSource.getText().toString(),
                        idCollection,
                        ""
                );
                bookings.document(idCollection).set(bookingModel);
                setBookingId(idCollection);
                startActivity(new Intent(getApplicationContext(), RouteListActivity.class));
            }
        });
    }

    private void setBookingId(String bookingId) {
        String id = auth.getCurrentUser().getUid();
        users.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc.getData().get("bookings") != null) {
                    users.document(id).update("bookings", bookingId);
                } else {
                    users.document(id).update("bookings", bookingId);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}