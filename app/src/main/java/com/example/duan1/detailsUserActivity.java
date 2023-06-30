package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duan1.Adapter.listUserAdapter;
import com.example.duan1.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class detailsUserActivity extends AppCompatActivity {
private TextView tvNameUser , tvEmailUser , tvSdt , tvPass, tvAddress , tvSex;
private ImageView imgAvtUser ,backPage;
private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        initUi();
        getDataUser();
        clickBAckPage();
    }

    private void clickBAckPage() {
        backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getDataUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users user = snapshot1.getValue(Users.class);
                    if(user.getId() == id) {
                        tvNameUser.setText(user.getName());
                        tvEmailUser.setText(user.getEmail());
                        tvAddress.setText(user.getAddress());
                        tvSdt.setText(user.getSdt());
                        tvPass.setText(user.getPassword());
                        tvSex.setText(user.getSex());
                        String strimage = user.getImage();
                        try {
                            Picasso.with(getApplicationContext())
                                    .load(strimage)
                                    .into(imgAvtUser);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiết User" + e);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUi() {
        Intent intent = getIntent();
        String strId = intent.getStringExtra("Id");
        id = Integer.parseInt(strId);
        tvNameUser = findViewById(R.id.tvNameUserDt);
        tvEmailUser = findViewById(R.id.tvEmailUserDt);
        tvSdt = findViewById(R.id.tvSdtlUserDt);
        tvPass = findViewById(R.id.tvPasslUserDt);
        tvAddress = findViewById(R.id.tvAddresslUserDt);
        tvSex = findViewById(R.id.tvSexlUserDt);
        imgAvtUser = findViewById(R.id.img_avt_user);
        backPage = findViewById(R.id.icon_backDt);
        }
}