package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.duan1.Adapter.listUserAdapter;
import com.example.duan1.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class listUserActivity extends AppCompatActivity {
    private ImageView icon_backTT;
    private RecyclerView rcvListUser;
    private List<Users> mListUser;
    private com.example.duan1.Adapter.listUserAdapter listUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        initUi();
        clickBackPage();

        getDataUser();


    }

    private void clickBackPage() {
        icon_backTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void getDataUser() {
        mListUser = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUser.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users user = snapshot1.getValue(Users.class);
                    mListUser.add(user);
                }
                listUserAdapter = new listUserAdapter(listUserActivity.this , mListUser);
                rcvListUser.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rcvListUser.setAdapter(listUserAdapter);
                RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
                rcvListUser.addItemDecoration(decoration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    private void initUi() {
        icon_backTT = findViewById(R.id.icon_backTT);
        rcvListUser = findViewById(R.id.rcvListUser);
    }
}