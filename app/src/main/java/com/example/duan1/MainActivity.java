package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.Fragment.FragmentDaoCho;
import com.example.duan1.Fragment.FragmentQuanLiTin;
import com.example.duan1.Fragment.FragmentThem;
import com.example.duan1.Fragment.FragmentTrangChu;
import com.example.duan1.model.Users;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    SearchView searchView;
    FragmentTrangChu fragmentTrangChu = new FragmentTrangChu();
    FragmentQuanLiTin fragmentQuanLiTin = new FragmentQuanLiTin();
    FragmentDaoCho fragmentDaoCho = new FragmentDaoCho();
    FragmentThem fragmentThem = new FragmentThem();
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static String name;
    public static int id;
    private Broadcast broadcast;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        searchView = findViewById(R.id.searchView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        if (currentUser != null) {
            getUser();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    showDialogChooseListProduct();

                } else {
                    MainActivity.showDiaLogWarning(MainActivity.this, "Vui lòng đăng nhập để có thể đăng bán sản phẩm");
                }
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrangChu).commit();

                        return true;

                    case R.id.believe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentQuanLiTin).commit();

                        return true;

                    case R.id.fab:
                        return true;

                    case R.id.market:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentDaoCho).commit();

                        return true;

                    case R.id.more:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentThem).commit();
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
    }

    private void getUser() {
        List<Users> mListUser = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return;
        }
        myRef = database.getReference("Users");
        String email = currentUser.getEmail();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUser.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users user = snapshot1.getValue(Users.class);
                    mListUser.add(user);
                }

                for (int i = 0; i < mListUser.size(); i++) {
                    if (mListUser.get(i).getEmail().equals(email)) {
                        name = (mListUser.get(i).getName());
                        id = mListUser.get(i).getId();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error get Id and Name from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogChooseListProduct() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_chon_danh_muc);
        getUser();
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        RelativeLayout layout_batDongSan = dialog.findViewById(R.id.layout_batDongSan);
        RelativeLayout layout_XeCo = dialog.findViewById(R.id.layout_XeCo);
        RelativeLayout layout_thoiTrang = dialog.findViewById(R.id.layout_thoiTrang);
        RelativeLayout layout_dienTu = dialog.findViewById(R.id.layout_dienTu);
        RelativeLayout layout_giaiTri = dialog.findViewById(R.id.layout_giaiTri);
        RelativeLayout layout_dienGiaDung = dialog.findViewById(R.id.layout_dienGiaDung);
        ImageView icon_close = dialog.findViewById(R.id.icon_close);

        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layout_giaiTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), chonDanhMucGiaiTriActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("Id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        layout_dienGiaDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Id : " + id + " NAme : " + name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), chonDanhMucDienMayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("Id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        layout_batDongSan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Id : " + id + " NAme : " + name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), chonDanhMucBDSActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("Id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        layout_XeCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Id : " + id + " NAme : " + name, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), chonDanhMucXeCoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("Id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        layout_thoiTrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), chonDanhMucThoiTrangAcrivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("Id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        layout_dienTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Id : " + id + " NAme : " + name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), chonDanhMucDoDienTuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("Id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void initUi() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.fab);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrangChu).commit();
        broadcast = new Broadcast();
    }

    public static void showDiaLogWarning(Context context, String text) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(text).setTitle("Warning").setIcon(R.drawable.ic_warning)
                .setNegativeButton("Ok", null);
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcast, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcast);
        super.onStop();
    }



}
