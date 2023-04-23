package com.example.loginandregister.acitivites.manageUser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginandregister.R;
import com.example.loginandregister.model.RouteModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.ViewHolder> {

    List<Map<String, Object>> listUser;

    public ManageUserAdapter(List<Map<String, Object>> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_manager_user,
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> user = listUser.get(position);
        holder.tvUserEmail.setText((String) user.get("mobileNo"));
        holder.tvSdt.setText("Email: " + (String) user.get("mobileNo"));
        holder.tvUserID.setText("User id: " + (String) user.get("userId"));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserEmail, tvSdt, tvUserID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvSdt = itemView.findViewById(R.id.tvSdt);
            tvUserID = itemView.findViewById(R.id.tvUserId);
        }
    }
}
