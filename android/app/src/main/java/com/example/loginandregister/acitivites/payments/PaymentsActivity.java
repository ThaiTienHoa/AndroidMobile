package com.example.loginandregister.acitivites.payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginandregister.MainActivity;
import com.example.loginandregister.R;
import com.example.loginandregister.model.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

//Thanh toán cập nhật thông tin tuyến xe
public class PaymentsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    CollectionReference bookingRef = db.collection("bookings");
    CollectionReference userRef = db.collection("users");

    CollectionReference busRef = db.collection("buses");

    TextView tvStartingPoint, tvEndingPoint, tvStartTime, tvEndingTime, textView5, tvPrice;
    Button btnDone;

    String from, to, travellingTime, price, dateStart, dateEnd, timeStart, timeEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_payment);
        tvStartingPoint = findViewById(R.id.tvStartingPoint);
        tvEndingPoint = findViewById(R.id.tvEndingPoint);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndingTime = findViewById(R.id.tvEndingTime);
        textView5 = findViewById(R.id.textView5);
        tvPrice = findViewById(R.id.tvPrice);
        btnDone = findViewById(R.id.btnDone);
        String email = getIntent().getStringExtra("email");
        String mobile = getIntent().getStringExtra("mobile");
        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");
        String busId = getIntent().getStringExtra("busId");
        String seatNo = getIntent().getStringExtra("seat_no");
        getBusesData(busId);
        btnDone.setOnClickListener(view -> {
            String idBooking = createOrder(from, to);
            updateSeat(busId, seatNo);
            updateBusIdForUser(busId);
            setBookingData(idBooking, busId, email, mobile, name, age, seatNo);
            Toast.makeText(getApplicationContext(), "Your order sucessfull", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }

    private void getBusesData(String busId) { //truy vấn collection "buses" trong database Firestore + ID
        busRef.document(busId).get().addOnSuccessListener(doc -> {
            if (doc != null) {
                from = (String) doc.getData().get("from");
                to = (String) doc.getData().get("to");
                price = (String) doc.getData().get("price");
                dateStart = (String) doc.getData().get("dateStart");
                dateEnd = (String) doc.getData().get("dateEnd");
                timeStart = (String) doc.getData().get("timeStart");
                timeEnd = (String) doc.getData().get("timeEnd");
                tvStartingPoint.setText(from);
                tvEndingPoint.setText(to);
                tvStartTime.setText(dateStart);
                tvEndingTime.setText(dateEnd);
                tvPrice.setText(price);
            }
        });
    }

    // Tạo đơn mới
    private String createOrder(String from, String to) {
        String idCollection = bookingRef.document().getId(); //Lấy Id từ firestore
        BookingModel bookingModel = new BookingModel( //Tạo đối tượng
                from,
                to,
                idCollection,
                ""
        );
        bookingRef.document(idCollection).set(bookingModel); //Lưu vào Firestore
        setBookingId(idCollection);
        return idCollection;
    }

    private void updateBusIdForUser(String busId) {
        String id = auth.getCurrentUser().getUid(); //Lấy id user đang đăng nhập
        userRef.document(id).get().addOnSuccessListener(doc -> { //truy vấn tới thông tin người dùng
            if (doc.getData().get("bookings") != null) {
                String bookingId = doc.getData().get("bookings").toString();
                bookingRef.document(bookingId).update("busId", busId); //Cập nhật busID cho user
            }
        });
    }

    //lưu mã đặt chỗ (bookingId) vào tài khoản người dùng hiện tại
    private void setBookingId(String bookingId) {
        String id = auth.getCurrentUser().getUid();
        userRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc.getData().get("bookings") != null) {
                    userRef.document(id).update("bookings", bookingId);
                } else {
                    userRef.document(id).update("bookings", bookingId);
                }
            }
        });
    }

    private void setBookingData(String bookingId, String busId, String email, String phone, String name, String age, String seatNo) {
        bookingRef.document(bookingId).update("email", email);
        bookingRef.document(bookingId).update("mobile", phone);
        bookingRef.document(bookingId).update("seat_no", seatNo);
        bookingRef.document(bookingId).update("name", name);
        bookingRef.document(bookingId).update("age", age);
        bookingRef.document(bookingId).update("busId", busId);
    }

    private void updateSeat(String busId, String seatNo) {
        busRef.document(busId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) { //Kiểm tra xem bus đã tồn tại chưa
                if (doc != null) {
                    int index = -1;
                    List<HashMap<String, Object>> seats = (List<HashMap<String, Object>>) doc.getData().get("seats");
                    //Lấy danh sách ghế từ bus,
                    // Mỗi thông tin tử trong mảng hashmap chứa trạng thái và số ghế
                    for (int i = 0; i < seats.size(); i++) {
                        HashMap<String, Object> seat = seats.get(i);
                        if (seat.get("seat_no").equals(seatNo)) {
                            index = i;
                            HashMap<String, Object> newData = new HashMap<>();
                            newData.put("available", "no"); //Cập nhật đã đặt
                            newData.put("seat_no", seatNo); //Cập nhật số ghế
                            seats.set(i, newData);
                            break;
                        }
                    }
                    if (index != -1) {
                        busRef.document(busId).update("seats", seats)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Update seats", "Cập nhật thành công"); //Cập nhật thành công thông báo thành công
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.w("Update seats", "Cập nhật thất bại", e); //Cập nhật thất bại thông báo thất bại
                                    }
                                });
                    }
                }
            }
        });
    }


}
