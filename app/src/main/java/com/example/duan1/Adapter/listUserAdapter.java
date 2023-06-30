package com.example.duan1.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.detailsUserActivity;
import com.example.duan1.model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class listUserAdapter extends RecyclerView.Adapter<listUserAdapter.ViewHolder> {
    private Context context;
    private List<Users> listUser;

    public listUserAdapter(Context context, List<Users> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public listUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listUserAdapter.ViewHolder h, int position) {
        Users users = listUser.get(position);
        h.tvName.setText(users.getName());
        h.tvEmail.setText(users.getEmail());
        String sdt = users.getSdt();
        if(sdt != null ){
            h.tvSdt.setText(users.getSdt());
        }else {
            h.tvSdt.setText("người dùng chưa thêm số điện thoại");
        }
        String strimage = users.getImage();
        try {
            Picasso.with(context)
                    .load(strimage)
                    .into(h.imgUser);
        } catch (Exception e) {
            System.out.println("Lỗi load ảnh trong newsAdapter" + e);
        }
        h.icon_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete User").setMessage("Do you want to delete User")
                        .setNegativeButton("no" , null).setPositiveButton("Yes" , deleteUser(users.getId())).show();

            }
        });
            String id = String.valueOf(users.getId());
        h.itemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , detailsUserActivity.class);
                    intent.putExtra("Id" , id);
                    context.startActivity(intent);
            }

        });

    }

    private DialogInterface.OnClickListener deleteUser(int id) {
            String idUser = String.valueOf(id);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");
            myRef.child(idUser).removeValue();
            notifyDataSetChanged();
        return null;
    }

    @Override
    public int getItemCount() {
        if (listUser != null) {
            return listUser.size();
        } else {
            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvEmail, tvSdt;
        private ImageView icon_delete, imgUser;
        private RelativeLayout itemUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameUser);
            tvEmail = itemView.findViewById(R.id.tvEmailUser);
            tvSdt = itemView.findViewById(R.id.tvSdtUser);
            icon_delete = itemView.findViewById(R.id.icon_deleteUser);
            imgUser = itemView.findViewById(R.id.imgUser);
            itemUser = itemView.findViewById(R.id.itemUser);
        }
    }
}
